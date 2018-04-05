package ro.titus.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class Logger extends JPanel {

	static Logger singleTon;
	JScrollPane scrollPan;

	ArrayList<LoggerKeyCombination> rows = new ArrayList<>();
	ArrayList<ArrayList<String>> displayedKeys = new ArrayList<>();
	private static final ArrayList<String> lastCombination = new ArrayList<>();

	public static Logger getInstance() {
		if (singleTon == null) {
			singleTon = new Logger();
		}
		return singleTon;
	}

	int width = Settings.defaultKeyWidth;
	int height = 40;
	int allWidth = 220;
	int allHeight = 155;

	private Logger() {
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		synchronized (Settings.horizontalLoggerLock) {
			if (Settings.horizontalLogger) {
				drawHorizontal(g2);
			} else {
				drawVertical(g2);
			}
		}

	}

	public void addKeysCombination(ArrayList<String> k,
			final boolean addToArray, boolean refresh) {

		if (!Settings.logShiftPlusSingleLetter) {
			if (k.size() == 2
					&& (k.contains("SHIFT") || k.contains("shift"))
					&& ((k.get(0).length() == 1 && k.get(0).matches("[a-zA-Z]")) || (k
							.get(1).length() == 1 && k.get(1).matches(
							"[a-zA-Z]")))) {
				return;
			}
		}
		final ArrayList<String> keys = new ArrayList<>(k);
		if (!refresh) {
			synchronized (lastCombination) {
				boolean theSame = false;
				if (lastCombination.size() == keys.size()) {
					int size = keys.size();
					for (String c : keys) {
						if (lastCombination.contains(c)) {
							size--;
						}
					}
					if (size == 0) {
						theSame = true;
					} else {
						lastCombination.clear();
						lastCombination.addAll(keys);
					}
				} else {
					lastCombination.clear();
					lastCombination.addAll(keys);
				}
				if (theSame) {

					/*
					 * synchronized (refreshLock) { if (refreshing) { return; }
					 * else { refreshing = true; } }
					 */

					synchronized (rows) {
						if (rows.size() > 0) {
							LoggerKeyCombination lkc = rows
									.get(rows.size() - 1);
							lkc.changeState(1);
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									repaint();
								}
							});
						}

					}

					new Thread(new Runnable() {

						@Override
						public void run() {
							synchronized (Thread.currentThread()) {
								try {
									Thread.currentThread().wait(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								SwingUtilities.invokeLater(new Runnable() {

									@Override
									public void run() {
										Logger.getInstance().refresh();
									}
								});
								/*
								 * synchronized (refreshLock) { refreshing =
								 * false; }
								 */
							}
						}
					}).start();
					return;
				}
			}
		}

		if (addToArray) {
			synchronized (displayedKeys) {
				displayedKeys.add(keys);
			}
		}
		String description = "";

		synchronized (KeyCombinationsManager.keyCombinationMap) {
			for (String combinatin : KeyCombinationsManager.keyCombinationMap
					.keySet()) {
				Object[] obj = KeyCombinationsManager.keyCombinationMap
						.get(combinatin);
				@SuppressWarnings("unchecked")
				ArrayList<String> registeredKeys = (ArrayList<String>) obj[0];
				if (!(boolean) obj[1]) {
					continue;
				}

				if (registeredKeys == null
						|| (registeredKeys != null && registeredKeys.size() == 0)) {
					System.out.println("got a 0 size keys array: "
							+ (registeredKeys == null) + "for description: "
							+ combinatin);
					continue;
				}

				if (registeredKeys.size() == keys.size()) {
					int all = keys.size();
					for (String key : keys) {
						if (registeredKeys.contains(key)) {
							all--;
						}
					}

					if (all == 0) {
						description = combinatin;
					}

				}
			}
		}

		int[] dim = Settings.getKeyCombinationSize(keys, description);
		width = dim[0];
		height = dim[1];
		int textWidth = dim[2];

		System.out.println("############### width before calculation: " + width
				+ " text width: " + textWidth);

		if (Settings.horizontalLogger) {
			width = Math.max(width, textWidth) + Settings.defaultKeyWidth;
		} else {
			width += textWidth + Settings.defaultKeyWidth;
		}

		System.out.println("############### width after calculation: " + width
				+ " text width: " + textWidth);

		LoggerKeyCombination lkc = new LoggerKeyCombination(keys, width,
				height, description, textWidth, dim[0], 0);

		synchronized (rows) {
			rows.add(lkc);
			allHeight = (rows.size() * 50) + 20;
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				KeyCombinationsManager kcm = KeyCombinationsManager
						.getInstance(false);
				if (kcm != null && addToArray) {
					if (kcm.isVisible()) {
						kcm.showKeyCombination(keys);
					}
				}
				repaint();
			}
		});

	}

	private void drawVertical(Graphics2D g2) {
		int offsetY = ((rows.size() - 1) * 50);
		allHeight = offsetY + 50;
		allWidth = 0;
		synchronized (rows) {
			for (LoggerKeyCombination lkc : rows) {
				g2.drawImage(lkc, 0, offsetY, null);
				if (lkc.getWidth() > allWidth) {
					allWidth = lkc.getWidth();
				}
				offsetY -= 50;
			}
		}

		allHeight = Math.max(allHeight + 50, 50);
		allWidth = Math.max(allWidth, 100);
		setPreferredSize(new Dimension(allWidth, allHeight));
		g2.dispose();
		int parentHeight = SwingUtilities.getWindowAncestor(this).getHeight();
		if (parentHeight < allHeight) {
			getParent().revalidate();
		}
	}

	private void drawHorizontal(Graphics2D g2) {
		int allWidth = 0;
		int offsetLeft = 0;
		synchronized (rows) {
			for (BufferedImage img : rows) {
				allWidth += img.getWidth();
			}
		}

		offsetLeft = allWidth;

		synchronized (rows) {
			for (BufferedImage img : rows) {
				offsetLeft -= img.getWidth();
				g2.drawImage(img, offsetLeft, 0, null);
			}
		}

		g2.dispose();

		setPreferredSize(new Dimension(allWidth, 60));

		if (allWidth > getParent().getWidth()) {
			getParent().revalidate();
		}

	}

	public void refresh() {
		synchronized (rows) {
			rows.clear();
			synchronized (displayedKeys) {
				for (ArrayList<String> keys : displayedKeys) {
					addKeysCombination(keys, false, true);
				}
			}
		}
	}

	public void removeSingleKeys() {
		synchronized (displayedKeys) {
			Iterator<ArrayList<String>> it = displayedKeys.iterator();
			while (it.hasNext()) {
				if (it.next().size() == 1) {
					it.remove();
				}
			}
		}
	}

}
