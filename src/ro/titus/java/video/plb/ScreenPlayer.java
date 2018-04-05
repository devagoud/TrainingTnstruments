/*
 * This software is OSI Certified Open Source Software
 * 
 * The MIT License (MIT)
 * Copyright 2000-2001 by Wet-Wired.com Ltd., Portsmouth England
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions: 
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * 
 */

package ro.titus.java.video.plb;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.FileInputStream;
import java.io.IOException;

import ro.titus.player.EventsPlayer;
import ro.titus.player.Player;

public class ScreenPlayer implements Runnable {

	private ScreenPlayerListener listener;

	private MemoryImageSource mis = null;
	private Rectangle area;

	private FrameDecompressor decompressor;

	private boolean running;

	private int width;
	private int height;

	int[] pFrameData;
	int pResult;
	long pFrameTime;

	long startTime;
	long realTime;
	long increase;
	FileInputStream iStream;
	String filename;
	long totalTime;

	public ScreenPlayer(FileInputStream iStream, ScreenPlayerListener listener,
			long startTime, String filename, long totalTime) {
		this.totalTime = totalTime;
		this.filename = filename;
		this.startTime = startTime;
		this.listener = listener;
		this.iStream = iStream;
		initialize(iStream);
	}

	private void initialize(FileInputStream iStream) {
		System.out.println("@initialize");
		try {
			width = iStream.read();
			width = width << 8;
			width += iStream.read();

			height = iStream.read();
			height = height << 8;
			height += iStream.read();

			area = new Rectangle(width, height);
			decompressor = new FrameDecompressor(iStream, width * height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		System.out.println("@video starting the vide playback");
		if (running == false) {
			System.out
					.println("@video running is false, creating a new thread");
			new Thread(this, "Screen Player").start();
		} else {
			System.out.println("@video running is true");
		}
	}

	public void stop() {
		running = false;
	}

	public synchronized void run() {
		running = true;
		while (running) {

			synchronized (Player.lock) {
				if (Player.stop) {
					System.out.println("@video Player.stop is true");
					stop();
				}
				if (Player.paused) {
					try {
						long pauseTime = System.currentTimeMillis();
						Player.lock.wait();
						pauseTime = System.currentTimeMillis() - pauseTime;
						startTime += pauseTime;
						EventsPlayer.changeStartTime(startTime + 1500);
						System.out
								.println("video player done pausing, pause length: "
										+ pauseTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				readFrame();
			} catch (IOException ioe) {
				listener.showNewImage(null, null, 0);
				ioe.printStackTrace();
				break;
			}

		}

		try {
			if (iStream != null) {
				System.out
						.println("@ScreenPlayer, end of loop, closeing iStreame");
				iStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		listener.playerStopped();

	}

	private boolean readFrame() throws IOException {

		FrameDecompressor.FramePacket frame = decompressor.unpack();

		synchronized (Player.videoSeek) {
			System.out.println("@video entering seek lock");
			if (Player.videoSeek.seek()) {
				System.out.println("@video seek is true");
				if (Player.videoSeek.breaking()) {
					System.out.println("@video break is true");
					Player.videoSeek.setBreaking(false);
					try {
						iStream.close();
						iStream = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					iStream = new FileInputStream(filename);
					initialize(iStream);
					System.out.println("@video finished reopening iStream");
					frame = decompressor.unpack();
				}
				long time = Player.videoSeek.getTime();
				System.out.println("@video starting to seek to time: " + time);
				while (frame.getTimeStamp() < time) {
					frame = decompressor.unpack();
				}
				System.out.println("@video finished seeking, frame time: "
						+ frame.getTimeStamp() + " seek time: " + time);
				Player.videoSeek.setSeek(false);
				pFrameData = null;
				System.out.println("@video, waiting for event seek to finish");
				while (true) {
					synchronized (Thread.currentThread()) {
						try {
							Thread.currentThread().wait(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					synchronized (Player.eventsSeek) {

						if (Player.eventsSeek.finished()) {
							System.out
									.println("@video event seek has finished, notifying the object");
							Player.eventsSeek.notifyAll();
							break;
						} else {
							System.out
									.println("@video event seek hasn't finished");
						}
					}
				}

				startTime = (System.currentTimeMillis() - Math.min(
						frame.getTimeStamp(), time));
				System.out
						.println("@video reseting start time, real time now is: "
								+ (System.currentTimeMillis() - startTime));
			} else {
				System.out.println("@video, seek is false");
			}
			System.out.println("@video exiting sync block");
		}

		if (pFrameData == null) {
			setPreviosFrame(frame);
			return false;
		}

		if (pResult == 0 || pResult == -1) {
			listener.showNewImage(null, null, pFrameTime);
			setPreviosFrame(frame);
			return false;
		}

		mis = new MemoryImageSource(area.width, area.height, pFrameData, 0,
				area.width);
		mis.setAnimated(true);
		listener.showNewImage(Toolkit.getDefaultToolkit().createImage(mis),
				area, pFrameTime);

		synchronized (Thread.currentThread()) {
			try {
				/*
				 * System.out .println("waiting for: " + (frame.getTimeStamp() -
				 * pFrameTime) + " milliseconds");
				 */
				long nft = frame.getTimeStamp();
				long delay = (nft - pFrameTime);
				realTime = System.currentTimeMillis() - startTime;
				if (nft < realTime) {
					System.out
							.println("@video current frame bigger the real time");
					delay = 0;
				}
				if (delay > 100 || (nft - realTime) > 200) {
					delay = nft - realTime;
				}
				System.out.println("frame time: " + nft + " real time: "
						+ realTime);
				if (delay > 0) {
					Thread.currentThread().wait(delay);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setPreviosFrame(frame);
		if (frame.getTimeStamp() >= totalTime - 400
				|| pFrameTime >= totalTime - 400) {
			System.out.println("@video end of video");
			stop();
		}
		return false;
	}

	private void setPreviosFrame(FrameDecompressor.FramePacket frame) {
		pFrameData = frame.getData();
		pResult = frame.getResult();
		pFrameTime = frame.getTimeStamp();
	}

	public long getCurrentTime() {
		return pFrameTime;
	}

}