package ro.titus.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class KeyboardMacBig extends Keyboard {

	static KeyboardMacBig keyboardMacBig;

	static final String[] labelsRow1 = new String[] { "esc", "F1", "F2", "F3",
			"F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12", "eject",
			"F13", "F14", "F15", "F16", "F17", "F18", "F19" };
	static final String[] labelsRow2 = new String[] { "`~", "1!", "2@", "3#",
			"4$", "5%", "6^", "7&", "8*", "9(", "0)", "-_", "=+", "delete",
			"fn", "home", "page up", "clear", "=", "/", "*" };
	static final String[] labelsRow3 = new String[] { "tab", "Q", "W", "E",
			"R", "T", "Y", "U", "I", "O", "P", "[{", "]}", "\\|", "delete2",
			"end", "page down", "7", "8", "9", "-" };
	static final String[] labelsRow4 = new String[] { "caps lock", "A", "S",
			"D", "F", "G", "H", "J", "K", "L", ";:", "'\"", "return", "4", "5",
			"6", "+" };
	static final String[] labelsRow5 = new String[] { "shift", "Z", "X", "C",
			"V", "B", "N", "M", ",<", ".>", "/?", "shift2", "up", "1", "2",
			"3", "enter" };
	static final String[] labelsRow6 = new String[] { "control", "option",
			"command", "space", "command2", "option2", "control2", "left",
			"down", "right", "0", "." };

	static Object[] labels = new Object[] { labelsRow1, labelsRow2, labelsRow3,
			labelsRow4, labelsRow5, labelsRow6 };

	int width = 800;
	int height = 235; // 29,36
	int space = 3;

	public static KeyboardMacBig getInstance() {
		if (buttons.size() == 0) {
			synchronized (buttons) {
				for (int i = 0; i < labels.length; i++) {
					String[] row = (String[]) labels[i];
					for (int j = 0; j < row.length; j++) {
						buttons.put(row[j].toLowerCase(), new BasicKey(1, 1,
								row[j], 0, 0, 0, 1));
					}
				}
			}
		}
		if (keyboardMacBig == null) {
			keyboardMacBig = new KeyboardMacBig();
		}
		return keyboardMacBig;
	}

	private KeyboardMacBig() {

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		//System.out.println("############## paintcomponent at big keyboard");

		if (Settings.macKeyboardBig) {
			width = getWidth();
			height = getHeight();

			if (Settings.keepAspectRatio) {
				Dimension d = Settings.getDimensions(width, height, 800, 235);
				System.out.println(d);
				width = (int) d.getWidth();
				height = (int) d.getHeight();
				System.out.println(width + "x" + height);
			}

			int basicButtonWidth = (int) (((width - 10) - (space * 19)) / 22.5);
			int basicButtonHeight = (int) ((double) ((height - 10) - (space * 5)) / 6d);

			/*System.out.println("bbw: " + basicButtonWidth + " bbh: "
					+ basicButtonHeight + " width: " + width + " width / 15: "
					+ (width / 15));*/

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			drawKeyboard(g2, basicButtonWidth, basicButtonHeight);
			g2.dispose();
		}
	}

	private void drawKeyboard(Graphics2D g2, int basicWidth, int basicHeight) {
		int width = basicWidth;
		int height = basicHeight;
		int x = 5;
		int y = 5;

		int fontSize = basicWidth / 5;

		int defRowWidth = (int) ((basicWidth * 13) + (basicWidth * 1.5) + (space * 13));

		for (int i = 0; i < labels.length; i++) {
			if (i == 0) {
				width = ((defRowWidth / 14) - (space * 13));
			} else {
				width = basicWidth;
			}

			if (i == 5) {
				height = basicHeight + (space * 2);
			}

			String[] row = (String[]) labels[i];
			for (int j = 0; j < row.length; j++) {

				// 6 but 2 small and 4 same space available 9 but and 3 spaces

				if (row[j].equals("tab")) {
					width = (int) (basicWidth * 1.5);
				} else if (row[j].equals("caps lock")) {
					width = (int) ((defRowWidth - (basicWidth * 11) - (space * 12)) / 2);
				} else if (row[j].contains("shift")) {
					width = (int) ((defRowWidth - ((basicWidth * 10) + (space * 11))) / 2);
				} else if (row[j].contains("command")
						|| row[j].contains("control")) {
					width = (int) (((basicWidth * 6) + (space * 3)) / 4);
				} else if (row[j].equals("space")) {
					width = (int) (basicWidth * 6) + (space * 5);
				} else if (row[j].equals("delete")) {
					width = (int) (basicWidth * 1.5);
				} else if (row[j].equals("return")) {
					width = (int) ((defRowWidth - (basicWidth * 11) - (space * 12)) / 2);
				} else if (row[j].equals("0")) {
					width = (width * 2) + space;
				} else if (row[j].contains("option")) {
					width = (int) (basicWidth * 1.25);
				} else {
					if (i == 0 && j <= 14) {
						width = (int) Math
								.floor((((basicWidth * 14.5d) + space) / 14d));
					} else {
						width = basicWidth;
					}
				}

				/*
				 * if (row[j].equals("left")) { height = height / 2; }
				 */

				/*
				 * if (row[j].equals("left")) { y += height; } else if
				 * (row[j].equals("up")) { y -= height; } else if
				 * (row[j].equals("down")) { x -= width + space; y += height; }
				 */

				if (row[j].equals("enter")) {
					height = (basicHeight * 2) + space;
				} else {
					height = basicHeight;
				}

				if (row[j].equals("left") || row[j].equals("fn")
						|| row[j].equals("delete2") || row[j].equals("clear")
						|| row[j].equals("F16") || row[j].equals("0")
						|| row[j].equals("7") || row[j].equals("F13")) {
					x += (basicHeight / 2) + 1;
				}

				if (row[j].equals("4")) {
					x += (basicWidth * 4) + (space * 5);
				}

				if (row[j].equals("up") || row[j].equals("1")) {
					x += (basicHeight * 1.5) + 1;
				}

				synchronized (buttons) {
					BasicKey k = buttons.get(row[j].toLowerCase());
					k = new BasicKey(width, height, row[j], x, y,
							k.getStatus(), fontSize);
					buttons.put(row[j].toLowerCase(), k);
				}

				x += width + space;

			}
			if (height == (basicHeight * 2) + space) {
				y += basicHeight + space;
			} else {
				y += height + space;
			}
			System.out.println("x: " + x + " y: " + y);
			x = 5;
		}

		synchronized (buttons) {
			for (BasicKey key : buttons.values()) {
				g2.drawImage(key, key.getXX(), key.getYY(), null);
			}
		}
	}

}
