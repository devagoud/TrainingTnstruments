package ro.titus.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

import ro.titus.tools.SessionRecOptions;
import ro.titus.video.VideoRecording;

public class AudioRecording {

	private static boolean keepRunning = false;
	private static TargetDataLine audioLine = null;
	private static AudioFormat format;

	public static void start() {
		if (SessionRecOptions.folder == null) {
			SessionRecOptions.folder = new File(String.valueOf(System
					.currentTimeMillis()));
		}
		format = new AudioFormat(8000.0f, 16, 1, true, true);
		System.out.println("@audioRec, format was set: " + format);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
			System.out.println("unsuported audio format");
			JOptionPane
					.showMessageDialog(null,
							"The defauld recording audio format is not supported on this computer");
			return;
		} else {
			System.out.println("got a supported audio format: " + format);
		}
		File temp = new File(SessionRecOptions.folder, "audioTemp"
				+ String.valueOf(System.currentTimeMillis()));
		try (FileOutputStream fos = new FileOutputStream(temp)) {
			audioLine = AudioSystem.getTargetDataLine(format);
			audioLine.open(format);
			audioLine.start();
			System.out.println("audioRecording was started");
			System.out.println("temp audio file: " + temp + " exists: "
					+ temp.exists());
			byte[] buffer = new byte[audioLine.getBufferSize() / 5];
			int bytesRead = 0;
			keepRunning = true;
			while (keepRunning) {
				bytesRead = audioLine.read(buffer, 0, buffer.length);
				fos.write(buffer, 0, bytesRead);
				synchronized (VideoRecording.pauseLock) {
					if (VideoRecording.pause) {
						break;
					}
				}
			}
			fos.flush();
			fos.close();
			// stop(false);
		} catch (Exception e) {
			stop(false);
			e.printStackTrace();
		}

	}

	public static void stop(boolean save) {
		System.out.println("@audioRec stoping audio rec, save is: " + save);
		keepRunning = false;
		if (audioLine != null && audioLine.isOpen()) {
			System.out.println("closeing audioLine");
			try {
				audioLine.stop();
				System.out.println("audio line was stoped");
				audioLine.drain();
				System.out.println("audio line was drained");
				audioLine.close();
				System.out.println("The audio line was closed");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (save) {
			System.out.println("saving audio file");
			saveToFile();
		}else{
			System.out.println("save is false");
		}
	}

	private static void saveToFile() {
		System.out.println("@audioRec saveToFile()");
		File temp = getMerged();
		if (temp.exists()) {
			AudioInputStream audioInputStream = null;
			long length = temp.length();
			try (FileInputStream fis = new FileInputStream(temp)) {
				System.out.println("@audioRec is format null: "
						+ (format == null) + " if is not: " + format);
				audioInputStream = new AudioInputStream(fis, format, length
						/ format.getFrameSize());
				AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
						new File(SessionRecOptions.folder, "audio.wav"));
				audioInputStream.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (audioInputStream != null) {
					try {
						audioInputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			temp.delete();
		} else {
			System.out.println("No audio was recorded this session");
		}
	}

	private static File getMerged() {
		System.out.println("@audioRec starting to merge all files.");
		File merged = new File(SessionRecOptions.folder, "audioMerged");
		File folder = SessionRecOptions.folder;
		System.out.println("@audioRec total number of files in the folder: "
				+ folder.listFiles().length);
		try (FileOutputStream mergedOut = new FileOutputStream(merged)) {
			byte[] buff = new byte[2528];
			int read;
			for (File audioTemp : folder.listFiles()) {
				if (audioTemp.getName().contains("audioTemp")) {
					System.out.println("@audioRec got an audio file: "
							+ audioTemp + " size: " + audioTemp.length());
					try (FileInputStream fis = new FileInputStream(audioTemp)) {
						while ((read = fis.read(buff, 0, buff.length)) != -1) {
							mergedOut.write(buff, 0, read);
						}
						System.out
								.println("@audioRec, finished adding file to merged audio");
						fis.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			mergedOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (File audioTemp : folder.listFiles()) {
			if (audioTemp.getName().contains("audioTemp")) {
				System.out.println("@audioRec, deleteing file: " + audioTemp);
				audioTemp.delete();
			}
		}

		return new File(SessionRecOptions.folder, "audioMerged");
	}

}
