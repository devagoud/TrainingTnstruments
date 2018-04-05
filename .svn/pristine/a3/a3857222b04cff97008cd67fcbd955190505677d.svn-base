package ro.titus.tools;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import ro.titus.gui.Keyboard;
import ro.titus.gui.KeyboardMacSmall;
import ro.titus.gui.Logger;
import ro.titus.gui.Mouse;
import ro.titus.video.VideoRecording;

public class KeyboardAndMouseMonitor implements NativeKeyListener,
		NativeMouseInputListener, NativeMouseWheelListener {

	static volatile boolean specialKeyOn = false;
	static final Object specialKeyLock = new Object();
	final static ArrayList<String> keyCombination = new ArrayList<>(4);
	static volatile int prevKeyCode = 0;
	// static volatile int sameKeyCount = 0;
	static final Object prevKeyCodeLock = new Object();
	static volatile long firstPressed = 0;

	public static final ArrayList<String> keyboardDebug = new ArrayList<>();
	public static final ArrayList<String> mouseDebug = new ArrayList<>();

	public static ArrayList<Object[]> eventsList = new ArrayList<>();
	public static volatile long lastFrameTime = 0;

	public KeyboardAndMouseMonitor() throws NativeHookException {

		GlobalScreen.registerNativeHook();
		GlobalScreen.getInstance().addNativeKeyListener(this);
		GlobalScreen.getInstance().addNativeMouseListener(this);
		GlobalScreen.getInstance().addNativeMouseWheelListener(this);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {

		int mode = 0;
		synchronized (VideoRecording.videoLock) {
			mode = VideoRecording.videoMode;
		}
		if (mode == 0 || mode == 1) {
			keyPressEvent(e.getKeyCode(), e.getRawCode(),
					e.getKeyLocation() < 3);
			if (mode == 1) {
				synchronized (eventsList) {
					int delay = 0;
					if (lastFrameTime != 0) {
						delay = (int) (System.currentTimeMillis() - lastFrameTime);
					}
					eventsList.add(new Object[] { delay, 0, e.getKeyCode(),
							e.getRawCode(), e.getKeyLocation() < 3 });
					lastFrameTime = System.currentTimeMillis();
				}

			}
		}

	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		int mode = 0;
		synchronized (VideoRecording.videoLock) {
			mode = VideoRecording.videoMode;
		}
		if (mode == 0 || mode == 1) {
			keyReleaseEvent(e.getKeyCode(), e.getRawCode(),
					e.getKeyLocation() < 3);
			if (mode == 1) {
				synchronized (eventsList) {
					int delay = 0;
					if (lastFrameTime != 0) {
						delay = (int) (System.currentTimeMillis() - lastFrameTime);
					}
					eventsList.add(new Object[] { delay, 1, e.getKeyCode(),
							e.getRawCode(), e.getKeyLocation() < 3 });
					lastFrameTime = System.currentTimeMillis();
				}
			}
		}

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// System.out.println("typed: " + e.paramString());
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) {
		int mode = 0;
		synchronized (VideoRecording.videoLock) {
			mode = VideoRecording.videoMode;
		}
		if (mode == 0 || mode == 1) {
			mousePressEvent(arg0.getButton());
			if (mode == 1) {
				synchronized (eventsList) {
					int delay = 0;
					if (lastFrameTime != 0) {
						delay = (int) (System.currentTimeMillis() - lastFrameTime);
					}
					eventsList.add(new Object[] { delay, 2, arg0.getButton() });
					lastFrameTime = System.currentTimeMillis();
				}
			}
		}

	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {
		int mode = 0;
		synchronized (VideoRecording.videoLock) {
			mode = VideoRecording.videoMode;
		}
		if (mode == 0 || mode == 1) {
			mouseReleaseEvent(arg0.getButton());
			if (mode == 1) {
				synchronized (eventsList) {
					int delay = 0;
					if (lastFrameTime != 0) {
						delay = (int) (System.currentTimeMillis() - lastFrameTime);
					}
					eventsList.add(new Object[] { delay, 3, arg0.getButton() });
					lastFrameTime = System.currentTimeMillis();
				}
			}
		}

	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent arg0) {
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent arg0) {
	}

	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent arg0) {
		int mode = 0;
		synchronized (VideoRecording.videoLock) {
			mode = VideoRecording.videoMode;
		}
		if (mode == 0 || mode == 1) {
			mouseWellMove(arg0.getWheelRotation());
			if (mode == 1) {
				synchronized (eventsList) {
					int delay = 0;
					if (lastFrameTime != 0) {
						delay = (int) (System.currentTimeMillis() - lastFrameTime);
					}
					eventsList.add(new Object[] { delay, 4,
							arg0.getWheelRotation() });
					lastFrameTime = System.currentTimeMillis();
				}
			}
		}

	}

	/*@SuppressWarnings("unchecked")
	public static void startPlayBack(File f) {
		try {
			try (ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(f))) {
				eventsList = (ArrayList<Object[]>) ois.readObject();
				ois.close();
				System.out.println("*&^*&())*&)(&)( number of events: "
						+ eventsList.size());
				synchronized (Thread.currentThread()) {
					Thread.currentThread().wait(1000);
				}
				for (Object[] obj : eventsList) {

					synchronized (VideoRecording.videoLock) {
						if (VideoRecording.videoMode != 2) {
							return;
						}
					}

					synchronized (Player.lock) {
						if (Player.stop) {
							return;
						}
						if (Player.paused) {
							try {
								System.out.println("@Mouse and Keyboard monitor, starting the pause");
								Player.lock.wait();
								System.out.println("@Mouse and Keyboard monitor, done pauseing");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

					if ((int) obj[0] > 1) {
						synchronized (Thread.currentThread()) {
							Thread.currentThread().wait((int) obj[0]);
						}
					}
					int type = (int) obj[1];
					switch (type) {
					case 0:
						keyPressEvent((int) obj[2], (int) obj[3],
								(boolean) obj[4]);
						break;
					case 1:
						keyReleaseEvent((int) obj[2], (int) obj[3],
								(boolean) obj[4]);
						break;
					case 2:
						mousePressEvent((int) obj[2]);
						break;
					case 3:
						mouseReleaseEvent((int) obj[2]);
						break;
					case 4:
						mouseWellMove((int) obj[2]);
						break;

					default:
						break;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public static Object[] keyCodeMac(int keyCode) {
		boolean left = true;
		if (keyCode == 56) {
			keyCode = 16;
		} else if (keyCode == 60) {
			keyCode = 16;
			left = false;
		} else if (keyCode == 59) {
			keyCode = 17;
		} else if (keyCode == 58) {
			keyCode = 18;
		} else if (keyCode == 55) {
			keyCode = 524;
		}
		return new Object[] { keyCode, left };
	}

	public static void keyPressEvent(final int keyCodePrincipal,
			int rawKeyCode, boolean left) {
		synchronized (Settings.componentsStateLock) {
			synchronized (specialKeyLock) {
				if (Settings.keyboardActive) {

					int keyCode = keyCodePrincipal;
					if (Settings.macSystem) {
						if (keyCode == 16) {
							keyCode = rawKeyCode;
							Object[] obj = keyCodeMac(keyCode);
							keyCode = (int) obj[0];
							left = (boolean) obj[1];
						} else if (keyCode == 157) {
							keyCode = 524;
							left = false;
						} else if (keyCode == 18) {
							left = false;
						}
						KeyboardMacSmall.getInstance().changeKeyState(keyCode,
								1, left);
					} else {
						Keyboard.getInstance().changeKeyState(keyCode, 1, left);
					}

					/*
					 * synchronized (keyboardDebug) {
					 * keyboardDebug.add(e.paramString() + " " +
					 * Settings.codeToKey.get(keyCode)); }
					 */

				}
				if (Settings.loggerActive) {

					int keyCode = keyCodePrincipal;
					if (Settings.macSystem) {
						if (keyCode == 16) {
							keyCode = rawKeyCode;
							Object[] obj = keyCodeMac(keyCode);
							keyCode = (int) obj[0];
						} else if (keyCode == 157) {
							keyCode = 524;
						}
					}
					if (Settings.singleKeyLoggingEnable) {
						synchronized (prevKeyCodeLock) {
							if (prevKeyCode == keyCode) {
								// sameKeyCount++;
							} else {
								prevKeyCode = keyCode;
								firstPressed = System.currentTimeMillis();
							}
						}
					}
					if (keyCode == 9 || keyCode == 16 || keyCode == 17
							|| keyCode == 18 || keyCode == 524) {
						specialKeyOn = true;
					}

					if (specialKeyOn) {
						synchronized (Settings.codeToKey) {
							if (!keyCombination.contains(Settings.codeToKey
									.get(keyCode))) {
								/*
								 * System.out.println("adding key: " + keyCode +
								 * " to the keycombination map");
								 */
								keyCombination.add(Settings.codeToKey
										.get(keyCode));
							}
						}
					}
				}
			}
		}
	}

	public static void keyReleaseEvent(final int keyCodePrincipal,
			final int rawKeycode, boolean left) {
		synchronized (Settings.componentsStateLock) {

			synchronized (specialKeyLock) {
				if (Settings.keyboardActive) {
					int keyCode = keyCodePrincipal;
					if (Settings.macSystem) {
						if (keyCode == 16) {
							keyCode = rawKeycode;
							Object[] obj = keyCodeMac(keyCode);
							keyCode = (int) obj[0];
							left = (boolean) obj[1];
						} else if (keyCode == 157) {
							keyCode = 524;
							left = false;
						} else if (keyCode == 18) {
							left = false;
						}
						KeyboardMacSmall.getInstance().changeKeyState(keyCode,
								2, left);
					} else {
						Keyboard.getInstance().changeKeyState(keyCode, 2, left);
					}
					/*
					 * synchronized (keyboardDebug) {
					 * keyboardDebug.add(e.paramString() + " " +
					 * Settings.codeToKey.get(keyCode)); }
					 */

				}
				if (Settings.loggerActive) {

					int keyCode = keyCodePrincipal;
					if (Settings.macSystem) {
						if (keyCode == 16) {
							keyCode = rawKeycode;
							Object[] obj = keyCodeMac(keyCode);
							keyCode = (int) obj[0];
						} else if (keyCode == 157) {
							keyCode = 524;
						}
					}
					if (Settings.singleKeyLoggingEnable) {
						synchronized (prevKeyCodeLock) {

							/*
							 * System.out .println("prev: " + prevKeyCode +
							 * " current: " + keyCode + " count: " // +
							 * sameKeyCount + " time: " +
							 * (System.currentTimeMillis() - firstPressed));
							 */

							if (prevKeyCode == keyCode
									&& System.currentTimeMillis()
											- firstPressed > Settings.singleKeyLoggingTime) {// &&
																								// sameKeyCount
																								// >
																								// 15)
																								// {
								ArrayList<String> single = new ArrayList<>();
								single.add(Settings.codeToKey.get(keyCode));
								Logger.getInstance().addKeysCombination(single,
										true, false);
								single.clear();
								single = null;
							}
							prevKeyCode = 0;
						}

					}
					/*
					 * System.out.println("special key map size: " +
					 * keyCombination.size() + " current keycode: " + keyCode);
					 */

					if (keyCode == 9 || keyCode == 16 || keyCode == 17
							|| keyCode == 18 || keyCode == 524) {
						specialKeyOn = false;
						if (keyCombination.size() > 1) {
							Logger.getInstance().addKeysCombination(
									keyCombination, true, false);
						}
						keyCombination.clear();
					}

					/*
					 * System.out.println("special key map size: " +
					 * keyCombination.size() + " current keycode: " + keyCode);
					 */

				}
			}
		}
	}

	public static void mousePressEvent(final int BUTTON) {

		synchronized (Settings.componentsStateLock) {

			if (Settings.mouseActive) {
				if (BUTTON == MouseEvent.BUTTON1) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								Mouse.getInstance().pressButton("left");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});

				} else if (BUTTON == MouseEvent.BUTTON2) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								Mouse.getInstance().pressButton("right");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				} else if (BUTTON == MouseEvent.BUTTON3) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								Mouse.getInstance().pressButton("middle");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
	}

	public static void mouseReleaseEvent(final int BUTTON) {
		synchronized (Settings.componentsStateLock) {
			if (Settings.mouseActive) {
				if (BUTTON == MouseEvent.BUTTON1) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								Mouse.getInstance().releaseButton("left");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});

				} else if (BUTTON == MouseEvent.BUTTON2) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								Mouse.getInstance().releaseButton("right");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				} else if (BUTTON == MouseEvent.BUTTON3) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								Mouse.getInstance().releaseButton("middle");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
			synchronized (specialKeyLock) {
				if (specialKeyOn) {
					if (BUTTON == MouseEvent.BUTTON1
							|| BUTTON == MouseEvent.BUTTON2
							|| BUTTON == MouseEvent.BUTTON3) {
						String button = "";
						switch (BUTTON) {
						case MouseEvent.BUTTON1:
							button = "leftMouseButton";
							break;
						case MouseEvent.BUTTON2:
							button = "rightMouseButton";
							break;
						case MouseEvent.BUTTON3:
							button = "middleMouseButton";
							break;
						default:
							break;
						}
						keyCombination.add(button);
					}

				}
			}
		}
	}

	public static void mouseWellMove(final int i) {
		synchronized (Settings.componentsStateLock) {
			if (Settings.mouseActive) {
				final boolean up;

				if (i > 0) {
					up = false;
				} else {
					up = true;
				}

				new Thread(new Runnable() {

					@Override
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								try {
									Mouse.getInstance().mouseWell(up, true);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});

						synchronized (Thread.currentThread()) {
							try {
								Thread.currentThread().wait(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								try {
									Mouse.getInstance().mouseWell(up, false);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}).start();
			}
		}
	}

}
