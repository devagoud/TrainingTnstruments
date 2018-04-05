package ro.titus.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioPlayback {

	static Clip clip;
	static AudioInputStream inputStream;

	// private final static Object playingLock = new Object();
	// private static volatile boolean playing = false;

	public static void playAudioFile(File f, long time) {

		if (f.exists() && f.getName().endsWith(".wav")) {
			stopPlaying();
			try {

				inputStream = AudioSystem.getAudioInputStream(f);
				AudioFormat format = inputStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				clip = (Clip) AudioSystem.getLine(info);
				

				// clip = AudioSystem.getClip();
				Control[] controls = clip.getControls();
				System.out.println("@audioPLB controls length: "
						+ controls.length);
				for (int i = 0; i < controls.length; i++) {
					System.out.println("@audioPLB controls: " + controls[i]
							+ " type: " + controls[i].getType());
				}

				clip.addLineListener(new LineListener() {
					@Override
					public void update(LineEvent event) {
						if (event.getType() == LineEvent.Type.STOP) {
							clip.drain();
							clip.close();
							if (inputStream != null) {
								try {
									inputStream.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							System.out
									.println("end of audio file closeing the clip");

						}
					}
				});
				System.out.println("Opening file: " + f.getName());
				clip.open(inputStream);
				System.out.println("@audioPB starting audio playback");
				clip.start();
				if (time > 0) {
					seekTo(time);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (clip != null) {
					try {
						clip.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		} else {
			System.err.println("Audion file: " + f.getPath() + " not found");
		}

	}

	public static void seekTo(long time) {
		System.out.println("@audioPB seeking to: " + time);
		clip.setMicrosecondPosition(time * 1000);
	}

	public static void stopPlaying() {
		System.out.println("@audioPB stopping audio playback");

		try {
			if (clip != null) {
				System.out.println("closeing the audio clip");
				try {
					//clip.drain();
					clip.close();
					System.out.println("the audio clip was closed");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("The audio clip is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
