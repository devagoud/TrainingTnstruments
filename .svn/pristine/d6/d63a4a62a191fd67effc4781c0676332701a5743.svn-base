package ro.titus.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ro.titus.tools.Refresher;
import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class Keyboard extends JPanel {

	// 271X635

	static Keyboard keyboard;

	int width = 500;
	int height = 180;

	static HashMap<String, BasicKey> buttons = new HashMap<>();

	static String[] row0 = new String[] { "ESC", "F1", "F2", "F3", "F4", "F5",
			"F6", "F7", "F8", "F9", "F10", "F11", "F12", "Prt Scm Sys Req",
			"Scroll Lock", "Pause Break" };
	static String[] row1 = new String[] { "`~", "1!", "2@", "3#", "4$", "5%",
			"6^", "7&", "8*", "9(", "0)", "-_", "=+", "backspace", "Insert",
			"Home", "Page Up", "Num Lock", "\\", "*", "-" };
	static String[] row2 = new String[] { "Tab", "Q", "W", "E", "R", "T", "Y",
			"U", "I", "O", "P", "[{", "]}", "\\|", "Delete", "End",
			"Page Down", "7 Home", "8 ^", "9 Pg Up", "+" };
	static String[] row3 = new String[] { "CapsLock", "A", "S", "D", "F", "G",
			"H", "J", "K", "L", ";:", "'\"", "Enter", "4 <", "5 5 5", "6 >" };
	static String[] row4 = new String[] { "Shift", "Z", "X", "C", "V", "B",
			"N", "M", ",<", ".>", "/?", "Shift2", "^", "1 End", "2 V",
			"3 Pg Dn", "Enter2" };
	static String[] row5 = new String[] { "CTRL", "super", "ALT", "space",
			"ALT2", "super2", "meta", "CTRL2", "<", "V1", ">", "0 Insert",
			". Del" };

	static Object[] rows = new Object[] { row0, row1, row2, row3, row4, row5 };

	public static Keyboard getInstance() {
		if (buttons.size() == 0) {
			for (int i = 0; i < rows.length; i++) {
				String[] row = (String[]) rows[i];
				for (int j = 0; j < row.length; j++) {
					synchronized (buttons) {
						String key = row[j];
						if (key.equals("super2")) {
							key = "super";
						}

						if (i == 5) {
							key = key.replace("2", "");
							if (key.equals("space")) {
								key = "   ";
							}
						}

						buttons.put(row[j].toLowerCase(), new BasicKey(1, 1,
								key, 0, 0, 0, 0));
					}
				}
			}
		}

		if (keyboard == null) {
			keyboard = new Keyboard();

		}
		return keyboard;
	}

	public Keyboard() {
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!Settings.macSystem) {
			Dimension size = getSize();
			/*
			 * size.width = Math.max(width, 20); size.height = Math.max(height,
			 * 20);
			 */

			width = (int) size.getWidth();
			height = (int) size.getHeight();

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// Rectangle dim = g2.getClipBounds().getBounds();
			// if (dim.width == width && dim.height == height) {

			/*
			 * if (Settings.keepAspectRatio) { int tempW = (int) (((float)
			 * ((float) height * 100f)) / 28.15f); int tempH = (int) (((float)
			 * ((float) width / 100f)) * 28.15f);
			 * 
			 * System.out.println("main: " + width + "x" + height +
			 * " version 1: " + (tempW < width) + " " + tempW + "x" + height +
			 * " version 2: " + (tempH < height) + " " + width + "x" + tempH);
			 * 
			 * 
			 * if (tempW < width) { width = tempW; } else { height = tempH; }
			 * 
			 * }
			 */
			if (Settings.keepAspectRatio) {
				Dimension d = Settings.getDimensions(width, height, 962, 271);
				width = (int) d.getWidth();
				height = (int) d.getHeight();
			}

			int defaultComponentWidth = (int) ((width - 20 - 22) / (23));
			int defaultComponentHeight = (int) (((height - 30) / 6));

			constructImages(defaultComponentWidth, defaultComponentHeight, g2);
			setPreferredSize(new Dimension(width, height));
			g2.dispose();
		}

	}

	public void changeKeyState(String name, int state) {
		// System.out.println("button name: " + name);
		synchronized (buttons) {

			if (buttons.containsKey(name)) {
				BasicKey k = buttons.get(name);
				k.changeState(state);
				Graphics2D g2 = null;
				if (Settings.macSystem) {
					if (Settings.macKeyboardBig) {
						g2 = (Graphics2D) KeyboardMacBig.getInstance()
								.getGraphics();
					} else {
						g2 = (Graphics2D) KeyboardMacSmall.getInstance()
								.getGraphics();
					}
				}else{
					g2 = (Graphics2D) getGraphics();
				}


				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g2.clipRect(k.getXX(), k.getYY(), k.w, k.h);
				g2.drawImage(k, k.getXX(), k.getYY(), null);
				g2.dispose();
				if (state != 0) {
					synchronized (Refresher.pressed) {
						Refresher.pressed.put(name, System.currentTimeMillis());
					}
				}
			} else {
				System.out.println("the buttons map doesn't contain the key: "
						+ name);
			}

		}
	}

	public void changeKeyState(int key, final int state, boolean left) {

/*		System.out
				.println("key " + key + " state: " + state + " left? " + left);
*/
		synchronized (buttons) {
			String name = null;
			synchronized (Settings.codeToKey) {
				name = Settings.codeToKey.get(key).toLowerCase();
			}

			if (name != null) {

				/*
				 * if (Settings.macSystem) {
				 * 
				 * if (name.contains("ctrl")) { name = "control"; } else if
				 * (name.contains("alt")) { name = "option"; } else if
				 * (name.contains("super")) { name = "command"; }else
				 * if(name.equals("<")){ name = "left"; }else
				 * if(name.equals(">")){ name = "right"; }else
				 * if(name.equals("^")){ name = "up"; }else
				 * if(name.equals("v1")){ name = "down"; }else
				 * if(name.equals("backspace")){ name = "delete"; }
				 * 
				 * if (!left && (name.startsWith("control") ||
				 * name.startsWith("option") || name .startsWith("command"))) {
				 * name += "2"; } } else {
				 */
				if (!left
						&& (name.startsWith("ctrl") || name.startsWith("alt")
								|| name.startsWith("shift")
								|| name.startsWith("super")
								|| name.startsWith("option")
								|| name.startsWith("command") || name
									.startsWith("control"))) {
					name += "2";
				}
				// }
				final String nameFinal = name;
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						changeKeyState(nameFinal, state);
					}
				});

			}
		}
	}

	private int getComponentWidth(String componentLable, int defaultWidth) {
		if (componentLable.contains("ALT") || componentLable.contains("CTRL")
				|| componentLable.contains("super")
				|| componentLable.contains("meta")) {
			defaultWidth = (int) ((float) (((float) defaultWidth) * 9f + 3f) / 7f);
		} else if (componentLable.equals("   ")
				|| componentLable.equals("space")) {
			defaultWidth = ((defaultWidth) * 6) + 6;
		} else if (componentLable.equals("CapsLock")
				|| componentLable.equals("backspace")
				|| componentLable.equals("Enter")) {
			defaultWidth = ((defaultWidth) * 2) + 1;
		} else if (componentLable.contains("Shift")) {
			defaultWidth = (int) ((float) (defaultWidth) * 2.5f) + 1;
			if (componentLable.equals("Shift2")) {
				defaultWidth += 1;
			}
		} else if (componentLable.contains("Tab")) {
			defaultWidth = (int) (((float) defaultWidth) * 1.5f) + 1;
		} else if (componentLable.equals("\\|")) {
			defaultWidth = (int) (((float) defaultWidth) * 1.5f);
		} else if (componentLable.equals("0 Insert")) {
			defaultWidth = (defaultWidth * 2) + 1;
		}
		return defaultWidth;
	}

	private void constructImages(int defaultComponentWidth,
			int defaultComponentHeight, Graphics2D g2) {

		int xPosition = 0;
		int yPosition = 0;
		int fontSize = 0;

		int componentWidth = defaultComponentHeight;
		int componentHeight = defaultComponentHeight;
		int space = 1;

		for (int i = 0; i < rows.length; i++) {
			if (i == 0) {
				xPosition = 10;
				yPosition = 10;
			} else if (i == 1) {
				xPosition = 10;
				yPosition = defaultComponentHeight + 15 + 1;
			} else if (i == 2) {
				xPosition = 10;
				yPosition = (defaultComponentHeight * 2) + 15 + 2;
			} else if (i == 3) {
				xPosition = 10;
				yPosition = (defaultComponentHeight * 3) + 15 + 3;
			} else if (i == 4) {
				xPosition = 10;
				yPosition = (defaultComponentHeight * 4) + 15 + 4;
			} else if (i == 5) {
				xPosition = 10;
				yPosition = (defaultComponentHeight * 5) + 15 + 5;
			}
			String[] row = (String[]) rows[i];

			for (int j = 0; j < row.length; j++) {

				if (i == 0 && (j == 0 || j == 4 || j == 8)) {

					space = ((defaultComponentWidth + (space * 3)) * 2) / 3;

				} else if ((i == 0 && j == 13) || (i == 1 && j == 14)
						|| (i == 5 && j == 8) || (i == 2 && j == 14)) {
					xPosition = (defaultComponentWidth * 15) + 14 + 10
							+ (defaultComponentWidth / 2);
				} else if ((i == 4 && j == 12)) {
					xPosition = (defaultComponentWidth * 16) + 15 + 10
							+ (defaultComponentWidth / 2);
				} else if ((i == 1 && j == 17) || (i == 2 && j == 17)
						|| (i == 3 && j == 13) || (i == 4 && j == 13)
						|| (i == 5 && j == 11)) {
					xPosition = (defaultComponentWidth * 19) + 16 + 10;
				} else if ((i == 2 && j == 20) || (i == 4 && j == 16)) {
					componentHeight = (componentHeight * 2) + 1;
				} else {
					space = 1;
				}

				String key = row[j];

				if (key.equals("V1")) {
					key = "V";
				}
				if (key.equals("super2")) {
					key = "super";
				}

				if (i == 5) {
					key = key.replace("2", "");
					if (key.equals("space")) {
						key = "   ";
					}
				}

				if (key.length() == 1 || key.equals("V1")) {
					fontSize = defaultComponentHeight / 3;
				} else if (key.length() == 2 || key.matches("[A-Z]\\d+")
						|| key.split(" ").length >= 2 || key.equals("Insert")
						|| key.equals("Home") || key.equals("Delete")
						|| key.equals("End") || key.equals("backspace")
						|| key.equals("Enter1")) {
					fontSize = defaultComponentHeight / 4;
				} else {
					fontSize = defaultComponentHeight / 3;
				}
				if (key.contains("caps")) {
					fontSize -= fontSize / 6;
				}

				componentWidth = getComponentWidth(key, defaultComponentWidth);

				synchronized (buttons) {
					if (buttons.containsKey(row[j].toLowerCase())) {
						BasicKey k = buttons.get(row[j].toLowerCase());
						k = new BasicKey(componentWidth, componentHeight,
								k.getLable(), xPosition, yPosition,
								k.getStatus(), fontSize);
						if (k != null) {
							g2.drawImage(k, k.getXX(), k.getYY(), null);
						}
						buttons.put(row[j].toLowerCase(), k);
					}

					xPosition += componentWidth + space;
					componentHeight = defaultComponentHeight;
				}

			}
		}

	}

}
