package ro.titus.player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class PlayerGUI extends JFrame implements MouseMotionListener,
		MouseListener {

	/*public static void main(String[] args) {

		long time = 348539456;
		long hour = Math.max(0, TimeUnit.MILLISECONDS.toHours(time));
		long min = Math.max(
				0,
				(TimeUnit.MILLISECONDS.toMinutes(time
						- TimeUnit.HOURS.toMillis(hour))));
		long sec = Math.max(
				0,
				(TimeUnit.MILLISECONDS.toSeconds(time
						- TimeUnit.MINUTES.toMillis(min)
						- TimeUnit.HOURS.toMillis(hour))));
		String t = String.format("%02d:%02d:%02d", hour, min, sec);
		System.out.println(t);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new PlayerGUI();
			}
		});
	}*/

	int alpha = 100;
	int controlsHeight = 0;
	static Canvas canvas;
	long totalTime;
	static volatile boolean stopped = true;
	final Object ejectLock = new Object();
	static PlayerGUI playerGUI;
	static Player player = new Player();
	final Object seekLock = new Object();
	volatile boolean seeking = false;

	public PlayerGUI() {
		
		try {
			setIconImage(Settings.icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		playerGUI = this;
		setTitle("Media Player");
		setAlwaysOnTop(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Rectangle dim = new Rectangle(Toolkit.getDefaultToolkit()
				.getScreenSize());
		int width = (int) (dim.getWidth() / 3);
		int height = (int) (dim.getHeight() / 3);
		controlsHeight = height / 10;
		canvas = new Canvas(width);
		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		canvas.setBounds(0, 0, width, height);
		getContentPane().add(canvas, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	private void open() {
		Player.setFolder();
		System.out.println("open was pressed!!!!!!");
	}

	class Canvas extends JPanel {

		Image ejectNBut, ejectHBut;
		public Queue<Object[]> queue = new ArrayBlockingQueue<>(20, false);
		Object[] prev;
		Color black = Color.black;
		public volatile boolean ejectHover;
		int dim;
		ToolsBar toolsBar;

		public Canvas(int width) {
			dim = controlsHeight * 2;
			toolsBar = new ToolsBar(width, controlsHeight);
			try {
				ejectNBut = ImageIO.read(
						new File("Images" + File.separator + "Buttons"
								+ File.separator + "eject_n.png"))
						.getScaledInstance(dim, dim, Image.SCALE_SMOOTH);
				ejectHBut = ImageIO.read(
						new File("Images" + File.separator + "Buttons"
								+ File.separator + "eject_h.png"))
						.getScaledInstance(dim, dim, Image.SCALE_SMOOTH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			long frameTime = -1;
			int width = getWidth();
			int height = getHeight();
			g2.setColor(black);
			g2.fillRect(0, 0, width, height);
			System.out.println("queue size: " + queue.size());
			if (!queue.isEmpty()) {
				prev = queue.poll();
			}
			if (stopped) {
				synchronized (ejectLock) {
					if (ejectHover) {
						g2.drawImage(ejectHBut, ((width - dim) / 2),
								((height - dim) / 2), null);
					} else {
						g2.drawImage(ejectNBut, ((width - dim) / 2),
								((height - dim) / 2), null);
					}
				}
			} else {
				if (prev != null) {
					Image img = (Image) prev[0];
					Rectangle dim = (Rectangle) prev[1];
					frameTime = (long) prev[2];
					if (img == null || dim == null) {
						return;
					}
					Dimension d = Settings.getDimensions(width, height,
							(int) dim.getWidth(), (int) dim.getHeight());
					int offsetLeft = (int) ((width - d.getWidth()) / 2);
					int offsetTop = (int) ((height - d.getHeight()) / 2);
					g2.drawImage(img, offsetLeft, offsetTop,
							(int) (d.getWidth() + offsetLeft),
							(int) (d.getHeight() + offsetTop), 0, 0,
							(int) dim.getWidth(), (int) dim.getHeight(), null);
				}
			}
			synchronized (seekLock) {
				if (seeking) {
					g2.drawImage(toolsBar.getToolBarImage(getWidth()), 0,
							(getHeight() - controlsHeight), null);
				} else {
					g2.drawImage(
							toolsBar.getToolBarImage(getWidth(), frameTime), 0,
							(getHeight() - controlsHeight), null);
				}
			}

			g2.dispose();
		}

		boolean showingStarted;
		boolean hiddingStarted;
		Object showingLock = new Object();
		Object hiddingLock = new Object();

		public void startHiddeing() {
			synchronized (hiddingLock) {
				if (hiddingStarted) {
					return;
				}
				hiddingStarted = true;
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					while (alpha > 10) {
						synchronized (Thread.currentThread()) {
							try {
								Thread.currentThread().wait(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							synchronized (showingLock) {
								if (showingStarted) {
									break;
								}
							}
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									toolsBar.setAlpha(alpha = alpha - 10);
									repaint();
								}
							});
						}
					}
					synchronized (hiddingLock) {
						hiddingStarted = false;
					}
				}
			}).start();
		}

		public void startShowing() {
			synchronized (showingLock) {
				if (showingStarted) {
					return;
				}
				showingStarted = true;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (alpha < 90) {
						synchronized (Thread.currentThread()) {
							try {
								Thread.currentThread().wait(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							synchronized (hiddingLock) {
								if (hiddingStarted) {
									break;
								}
							}
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									toolsBar.setAlpha(alpha = alpha + 10);
									repaint();
								}
							});
						}
					}
					synchronized (showingLock) {
						showingStarted = false;
					}
				}
			}).start();
		}

	}

	int pressedComponent;
	boolean inSeekMode;

	@Override
	public void mouseDragged(MouseEvent e) {
		int y = (int) e.getY();
		int x = (int) e.getX();
		Rectangle r = canvas.getBounds();
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		int component = getHoverComponent(x, y, width, height);
		if (component == 3 || component == 5) {
			if ((!inSeekMode || (inSeekMode && component == pressedComponent))) {
				changeSeekPosition(component, width, x);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int y = (int) e.getY();
		int x = (int) e.getX();
		Rectangle r = canvas.getBounds();
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		int component = getHoverComponent(x, y, width, height);
		if (y > (height - controlsHeight)) {
			// show toolsbar
			canvas.startShowing();
		} else {
			// hide toolsbar
			canvas.startHiddeing();
		}

		boolean refresh = false;

		if (component == 1) {
			if (!canvas.toolsBar.isPlayHover()) {
				canvas.toolsBar.playToggleHover();
				refresh = true;
			}
		} else {
			if (canvas.toolsBar.isPlayHover()) {
				canvas.toolsBar.playToggleHover();
				refresh = true;
			}
		}
		if (component == 2) {
			if (!canvas.toolsBar.isStopHover()) {
				canvas.toolsBar.stopToggleHover();
				refresh = true;
			}
		} else {
			if (canvas.toolsBar.isStopHover()) {
				canvas.toolsBar.stopToggleHover();
				refresh = true;
			}
		}
		if (component == 4) {
			if (!canvas.toolsBar.isSoundHover()) {
				canvas.toolsBar.soundToggleHover();
				refresh = true;
			}

		} else {
			if (canvas.toolsBar.isSoundHover()) {
				canvas.toolsBar.soundToggleHover();
				refresh = true;
			}
		}
		if (component == 6) {
			synchronized (ejectLock) {
				if (!canvas.ejectHover) {
					canvas.ejectHover = true;
					refresh = true;
				}
			}
		} else {
			synchronized (ejectLock) {
				if (canvas.ejectHover) {
					canvas.ejectHover = false;
					refresh = true;
				}
			}
		}
		if (refresh) {
			canvas.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		inSeekMode = true;
		int y = (int) e.getY();
		int x = (int) e.getX();
		Rectangle r = canvas.getBounds();
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		pressedComponent = getHoverComponent(x, y, width, height);
		if (pressedComponent == 3) {
			synchronized (seekLock) {
				seeking = true;
				changeSeekPosition(pressedComponent, width, x);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		boolean playState = false;

		int y = (int) e.getY();
		int x = (int) e.getX();
		Rectangle r = canvas.getBounds();
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		int component = getHoverComponent(x, y, width, height);

		if (progressHasChanged) {
			progressHasChanged = false;
			synchronized (Player.videoSeek) {
				if (Player.videoSeek.seek()) {
					return;
				}
			}
			Player.startSeeking(canvas.toolsBar.getCurrent());
		}

		if (pressedComponent == 3) {
			synchronized (seekLock) {
				System.out
						.println("seeking to the current position, the playback should be stopped at this point");
				seeking = false;
			}
		}

		if (component == pressedComponent) {
			if (component == 1) {
				if (canvas.toolsBar.getPlayState()) {
					// is in play mode and pause was pressed
					System.out.println("pausing");
					playState = true;
					Player.pause();
				} else {
					if (Player.isAllGood()) {
						System.out.println("start playing");
						synchronized (ejectLock) {
							stopped = false;
						}
						Player.play();
					} else {
						synchronized (ejectLock) {
							if (stopped) {
								System.out.println("open file");
								open();
							}
						}
					}
				}
			} else if (component == 2) {
				// stop the playback if it was started
				System.out.println("stopping");
				Player.stop();
				synchronized (ejectLock) {
					stopped = true;
				}
				repaint();
			} else if (component == 3 || component == 5) {
				changeSeekPosition(component, width, x);
			} else if (component == 4) {
				if (canvas.toolsBar.getSoundState()) {
					System.out.println("mutting");
				} else {
					System.out.println("unmutting");
				}
			} else if (component == 6) {
				synchronized (ejectLock) {
					if (stopped) {
						System.out.println("open file");
						open();
					}
				}

			}

			synchronized (ejectLock) {
				playState = !stopped;
			}

			if ((component == 1 && playState) || component == 2
					|| component == 4 || component == 6) {
				pressButton(component);
			}
		}
		inSeekMode = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * @return 1 = play; 2 = stop; 3 = progressBar; 4 = sound; 5 = soundBar; 6 =
	 *         eject;
	 */
	public int getHoverComponent(int x, int y, int width, int height) {

		if (y >= (height - controlsHeight)) {
			if (x < controlsHeight) {
				return 1;
			} else if (x > controlsHeight && x < (controlsHeight * 2)) {
				return 2;
			} else if (x > (controlsHeight * 2)
					&& x < (width - (controlsHeight * 5))) {
				return 3;
			} else if (x > (width - (controlsHeight * 5))
					&& x < (width - (controlsHeight * 4))) {
				return 4;
			} else if (x > (width - (controlsHeight * 4))) {
				return 5;
			}
		} else if (y >= ((height - (controlsHeight * 2)) / 2)
				&& y <= (((height - (controlsHeight * 2)) / 2) + (controlsHeight * 2))) {
			if (x > ((width - (controlsHeight * 2)) / 2)
					&& x < (((width - (controlsHeight * 2)) / 2) + (controlsHeight * 2))) {
				return 6;
			}
		}

		return 0;
	}

	private void pressButton(final int but) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						if (but == 1) {
							canvas.toolsBar.playToggleHover();
						} else if (but == 2) {
							canvas.toolsBar.stopToggleHover();
						} else if (but == 4) {
							canvas.toolsBar.soundToggleHover();
						} else if (but == 6) {
							synchronized (ejectLock) {
								canvas.ejectHover = false;
							}
						}
						canvas.repaint();
					}
				});
				synchronized (Thread.currentThread()) {
					try {
						Thread.currentThread().wait(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						if (but == 1) {
							canvas.toolsBar.changePlayState();
							canvas.toolsBar.playToggleHover();
						} else if (but == 2) {
							canvas.toolsBar.stopToggleHover();
						} else if (but == 4) {
							if (canvas.toolsBar.getSoundState()) {
								canvas.toolsBar.mute();
							} else {
								canvas.toolsBar.unmute();
							}
							canvas.toolsBar.soundToggleHover();
						} else if (but == 6) {
							synchronized (ejectLock) {
								canvas.ejectHover = true;
							}
						}
						canvas.repaint();
					}
				});
			}
		}).start();
	}

	volatile boolean progressHasChanged;

	private void changeSeekPosition(int component, int width, int x) {
		synchronized (Player.videoSeek) {
			if (Player.videoSeek.seek()) {
				return;
			}
		}
		System.out.println("@seek x: " + x + " width: " + width + " comp: "
				+ component);
		if (component == 3) {
			if (stopped) {
				return;
			}
			int max = width - (controlsHeight * 7);
			x -= (controlsHeight * 2);
			int val = (x * 100) / max;
			canvas.toolsBar.setProgress(val);
			progressHasChanged = true;
		} else if (component == 5) {
			int max = (controlsHeight * 4);
			x -= (width - max);
			int val = (x * 100) / max;
			canvas.toolsBar.setVolum(val);
		}
		canvas.repaint();
	}
}
