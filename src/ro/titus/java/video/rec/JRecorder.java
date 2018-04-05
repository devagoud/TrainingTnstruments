

package ro.titus.java.video.rec;

import java.io.FileOutputStream;

public class JRecorder implements ScreenRecorderListener {

	private static ScreenRecorder recorder;

	private static boolean shuttingDown = false;
	static volatile long frameTime = 0;

	public void startRecording(String fileName) {

		if (recorder != null) {
			return;
		}

		try {
			FileOutputStream oStream = new FileOutputStream(fileName);
			recorder = new DesktopScreenRecorder(oStream, this);
			recorder.startRecording();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void actionPerformed(ActionEvent ev) { if
	 * (ev.getActionCommand().equals("start") && recorder == null) { try {
	 * 
	 * new RecordingOptions(this);
	 * 
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } } else if
	 * (ev.getActionCommand().equals("stop") && recorder != null) {
	 * text.setText("Stopping"); recorder.stopRecording(); } }
	 * 
	 * public void frameRecorded(boolean fullFrame) { frameCount++; if (text !=
	 * null) { text.setText("Frame: " + frameCount); } }
	 */

	public void stop() {
		frameTime = recorder.getFrameTime();
		recorder.stopRecording();
	}

	public void recordingStopped() {
		if (!shuttingDown) {
			recorder = null;
		}
	}

	public static void shutdown() {
		shuttingDown = true;
		if (recorder != null) {
			recorder.stopRecording();
		}

	}

	public long getFrameTime() {
		return frameTime;
	}

}