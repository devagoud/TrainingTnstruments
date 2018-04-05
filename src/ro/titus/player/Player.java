package ro.titus.player;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ro.titus.audio.AudioPlayback;
import ro.titus.java.video.plb.ScreenPlayer;
import ro.titus.java.video.plb.ScreenPlayerListener;
import ro.titus.video.VideoRecording;

public class Player implements ScreenPlayerListener {

	public static final Object lock = new Object();
	public static volatile boolean paused = false;
	public static volatile boolean stop = false;
	public static File parentFolder;
	public static String videoFile;
	public static File micAudioFile;
	public static File sysAudioFile;
	public static File commandsFile;
	public static File meta;
	static ScreenPlayer screenPlayer;
	static ScreenPlayerListener listener;
	public static volatile boolean allGood = false;

	public static final Object guiSeekLock = new Object();
	public static boolean isSeeking = false;

	public static EventsPlayer eventsPlayer;

	public static FileInputStream videoStream;

	public static long pauseTime;

	public static Object eventsSeekLock = new Object();
	// public static boolean eventsSeek = false;
	public static long totalTime;
	public static final SeekObject videoSeek = new SeekObject();
	public static final SeekObject eventsSeek = new SeekObject();

	public Player() {
		listener = this;
	}

	public static boolean isAllGood() {
		return allGood;
	}

	public static void play() {
		stop = false;
		synchronized (lock) {
			if (paused) {
				paused = false;
				pauseTime = System.currentTimeMillis() - pauseTime;
				System.out.println("@Player pauseTime: " + pauseTime);
				lock.notifyAll();
				startAudioFrom(screenPlayer.getCurrentTime());
				return;
			}
		}
		if (allGood) {
			final long startTime = System.currentTimeMillis();
			initAndStartVP(startTime);
			if (!stop) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						synchronized (VideoRecording.videoLock) {
							VideoRecording.videoMode = 2;
						}
						eventsPlayer = new EventsPlayer();
						EventsPlayer.startPlayback(commandsFile,
								(startTime + 1500));
					}
				}).start();
				if (micAudioFile != null
						&& micAudioFile.getName().endsWith(".wav")) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							AudioPlayback.playAudioFile(micAudioFile, 0);
						}
					}).start();
				}
			} else {
				synchronized (VideoRecording.videoLock) {
					VideoRecording.videoMode = 2;
				}
				stop = false;
			}

			/*
			 * synchronized (lock) { paused = false; lock.notifyAll(); }
			 */
		}
	}

	public static void stop() {
		System.out.println("Player.stop()");
		stopAudio();
		synchronized (lock) {
			stop = true;
		}

		synchronized (videoStream) {
			System.out.println("Closeing the vide stream");
			if (videoStream != null) {
				try {
					videoStream.close();
					System.out.println("the vide stream was closed");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void pause() {
		stopAudio();
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Player.pause()");
				synchronized (lock) {
					paused = true;
				}
			}
		}).start();

	}

	public static void setFolder() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int i = fileChooser.showOpenDialog(PlayerGUI.playerGUI);
		if (i == JFileChooser.APPROVE_OPTION) {
			File folder = fileChooser.getSelectedFile();
			parentFolder = folder;
			File[] content = folder.listFiles();
			for (File f : content) {
				if (f.getName().endsWith(".cap")) {
					videoFile = f.getAbsolutePath();
				} else if (f.getName().endsWith(".obj")) {
					commandsFile = f;
				} else if (f.getName().endsWith(".wav")) {
					micAudioFile = f;
				} else if (f.getName().endsWith(".inf")) {
					meta = f;
				}
			}
			if (videoFile != null && commandsFile != null) {
				allGood = true;

				new Thread(new Runnable() {
					@SuppressWarnings("unchecked")
					@Override
					public void run() {

						try (ObjectInputStream ois = new ObjectInputStream(
								new FileInputStream(meta))) {
							HashMap<String, Long> map = (HashMap<String, Long>) ois
									.readObject();
							totalTime = map.get("duration");
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									PlayerGUI.canvas.toolsBar
											.setTotalTime(totalTime);
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}).start();

			} else {
				System.out.println("got a null one: " + (videoFile == null)
						+ "   " + (commandsFile == null));
				allGood = false;
				JOptionPane
						.showMessageDialog(
								PlayerGUI.playerGUI,
								"The folder doesn't contain all the files necessary for playback, there must be a '.cap' and a '.obj' file in the folder you've selected");
			}
		}
	}

	@Override
	public void showNewImage(Image image, Rectangle area, final long frameTime) {
		if (image == null || area == null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					PlayerGUI.canvas.toolsBar.setProgress(frameTime);
					PlayerGUI.canvas.repaint();
				}
			});
			return;
		} else {
			if (PlayerGUI.canvas.queue.size() >= 19) {
				PlayerGUI.canvas.queue.poll();
				PlayerGUI.canvas.queue.poll();
				PlayerGUI.canvas.queue.poll();
			}
			PlayerGUI.canvas.queue.add(new Object[] { image, area, frameTime });
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				PlayerGUI.canvas.repaint();
			}
		});
	}

	public static void startSeeking(long time) {

		boolean breaking = time < screenPlayer.getCurrentTime();
		System.out.println("@Player starting to seek to: " + time
				+ " break is: " + breaking);
		stopAudio();
		/*
		 * synchronized (eventsSeekLock) { eventsSeekTime = time; eventsSeek =
		 * true; eventsSeekBreak = breaking; } synchronized (seekLock) {
		 * seekTime = time; seek = true; seekBreak = breaking; synchronized
		 * (Thread.currentThread()) { try { Thread.currentThread().wait(100); }
		 * catch (InterruptedException e) { e.printStackTrace(); } } }
		 */

		synchronized (videoSeek) {
			videoSeek.setTime(time);
			videoSeek.setSeek(true);
			videoSeek.setBreaking(breaking);
		}

		synchronized (eventsSeek) {
			eventsSeek.setTime(time);
			eventsSeek.setSeek(true);
			eventsSeek.setBreaking(breaking);
			eventsSeek.setFinished(false);
		}

	}

	@Override
	public void playerPaused() {

	}

	@Override
	public void playerStopped() {
		stop();
	}

	public void seek(long mill) {

	}

	public static void initAndStartVP(final long startTime) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					try {
						if (videoStream != null) {
							System.out.println("@initAndStartVP() videoStream is not null, closeing...");
							videoStream.close();
							videoStream = null;
						} else {
							System.out
									.println("at initAndStartVP() videoStream is null");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					videoStream = new FileInputStream(videoFile);
					screenPlayer = new ScreenPlayer(videoStream, listener,
							startTime, videoFile, totalTime);
					System.out.println("@Player starting video playback");
					screenPlayer.play();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}, "Video playback thread").start();
	}

	private static void stopAudio() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				AudioPlayback.stopPlaying();
			}
		}).start();
	}

	public static void startAudioFrom(final long time) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AudioPlayback.playAudioFile(micAudioFile, time);
			}
		}).start();
	}

}
