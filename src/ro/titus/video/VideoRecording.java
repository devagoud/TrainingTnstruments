package ro.titus.video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JFrame;

import ro.titus.audio.AudioRecording;
import ro.titus.gui.MainFrame;
import ro.titus.java.video.rec.JRecorder;
import ro.titus.tools.KeyboardAndMouseMonitor;
import ro.titus.tools.SessionRecOptions;

public class VideoRecording {

	public static final Object videoLock = new Object();
	public static volatile int videoMode = 0;
	public static JRecorder jrec;

	public static final Object pauseLock = new Object();
	public static volatile long pauseTime = 0;
	public static volatile boolean pause = false;
	public static volatile long pauseStart = 0;
	public static volatile long totalPauseTime = 0;
	public static volatile long startTime, endTime;

	public static void startRecording() {
		startTime = System.currentTimeMillis();
		MainFrame.getInstance().setState(JFrame.ICONIFIED);
		
		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { synchronized (videoLock) { videoMode =
		 * 1; } } }).start();
		 */
		if (jrec == null) {
			jrec = new JRecorder();
		}
		if (SessionRecOptions.folder == null) {
			SessionRecOptions.folder = new File(String.valueOf(System
					.currentTimeMillis()));
			SessionRecOptions.folder.mkdir();
		}
		startMicRec();
		jrec.startRecording(SessionRecOptions.folder.getAbsolutePath()
				+ File.separator + "vid.cap");
		/*
		 * jrec.control.setActionCommand("stop");
		 * jrec.control.setText("Stop Recording");
		 */

	}

	public static void stopRecording() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (SessionRecOptions.includeMicAudio) {
					AudioRecording.stop(true);
				}
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (jrec != null) {
					jrec.stop();
				} else {
					System.out
							.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! Warning the JRecorder is null !!!!!!!!!!!!!!!!!!!!!!!!!!!");
				}
			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					synchronized (videoLock) {
						videoMode = 0;
					}

					synchronized (KeyboardAndMouseMonitor.eventsList) {
						try (ObjectOutputStream oos = new ObjectOutputStream(
								new FileOutputStream(new File(
										SessionRecOptions.folder, "video.obj")))) {
							oos.writeObject(KeyboardAndMouseMonitor.eventsList);
							oos.flush();
							oos.close();
							KeyboardAndMouseMonitor.eventsList.clear();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		HashMap<String, Long> meta = new HashMap<>();
		meta.put("duration", System.currentTimeMillis() - startTime - 1000);
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(new File(SessionRecOptions.folder,
						"meta.inf")))) {
			oos.writeObject(meta);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void pauseRecording() {
		synchronized (pauseLock) {
			pauseStart = System.currentTimeMillis();

			synchronized (videoLock) {
				videoMode = 0;
			}
			pause = true;
		}
	}

	public static void resumeRecording() {
		synchronized (pauseLock) {
			pauseTime = System.currentTimeMillis() - pauseStart;
			startTime += pauseTime;
			totalPauseTime += pauseTime;
			pause = false;
			startMicRec();
			KeyboardAndMouseMonitor.lastFrameTime += pauseTime;
			synchronized (videoLock) {
				videoMode = 1;
			}
			pauseLock.notifyAll();
		}
	}

	private static void startMicRec() {
		if (SessionRecOptions.includeMicAudio) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					AudioRecording.start();
				}
			}).start();
		}
	}

}
