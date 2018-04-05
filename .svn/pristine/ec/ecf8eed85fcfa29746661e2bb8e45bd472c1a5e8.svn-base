package ro.titus.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ro.titus.tools.Settings;

public class MouseButtonProto {

	private static File leftImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "left_normal.png");
	private static File leftPImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "left_pressed.png");
	private static File leftRImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "left_release.png");
	private static File rightImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "right_normal.png");
	private static File rightPImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "right_pressed.png");
	private static File rightRImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "right_release.png");
	private static File middleImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "middle_normal.png");
	private static File middlePImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "middle_pressed.png");
	private static File middleRImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "middle_release.png");

	private static File upPImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "middle_up_pressed.png");
	private static File upRImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "middle_up_release.png");

	private static File downPImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "middle_down_pressed.png");
	private static File downRImageFile = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "middle_down_release.png");

	private static File leftSimple = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "left_button.png");

	private static File rightSimple = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "right_button.png");

	private static File leftSimplePressed = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "left_button_pressed.png");

	private static File rightSimplePressed = new File("Images" + File.separator
			+ "Mouse" + File.separator + "White" + File.separator
			+ "right_button_pressed.png");

	private static File[] imagesFiles = new File[] { leftImageFile,
			leftPImageFile, leftRImageFile, rightImageFile, rightPImageFile,
			rightRImageFile, middleImageFile, middlePImageFile,
			middleRImageFile, upPImageFile, upRImageFile, downPImageFile,
			downRImageFile, leftSimple, leftSimplePressed, rightSimple,
			rightSimplePressed };

	int w;
	int h;
	int x;
	int y;
	BufferedImage image;
	BufferedImage fromFile;
	int state = 0;
	int buttonNumber;

	public MouseButtonProto(int buttonNumber, int x, int y, int width,
			int height) throws IOException {
		this.x = x;
		this.y = y;
		w = width;
		h = height;
		this.buttonNumber = buttonNumber;
		redraw(true);
	}

	public void redraw(boolean stateChanged) throws IOException {

		if (stateChanged) {
			try {
				synchronized (imagesFiles) {
					if (buttonNumber == 17) {
						fromFile = ImageIO.read(imagesFiles[6]);
					} else if (buttonNumber == 18) {
						fromFile = ImageIO.read(imagesFiles[7]);
					} else {
						fromFile = ImageIO.read(imagesFiles[buttonNumber]);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("file name: "
						+ imagesFiles[buttonNumber].getAbsolutePath());
			}
		} else {

			setDimensions();
		}
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(fromFile, 0, 0, w, h, null);
		g.dispose();

	}

	private void setDimensions() {

		/*
		 * if (Settings.keepAspectRatio) { int tempW = (int) (((float) ((float)
		 * h / 100f)) * 59.45f); int tempH = (int) (((float) ((float) w * 100f))
		 * / 59.45f);
		 * 
		 * if (tempW < w) { w = tempW; } else { h = tempH; } }
		 */

		// botton height - 48.46%
		// left width - 50.66% height - 51.53%
		// right width - 50.92% height - 51.53%
		// middle width - 19% height - 19.39%

		if (Settings.keepAspectRatio) {
			if (Settings.keepAspectRatio) {
				Dimension d = Settings.getDimensions(w, h, 379, 619);
				w = (int) d.getWidth();
				h = (int) d.getHeight();
			}
		}
		int totalWidth = w;
		int totalHeight = h;

		switch (buttonNumber) {
		case 0:
		case 1:
		case 2:
			w = (int) (((float) ((float) w / 100f)) * 50.66f);
			h = (int) (((float) ((float) h / 100f)) * 51.53f);
			x = 0;
			y = 0;
			break;
		case 3:
		case 4:
		case 5:
			w = (int) (((float) ((float) w / 100f)) * 50.92f);
			h = (int) (((float) ((float) h / 100f)) * 51.53f);
			x = (totalWidth - w);
			y = 0;
			break;
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
			w = (int) (((float) ((float) w / 100f)) * 19f);
			h = (int) (((float) ((float) h / 100f)) * 19.39f);
			x = (int) (((float) ((float) totalWidth / 100f)) * 41.80f);
			y = (int) (((float) ((float) totalHeight / 100f)) * 8.64f);
			break;
		case 13:
		case 14:
			w = (int) (((float) ((float) w / 100f)) * 50.66f);
			break;
		case 15:
		case 16:
			w = (int) (((float) ((float) w / 100f)) * 50.66f);
			break;
		case 17:
		case 18:
			w = (int) (((float) ((float) w / 100f)) * 19f);
			break;
		default:
			break;
		}

	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getState() {
		return state;
	}

	public void setDimension(int width, int height) {
		w = width;
		h = height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setState(String buttonName, String state) throws IOException {

		// System.out.println("name: " + buttonName + " state: " + state);

		if (buttonName.equals("left")) {
			if (state.equals("normal")) {
				buttonNumber = 0;
			} else if (state.equals("pressed")) {
				buttonNumber = 1;
			} else if (state.equals("released")) {
				buttonNumber = 2;
			}
		} else if (buttonName.equals("right")) {
			if (state.equals("normal")) {
				buttonNumber = 3;
			} else if (state.equals("pressed")) {
				buttonNumber = 4;
			} else if (state.equals("released")) {
				buttonNumber = 5;
			}
		} else if (buttonName.equals("middle")) {
			if (state.equals("normal")) {
				buttonNumber = 6;
			} else if (state.equals("pressed")) {
				buttonNumber = 7;
			} else if (state.equals("released")) {
				buttonNumber = 8;
			} else if (state.equals("up")) {
				buttonNumber = 9;
			} else if (state.equals("up_released")) {
				buttonNumber = 10;
			} else if (state.equals("down")) {
				buttonNumber = 11;
			} else if (state.equals("down_released")) {
				buttonNumber = 12;
			}
		}
		redraw(true);
	}

	public BufferedImage getImage() {
		return image;
	}

	public static void changeTheme(boolean black) {
		if (black) {
			leftImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "left_normal.png");
			leftPImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "left_pressed.png");
			leftRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "left_release.png");
			rightImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "right_normal.png");
			rightPImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "right_pressed.png");
			rightRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "right_release.png");
			middleImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "middle_normal.png");
			middlePImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "middle_pressed.png");
			middleRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "middle_release.png");

			upPImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "middle_up_pressed.png");
			upRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "middle_up_release.png");

			downPImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "middle_down_pressed.png");
			downRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "middle_down_release.png");

			leftSimple = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "left_button.png");

			rightSimple = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "right_button.png");

			leftSimplePressed = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "left_button_pressed.png");

			rightSimplePressed = new File("Images" + File.separator + "Mouse"
					+ File.separator + "Black" + File.separator
					+ "right_button_pressed.png");

		} else {
			leftImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "left_normal.png");
			leftPImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "left_pressed.png");
			leftRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "left_release.png");
			rightImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "right_normal.png");
			rightPImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "right_pressed.png");
			rightRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "right_release.png");
			middleImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "middle_normal.png");
			middlePImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "middle_pressed.png");
			middleRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "middle_release.png");

			upPImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "middle_up_pressed.png");
			upRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "middle_up_release.png");

			downPImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "middle_down_pressed.png");
			downRImageFile = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "middle_down_release.png");

			leftSimple = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "left_button.png");

			rightSimple = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "right_button.png");

			leftSimplePressed = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "left_button_pressed.png");

			rightSimplePressed = new File("Images" + File.separator + "Mouse"
					+ File.separator + "White" + File.separator
					+ "right_button_pressed.png");

		}
		synchronized (imagesFiles) {
			imagesFiles = new File[] { leftImageFile, leftPImageFile,
					leftRImageFile, rightImageFile, rightPImageFile,
					rightRImageFile, middleImageFile, middlePImageFile,
					middleRImageFile, upPImageFile, upRImageFile,
					downPImageFile, downRImageFile, leftSimple,
					leftSimplePressed, rightSimple, rightSimplePressed };
		}
	}

}
