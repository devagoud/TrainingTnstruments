package ro.titus.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class KeyboardMacSmall extends Keyboard {

	static KeyboardMacSmall keyboardMacSmall;

	static final String[] labelsRow1 = new String[] { "esc", "F1", "F2", "F3",
			"F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12", "eject" };
	static final String[] labelsRow2 = new String[] { "`~", "1!", "2@", "3#",
			"4$", "5%", "6^", "7&", "8*", "9(", "0)", "-_", "=+", "delete" };
	static final String[] labelsRow3 = new String[] { "tab", "Q", "W", "E",
			"R", "T", "Y", "U", "I", "O", "P", "[{", "]}", "\\|" };
	static final String[] labelsRow4 = new String[] { "caps lock", "A", "S",
			"D", "F", "G", "H", "J", "K", "L", ";:", "'\"", "return" };
	static final String[] labelsRow5 = new String[] { "shift", "Z", "X", "C",
			"V", "B", "N", "M", ",<", ".>", "/?", "shift2" };
	static final String[] labelsRow6 = new String[] { "fn", "control",
			"option", "command", "space", "command2", "option2", "left", "up",
			"down", "right" };

	static Object[] labels = new Object[] { labelsRow1, labelsRow2, labelsRow3,
			labelsRow4, labelsRow5, labelsRow6 };

	int width;
	int height;
	int space = 3;

	public static KeyboardMacSmall getInstance() {


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
		if (keyboardMacSmall == null) {
			keyboardMacSmall = new KeyboardMacSmall();

		}
		return keyboardMacSmall;
	}

	private KeyboardMacSmall() {

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("############## paintcomponent at small keyboard");
		if (!Settings.macKeyboardBig) {
			width = getWidth();
			height = getHeight();

			if (Settings.keepAspectRatio) {
				Dimension d = Settings.getDimensions(width, height, 777, 334);
				System.out.println(d);
				width = (int) d.getWidth();
				height = (int) d.getHeight();
				System.out.println(width + "x" + height);
			}

			int basicButtonWidth = (int) (((width - 10) - (space * 13)) / 14.5);
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

				if (row[j].equals("tab")) {
					width = (int) (basicWidth * 1.5);
				} else if (row[j].equals("caps lock")) {
					width = (int) ((defRowWidth - (basicWidth * 11) - (space * 12)) / 2);
				} else if (row[j].contains("shift")) {
					width = (int) ((defRowWidth - ((basicWidth * 10) + (space * 11))) / 2);
				} else if (row[j].contains("command")) {
					width = (int) ((defRowWidth - ((basicWidth * 12) + (space * 13))) / 2);
				} else if (row[j].equals("space")) {
					width = (int) (basicWidth * 5) + (space * 4);
				} else if (row[j].equals("delete")) {
					width = (int) (basicWidth * 1.5);
				} else if (row[j].equals("return")) {
					width = (int) ((defRowWidth - (basicWidth * 11) - (space * 12)) / 2);
				} else {
					if (i == 0) {
						width = (int) ((defRowWidth - (space * 13)) / 14);
					} else {
						width = basicWidth;
					}
				}

				if (row[j].equals("left")) {
					height = height / 2;
				}

				if (row[j].equals("left")) {
					y += height;
				} else if (row[j].equals("up")) {
					y -= height;
				} else if (row[j].equals("down")) {
					x -= width + space;
					y += height;
				}

				if (row[j].equals("eject") || row[j].equals("F1")) {
					x += space * 2;
				}

				synchronized (buttons) {
					BasicKey k = buttons.get(row[j].toLowerCase());
					k = new BasicKey(width, height, row[j], x, y,
							k.getStatus(), fontSize);
					buttons.put(row[j].toLowerCase(), k);
				}

				x += width + space;

			}
			y += height + space;
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
