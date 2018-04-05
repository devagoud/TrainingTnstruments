package ro.titus.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.jnativehook.GlobalScreen;

import ro.titus.Entrance;
import ro.titus.gui.menus.KeyboardMenu;
import ro.titus.gui.menus.LoggerMenu;
import ro.titus.gui.menus.MouseMenu;
import ro.titus.gui.menus.SettingsMenu;
import ro.titus.gui.menus.ThemeMenu;
import ro.titus.tools.But;
import ro.titus.tools.Settings;
import ro.titus.video.VideoRecording;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	Color defBack;
	public static JMenuBar menuBar = new JMenuBar();
	public static JMenu components = new JMenu("Components");
	public static final Mouse mouse = Mouse.getInstance();
	public final KeyboardMenu keyboardMenu = KeyboardMenu.getInstance();
	public static Keyboard keyboard;
	public static ArrayList<DetachedComponent> detachedComponents = new ArrayList<>();
	public static JScrollPane loggerWithScroll;
	private final Logger logger = Logger.getInstance();

	static final MouseMenu mouseMenu = MouseMenu.getInstance();
	static final LoggerMenu loggerMenu = LoggerMenu.getInstance();

	static MainFrame singleton;
	static final Object attachingLock = new Object();
	static volatile boolean attaching = false;

	static final JPanel centerPanelHorizontal = new JPanel();
	static final JPanel centerPanelVertical = new JPanel();

	private static boolean mouseIsVisibel = true;
	private static boolean keyboardIsVisible = true;
	public static boolean loggerIsVisible = true;

	public static MainFrame getInstance() {
		if (singleton == null) {
			singleton = new MainFrame();
		}
		return singleton;
	}

	public MainFrame() {
		try {
			setIconImage(Settings.icon);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setTitle("Catch On");
		if (Settings.macSystem) {
			keyboard = KeyboardMacSmall.getInstance();
		} else {
			keyboard = Keyboard.getInstance();
		}

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);

		defBack = getBackground();

		SettingsMenu settings = new SettingsMenu();
		ThemeMenu theme = new ThemeMenu();

		components.add(loggerMenu);
		components.add(keyboardMenu);
		components.add(mouseMenu);

		menuBar.add(settings);
		menuBar.add(theme);
		menuBar.add(components);

		final BufferedImage stopN = getStopButton(true);
		final BufferedImage stopH = getStopButton(false);

		final BufferedImage recN = getRecButton(true);
		final BufferedImage recH = getRecButton(false);

		final BufferedImage pauseN = getPause(true);
		final BufferedImage pauseH = getPause(false);

		final But stop = new But(stopN, stopH, stopN, 20, 20);
		final But rec = new But(recN, recH, recN, 20, 20);

		rec.setActionCommand("rec");

		menuBar.add(Box.createHorizontalStrut(45));
		menuBar.add(rec);
		menuBar.add(Box.createHorizontalStrut(12));
		menuBar.add(stop);

		rec.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (rec.getActionCommand().equals("rec")) {
					rec.changeNormal(pauseN);
					rec.changeHover(pauseH);
					rec.changePressed(pauseN);
					rec.setActionCommand("pause");
					setTitle("Recording...");
					new Thread(new Runnable() {
						@Override
						public void run() {
							boolean resume = false;
							synchronized (VideoRecording.pauseLock) {
								if (VideoRecording.pause) {
									resume = true;
								}
							}
							if (resume) {
								VideoRecording.resumeRecording();
							} else {
								VideoRecording.startRecording();
							}
						}
					}).start();
				} else {
					rec.setActionCommand("rec");
					rec.changeNormal(recN);
					rec.changeHover(recH);
					rec.changePressed(recN);
					setTitle("Recording was paused");
					new Thread(new Runnable() {
						@Override
						public void run() {
							VideoRecording.pauseRecording();
						}
					}).start();
				}
			}
		});

		stop.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				rec.setActionCommand("rec");
				rec.changeNormal(recN);
				rec.changeHover(recH);
				rec.changePressed(recN);

				setTitle("Catch On");

				new Thread(new Runnable() {
					@Override
					public void run() {
						VideoRecording.stopRecording();
					}
				}).start();
			}
		});

		setJMenuBar(menuBar);

		loggerWithScroll = new JScrollPane(logger);

		synchronized (Settings.horizontalLoggerLock) {
			if (Settings.horizontalLogger) {
				loggerWithScroll.setPreferredSize(new Dimension(1200, 75));
				loggerWithScroll.setMaximumSize(new Dimension(2000, 75));
				loggerWithScroll.setMinimumSize(new Dimension(94, 75));
				loggerWithScroll
						.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			} else {
				loggerWithScroll.setPreferredSize(new Dimension(210, 254));
				loggerWithScroll.setMaximumSize(new Dimension(210, 2000));
			}
		}

		mouse.setPreferredSize(new Dimension(120, 254));
		mouse.setMaximumSize(new Dimension(350, 620));

		if (Settings.macSystem) {
			keyboard.setPreferredSize(new Dimension(590, 254));
		} else {
			keyboard.setPreferredSize(new Dimension(900, 254));
		}

		JScrollBar upAndDown = loggerWithScroll.getHorizontalScrollBar();
		JScrollBar leftAndRight = loggerWithScroll.getVerticalScrollBar();

		AdjustmentListener adjLis = new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				repaint();
			}
		};

		upAndDown.addAdjustmentListener(adjLis);
		leftAndRight.addAdjustmentListener(adjLis);

		centerPanelHorizontal.setLayout(new BoxLayout(centerPanelHorizontal,
				BoxLayout.X_AXIS));

		synchronized (Settings.horizontalLoggerLock) {
			if (!Settings.horizontalLogger) {
				centerPanelHorizontal.add(loggerWithScroll);
			}
		}

		centerPanelHorizontal.add(keyboard);
		centerPanelHorizontal.add(mouse);

		centerPanelVertical.setLayout(new BoxLayout(centerPanelVertical,
				BoxLayout.Y_AXIS));
		centerPanelVertical.add(centerPanelHorizontal);
		synchronized (Settings.horizontalLoggerLock) {
			if (Settings.horizontalLogger) {
				centerPanelVertical.add(loggerWithScroll);
			}
		}

		getContentPane().add(centerPanelVertical, BorderLayout.CENTER);

		pack();
		setVisible(true);
		System.out.println("############################### dimensions: "
				+ getSize());
	}

	public void detachComponent(Component pan, String componentName) {
		DetachedComponent dc = new DetachedComponent(pan, componentName);
		removeComponentFromMainFrame(pan);
		synchronized (detachedComponents) {
			detachedComponents.add(dc);
		}
	}

	public void attachComponent(String componentName) {
		synchronized (attachingLock) {
			attaching = true;
		}
		synchronized (detachedComponents) {
			Iterator<DetachedComponent> it = detachedComponents.iterator();
			while (it.hasNext()) {
				DetachedComponent dc = it.next();
				if (dc.getComponentName().equals(componentName)) {
					dc.setVisible(false);
					dc.dispose();
					dc = null;
					it.remove();
				}
			}
		}

		showComponentInMainFrame(componentName);
	}

	public void closeComponent(String componentName) {
		System.out.println("removeing component: " + componentName);

		synchronized (Settings.componentsStateLock) {
			synchronized (Entrance.km) {
				synchronized (attachingLock) {
					if (!attaching) {
						synchronized (detachedComponents) {
							Iterator<DetachedComponent> it = detachedComponents
									.iterator();
							while (it.hasNext()) {
								DetachedComponent dc = it.next();
								if (dc.getComponentName().equals(componentName)) {
									dc.setVisible(false);
									dc.dispose();
									dc = null;
									it.remove();
								}
							}
						}

						if (componentName.equals("mouse")) {
							Settings.mouseActive = false;
							GlobalScreen.getInstance()
									.removeNativeMouseListener(Entrance.km);
							GlobalScreen
									.getInstance()
									.removeNativeMouseWheelListener(Entrance.km);
							mouseMenu.removeMenuItem();
							mouseMenu.changeTitle(1, true);
							removeComponentFromMainFrame(mouse);
							components.add(mouseMenu);
						} else if (componentName.equals("keyboard")) {
							if (!Settings.loggerActive) {
								GlobalScreen.getInstance()
										.removeNativeKeyListener(Entrance.km);
							}
							keyboardMenu.removeMenuItem();
							keyboardMenu.changeTitle(1, true);
							removeComponentFromMainFrame(keyboard);
							components.add(keyboardMenu);
							Settings.keyboardActive = false;
						} else if (componentName.equals("logger")) {
							Settings.loggerActive = false;
							loggerMenu.removeMenuItem();
							loggerMenu.changeTitle(1, true);
							components.add(loggerMenu);
							if (!Settings.keyboardActive) {
								GlobalScreen.getInstance()
										.removeNativeKeyListener(Entrance.km);
							}
							removeComponentFromMainFrame(loggerWithScroll);
						}
					}
					attaching = false;
				}
			}
			pack();

		}

	}

	public void openComponent(String componentName) {
		synchronized (Settings.componentsStateLock) {

			synchronized (Entrance.km) {
				if (componentName.equals("mouse")) {
					GlobalScreen.getInstance().addNativeMouseListener(
							Entrance.km);
					GlobalScreen.getInstance().addNativeMouseWheelListener(
							Entrance.km);

					mouseMenu.changeTitle(0, false);
					mouseMenu.addMenuItem();
					mouseMenu.changeTitle(1, false);
					Settings.mouseActive = true;
					showComponentInMainFrame("mouse");
				} else if (componentName.equals("keyboard")) {
					if (!Settings.loggerActive) {
						GlobalScreen.getInstance().addNativeKeyListener(
								Entrance.km);
					}
					keyboardMenu.changeTitle(0, false);
					keyboardMenu.addMenuItem();
					keyboardMenu.changeTitle(1, false);
					Settings.keyboardActive = true;
					showComponentInMainFrame("keyboard");
				} else if (componentName.equals("logger")) {
					if (!Settings.keyboardActive) {
						GlobalScreen.getInstance().addNativeKeyListener(
								Entrance.km);
					}
					Settings.loggerActive = true;
					loggerMenu.changeTitle(0, false);
					loggerMenu.addMenuItem();
					loggerMenu.changeTitle(1, false);
					showComponentInMainFrame("logger");
				}
			}
			pack();
		}
	}

	public void showComponentInMainFrame(String componentName) {
		centerPanelHorizontal.removeAll();
		centerPanelVertical.removeAll();

		if (componentName.equals("logger")) {
			loggerIsVisible = true;
		} else if (componentName.equals("keyboard")) {
			keyboardIsVisible = true;
		} else if (componentName.equals("mouse")) {
			mouseIsVisibel = true;
		}

		System.out.println("mouse: " + mouseIsVisibel + " keyboard: "
				+ keyboardIsVisible + " logger: " + loggerIsVisible);

		synchronized (Settings.horizontalLoggerLock) {
			if (!Settings.horizontalLogger) {
				if (loggerIsVisible) {
					centerPanelHorizontal.add(loggerWithScroll);
				}
			}
		}
		if (keyboardIsVisible) {
			centerPanelHorizontal.add(keyboard);
		}
		if (mouseIsVisibel) {
			centerPanelHorizontal.add(mouse);
		}

		centerPanelVertical.add(centerPanelHorizontal);
		synchronized (Settings.horizontalLoggerLock) {
			if (Settings.horizontalLogger) {
				if (loggerIsVisible) {
					centerPanelVertical.add(loggerWithScroll);
				}
			}
		}

		centerPanelHorizontal.revalidate();
		centerPanelVertical.revalidate();
		revalidate();
		repaint();

	}

	public void removeComponentFromMainFrame(Component component) {
		if (component instanceof Mouse) {
			mouseIsVisibel = false;
		} else if (component instanceof Keyboard) {
			keyboardIsVisible = false;
		} else if (component instanceof JScrollPane) {
			loggerIsVisible = false;
		}

		if (component instanceof JScrollPane) {
			synchronized (Settings.horizontalLoggerLock) {
				if (Settings.horizontalLogger) {
					centerPanelVertical.remove(component);
				} else {
					centerPanelHorizontal.remove(component);
				}
			}
		} else {
			centerPanelHorizontal.remove(component);
		}

		revalidate();
		repaint();
	}

	public void changeKeyboardLayout(boolean small) {

		removeComponentFromMainFrame(keyboard);
		synchronized (Keyboard.buttons) {
			Keyboard.buttons.clear();
		}

		Settings.macKeyboardBig = !small;

		if (small) {
			keyboard = KeyboardMacSmall.getInstance();
		} else {
			keyboard = KeyboardMacBig.getInstance();
		}

		showComponentInMainFrame("keyboard");

		keyboard.revalidate();
		keyboard.repaint();
		revalidate();
		repaint();
	}

	static BufferedImage active;
	// static BufferedImage inactive;
	static PopupMenu trayMenu = new PopupMenu();
	static SystemTray tray;
	static TrayIcon icon;
	static MenuItem trayMenuItem1;
	static MenuItem trayMenuItem2;

	public void addToTray() {

		if (SystemTray.isSupported()) {
			if (active == null) {
				try {
					active = ImageIO.read(new File("icon.png"));
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			/*
			 * if (inactive == null) { try { inactive = ImageIO.read(new
			 * File("active.png")); } catch (IOException e) {
			 * e.printStackTrace(); return; } }
			 */
			if (tray == null) {
				tray = SystemTray.getSystemTray();
			}
			if (trayMenu == null) {
				trayMenu = new PopupMenu();
				trayMenuItem1 = new MenuItem("Stop Recording");
				trayMenuItem2 = new MenuItem("Exit");

				ActionListener l = new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (e.getSource() == trayMenuItem1) {
							VideoRecording.stopRecording();
							MainFrame.getInstance().setState(NORMAL);
							tray.remove(icon);
						} else {

						}
					}
				};

				trayMenuItem1.addActionListener(l);
				trayMenuItem2.addActionListener(l);

				trayMenu.add(trayMenuItem1);

			}
			if (icon == null) {
				if (active != null && trayMenu != null && tray != null) {
					icon = new TrayIcon(active, "Recording", trayMenu);
				}
			}

			if (icon != null && active != null && trayMenu != null
					&& tray != null) {
				try {
					tray.add(icon);
					MainFrame.getInstance().setState(ICONIFIED);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private BufferedImage getStopButton(boolean mode) {
		BufferedImage stop = new BufferedImage(20, 20,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) stop.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		/*
		 * g2.setColor(getBackground()); g2.fillRect(0, 0, 40, 40);
		 */
		if (mode) {
			g2.setColor(Color.black);
		} else {
			g2.setColor(Color.blue);
		}
		g2.fillRoundRect(1, 1, 18, 18, 12, 12);
		return stop;
	}

	private BufferedImage getRecButton(boolean mode) {
		BufferedImage rec = new BufferedImage(20, 20,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) rec.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		/*
		 * g2.setColor(getBackground()); g2.fillRect(0, 0, 40, 40);
		 */
		if (mode) {
			g2.setColor(Color.red);
		} else {
			g2.setColor(Color.blue);
		}
		g2.fillOval(1, 1, 18, 18);
		return rec;
	}

	private BufferedImage getPause(boolean mode) {
		BufferedImage pause = new BufferedImage(20, 20,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) pause.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		/*
		 * g2.setColor(getBackground()); g2.fillRect(0, 0, 40, 40);
		 */
		if (mode) {
			g2.setColor(Color.green);
		} else {
			g2.setColor(Color.blue);
		}
		g2.fillRoundRect(1, 1, 7, 18, 12, 12);
		g2.fillRoundRect(12, 2, 7, 18, 12, 12);
		return pause;
	}

}
