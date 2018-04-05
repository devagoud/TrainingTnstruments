
package ro.titus.java.video.rec;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import ro.titus.tools.SessionRecOptions;
import ro.titus.video.VideoRecording;

public abstract class ScreenRecorder implements Runnable {

	private Rectangle recordArea;

	private int frameSize;
	private int[] rawData;

	private OutputStream oStream;

	private boolean recording = false;
	private boolean running = false;

	private long startTime;
	private long frameTime;
	private boolean reset;

	private ScreenRecorderListener listener;
	public int fps = 190;

	private class DataPack {
		public DataPack(int[] newData, long frameTime) {
			this.newData = newData;
			this.frameTime = frameTime;
		}

		public long frameTime;
		public int[] newData;
	}

	private class StreamPacker implements Runnable {
		Queue<DataPack> queue = new LinkedList<DataPack>();
		private FrameCompressor compressor;

		public StreamPacker(OutputStream oStream, int frameSize) {
			compressor = new FrameCompressor(oStream, frameSize);

			new Thread(this, "Stream Packer").start();
		}

		public void packToStream(DataPack pack) {
			while (queue.size() > 2) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
			}
			queue.add(pack);
		}

		public void run() {
			while (recording) {
				synchronized (VideoRecording.pauseLock) {
					if (VideoRecording.pause) {
						System.out.println("@videoRec starting to pause");
						System.out.println("@videoRec frameTime before pause: "
								+ frameTime);
						try {
							VideoRecording.pauseLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("@videoRec resuming from pause");
						System.out.println("@videoRec increasing startTime");
						startTime += VideoRecording.pauseTime;
						System.out.println("@videRec frameTime after pause: "
								+ (System.currentTimeMillis() - startTime));
					}
				}
				while (queue.isEmpty() == false) {
					DataPack pack = queue.poll();

					try {
						// long t1 = System.currentTimeMillis();
						compressor.pack(pack.newData, pack.frameTime, reset);
						// long t2 = System.currentTimeMillis();
						// System.out.println("  pack time:"+(t2-t1));

						if (reset == true) {
							reset = false;
						}
					} catch (Exception e) {
						e.printStackTrace();
						try {
							oStream.close();
						} catch (Exception e2) {
						}
						return;
					}
				}
				while (queue.isEmpty() == true) {
					try {
						Thread.sleep(50);
					} catch (Exception e) {
					}
				}
			}
		}
	}

	private StreamPacker streamPacker;

	public ScreenRecorder(OutputStream oStream, ScreenRecorderListener listener) {

		this.listener = listener;
		this.oStream = oStream;
	}

	public void triggerRecordingStop() {
		recording = false;
	}

	public synchronized void run() {
		startTime = System.currentTimeMillis();

		recording = true;
		running = true;
		long lastFrameTime = 0;
		long time = 0;

		frameSize = recordArea.width * recordArea.height;
		streamPacker = new StreamPacker(oStream, frameSize);

		while (recording) {

			time = System.currentTimeMillis();
			while (time - lastFrameTime < fps) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
				time = System.currentTimeMillis();
			}
			lastFrameTime = time;

			try {

				recordFrame();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					oStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				break;
			}
		}

		running = false;
		recording = false;

		listener.recordingStopped();
	}

	public abstract Rectangle initialiseScreenCapture();

	public abstract BufferedImage captureScreen(Rectangle recordArea);

	public void recordFrame() throws IOException {
		// long t1 = System.currentTimeMillis();
		BufferedImage bImage = captureScreen(recordArea);
		frameTime = System.currentTimeMillis() - startTime;
		// long t2 = System.currentTimeMillis();

		rawData = new int[frameSize];
		// System.out.println(recordArea);
		// System.out.println(bImage.getWidth() + "x" + bImage.getHeight());
		bImage.getRGB(0, 0, recordArea.width, recordArea.height, rawData, 0,
				recordArea.width);
		// long t3 = System.currentTimeMillis();

		streamPacker.packToStream(new DataPack(rawData, frameTime));

		// System.out.println("Times");
		// System.out.println("  capture time:"+(t2-t1));
		// System.out.println("  data grab time:"+(t3-t2));

		// listener.frameRecorded(false);
	}

	public void startRecording() {
		fps = SessionRecOptions.FramesPerSecond;
		recordArea = initialiseScreenCapture();

		if (recordArea == null) {
			return;
		}
		try {
			oStream.write((recordArea.width & 0x0000FF00) >>> 8);
			oStream.write((recordArea.width & 0x000000FF));

			oStream.write((recordArea.height & 0x0000FF00) >>> 8);
			oStream.write((recordArea.height & 0x000000FF));
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (VideoRecording.videoLock) {
					VideoRecording.videoMode = 1;
				}
			}
		}).start();

		new Thread(this, "Screen Recorder").start();
	}

	public void stopRecording() {
		triggerRecordingStop();

		int count = 0;
		while (running == true && count < 10) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
			count++;
		}

		try {
			oStream.flush();
			oStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public boolean isRecording() { return recording; }
	 */

	/*
	 * public int getFrameSize() { return frameSize; }
	 */

	public void pause() {
		synchronized (VideoRecording.pauseLock) {
			if (VideoRecording.pause) {
				System.out.println("@videoRec starting to pause");
				System.out.println("@videoRec frameTime before pause: "
						+ frameTime);
				try {
					VideoRecording.pauseLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("@videoRec resuming from pause");
				System.out.println("@videoRec increasing startTime");
				startTime += VideoRecording.pauseTime;
				System.out.println("@videRec frameTime after pause: "
						+ (System.currentTimeMillis() - startTime));
			}
		}
	}

	public long getFrameTime() {
		return frameTime;
	}

}