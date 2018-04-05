package ro.titus.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import ro.titus.tools.Settings;

public class LoggerKeyCombination extends BufferedImage {

	ArrayList<String> keys;
	int allWidth;
	int height;
	int descriptionWidth;
	int keysWidth;
	String description;
	int state;

	public LoggerKeyCombination(ArrayList<String> keys, int allWidth,
			int height, String description, int descriptionWidth,
			int keysWidth, int state) {

		super(allWidth, height, BufferedImage.TYPE_INT_ARGB);
		this.keys = keys;
		this.allWidth = allWidth;
		this.descriptionWidth = descriptionWidth;
		this.keysWidth = keysWidth;
		this.description = description;
		this.state = state;
		drawImage();
	}

	public void changeState(int state) {
		this.state = state;
		drawImage();
	}

	private void drawImage() {
		int width = Settings.defaultKeyWidth;
		height = Settings.defaultKeyWidth;

		System.out.println("at the logger key combination");

		if (keys.size() == 0) {
			System.err
					.println("LoggerKeyCombination.LoggerKeyCombination() the keys arraylist size is 0");
			return;
		}
		int offsetLeft = 0;
		if (Settings.horizontalLogger) {
			System.out
					.println("!!!!!!!!!!!!! the logger orientation is horizontal");
			System.out.println("description width: " + descriptionWidth
					+ " all width: " + allWidth);
			offsetLeft = (descriptionWidth >= keysWidth) ? ((descriptionWidth - keysWidth) / 2)
					: 0;
		}

		System.out.println("#@#@#@##@ offset left: " + offsetLeft);

		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setPaint(Color.BLACK);
		Font font = new Font(null, Font.BOLD,
				(int) (Settings.keyboardButtonsFontSize * 0.7));
		g2.setFont(font);
		for (int i = 0; i < keys.size(); i++) {
			width = Settings.detButtonsSize(keys.get(i));
			System.out.println("key: " + keys.get(i));
			BufferedImage key = null;

			if (keys.get(i).equals("leftMouseButton")) {
				try {
					key = new MouseButtonProto(13 + state, 0, 0, 40, 40)
							.getImage();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (keys.get(i).equals("rightMouseButton")) {
				try {
					key = new MouseButtonProto(15 + state, 0, 0, 40, 40)
							.getImage();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (keys.get(i).equals("middleMouseButton")) {
				try {
					key = new MouseButtonProto(17 + state, 0, 0, 40, 40)
							.getImage();
					System.out.println("############################## createing middle button for the logger");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

				key = new BasicKey(width, height, keys.get(i), offsetLeft, 0,
						state, Settings.fontSize);
			}
			if (key != null) {
				g2.drawImage(key, offsetLeft, 0, null);
			}
			if (i < keys.size() - 1) {
				g2.drawString("+", offsetLeft += width + 4,
						Settings.defaultKeyWidth / 2);
			} else if (description.length() > 0) {
				if (!Settings.horizontalLogger) {
					g2.drawString("=", offsetLeft += width + 4,
							Settings.defaultKeyWidth / 2);
				}
			}
			offsetLeft += 8;
		}

		if (!Settings.horizontalLogger) {
			g2.drawString(description, offsetLeft, Settings.defaultKeyWidth / 2);
		} else {
			g2.drawString(
					description,
					(descriptionWidth >= keysWidth) ? (0) : ((allWidth
							- descriptionWidth - Settings.defaultKeyWidth) / 2),
					height + ((Settings.defaultKeyWidth / 4)));
		}

		g2.dispose();
	}

}
