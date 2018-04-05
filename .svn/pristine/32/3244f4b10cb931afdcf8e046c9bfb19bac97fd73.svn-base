package ro.titus.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import ro.titus.tools.KeyboardAndMouseMonitor;

public class EventsPlayer {

	static long eventTime;
	static long realTime;
	volatile static long startTime;
	static int notStuck = 0;
	volatile static boolean hasSeek = false;
	static boolean hasContinued = false;

	public static void startPlayback(File f, long st) {
		ArrayList<Object[]> events = loadEvents(f);
		startPlayback(events, st);
	}

	private static void startPlayback(final ArrayList<Object[]> events, long st) {
		try {
			startTime = st;
			eventTime = 0;
			System.out
					.println("starting events playback, total number of events: "
							+ events.size());
			System.out.println("start time: " + startTime);
			for (Object[] obj : events) {

				synchronized (Player.eventsSeek) {
					System.out.println("@events entering seek block");
					if (Player.eventsSeek.seek()) {
						System.out.println("@events seek is true");
						if (Player.eventsSeek.breaking()) {
							System.out.println("@events break is true");
							Player.eventsSeek.setBreaking(false);
							new Thread(new Runnable() {

								@Override
								public void run() {
									startPlayback(
											events,
											(System.currentTimeMillis() - Player.eventsSeek
													.getTime()));
								}
							}).start();
							System.out
									.println("@events break is true, creating new loop and breaking from this one");
							break;
						}
						int temp = (int) obj[0];
						long time = Player.eventsSeek.getTime();
						System.out.println("@events starting to seek to: "
								+ time + " event time is: " + eventTime);
						if (eventTime < time) {
							eventTime += temp;
							continue;
						}
						System.out
								.println("@events finished seeking, event time: "
										+ eventTime + " seek time: " + time);
						Player.eventsSeek.setSeek(false);
						Player.eventsSeek.setFinished(true);
						System.out
								.println("@events setting seek to false, finished to true and starting to wait");
						Player.eventsSeek.wait();
						startTime = System.currentTimeMillis()
								- Math.min(eventTime, time);
						Player.startAudioFrom(time);
						System.out
								.println("@events, finished waiting, real time is: "
										+ (System.currentTimeMillis() - startTime));
					}
				}

				synchronized (Player.lock) {
					if (Player.stop) {
						return;
					}
					if (Player.paused) {
						try {
							System.out
									.println("@Mouse and Keyboard monitor, starting the pause");
							Player.lock.wait();
							System.out
									.println("at eventsPlayer the lock was notified");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				int delay = (int) obj[0];
				if (delay > 1) {
					realTime = System.currentTimeMillis() - startTime;
					eventTime += delay;
					synchronized (Thread.currentThread()) {
						if (realTime > eventTime) {
							delay = 0;
						}
						if (delay > 100 && (eventTime - realTime) > 200) {
							delay = (int) (eventTime - realTime);
						}
						if (delay > 0) {
							if (delay > 1400) {
								int temp = delay;
								while (temp > 0) {
									Thread.currentThread().wait(
											Math.min(temp, 100));
									temp -= 100;
								}
							} else {
								Thread.currentThread().wait(delay);
							}
						}
						System.out.println("event time: " + eventTime
								+ " real time: " + realTime + " delay: "
								+ delay);
					}
				}
				int type = (int) obj[1];
				switch (type) {
				case 0:
					KeyboardAndMouseMonitor.keyPressEvent((int) obj[2],
							(int) obj[3], (boolean) obj[4]);
					break;
				case 1:
					KeyboardAndMouseMonitor.keyReleaseEvent((int) obj[2],
							(int) obj[3], (boolean) obj[4]);
					break;
				case 2:
					KeyboardAndMouseMonitor.mousePressEvent((int) obj[2]);
					break;
				case 3:
					KeyboardAndMouseMonitor.mouseReleaseEvent((int) obj[2]);
					break;
				case 4:
					KeyboardAndMouseMonitor.mouseWellMove((int) obj[2]);
					break;

				default:
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Object[]> loadEvents(File f) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				f))) {
			ArrayList<Object[]> events = (ArrayList<Object[]>) ois.readObject();
			ois.close();
			return events;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void changeStartTime(long st) {
		startTime = st;
	}

}
