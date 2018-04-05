package ro.titus.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ro.titus.tools.Refresher;
import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class Mouse extends JPanel {

	static File lowPartImageFile = new File("Images" + File.separator + "Mouse"
			+ File.separator + "White" + File.separator + "mouse.png");
	static BufferedImage lowPart;
	static MouseButtonProto left, right, middle;

	boolean upWellEvent = false;
	static Mouse singleTon;
	int width = 80;
	int height = 155;

	public static Mouse getInstance() {
		if (singleTon == null) {
			singleTon = new Mouse();
		}
		return singleTon;
	}

	private Mouse() {
		try {
			System.out.println("Mouse.Mouse()");
			lowPart = ImageIO.read(lowPartImageFile);
			left = new MouseButtonProto(0, 0, 0, 176, 296);
			right = new MouseButtonProto(3, 86, 0, 176, 296);
			middle = new MouseButtonProto(6, 75, 28, 176, 296);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Dimension size = getSize();
		/*
		 * size.width = Math.max(width, 20); size.height = Math.max(height, 20);
		 */

		width = (int) size.getWidth();
		height = (int) size.getHeight();

		/*
		 * if (Settings.keepAspectRatio) { int tempW = (int) (((float) ((float)
		 * height / 100f)) * 59.45f); int tempH = (int) (((float) ((float) width
		 * * 100f)) / 59.45f);
		 * 
		 * if (tempH < height) { width = tempW; } else { height = tempH; } }
		 */
		if (Settings.keepAspectRatio) {
			Dimension d = Settings.getDimensions(width, height, 379, 619);
			width = (int) d.getWidth();
			height = (int) d.getHeight();
		}
		// Rectangle2D r = g2.getClipBounds();
		// System.out.println(r + " dimensions: " + width + "x" + height);
		// if (r.getHeight() == height || r.getWidth() == width) {
		left.setDimension(width, height);
		right.setDimension(width, height);
		middle.setDimension(width, height);
		// setPreferredSize(new Dimension(width, height));
		try {
			left.redraw(false);
			right.redraw(false);
			middle.redraw(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int offsetTop = ((getHeight() - height) / 2);

		g2.drawImage(getScaledImage(width, height), 0, offsetTop, null);
		g2.drawImage(left.getImage(), left.getX(), left.getY() + offsetTop,
				null);
		g2.drawImage(right.getImage(), right.getX(), right.getY() + offsetTop,
				null);
		g2.drawImage(middle.getImage(), middle.getX(), middle.getY()
				+ offsetTop, null);
		/*
		 * } else { if (r.getWidth() == width) {
		 * System.out.println("drawing all 3 buttons");
		 * g2.drawImage(left.getImage(), left.getX(), left.getY(), null);
		 * g2.drawImage(right.getImage(), right.getX(), right.getY(), null);
		 * g2.drawImage(middle.getImage(), middle.getX(), middle.getY(), null);
		 * } else if (r.getWidth() < width && r.getY() == 0 && r.getX() == 0) {
		 * System.out.println("drowing left button");
		 * g2.drawImage(left.getImage(), left.getX(), left.getY(), null); } else
		 * if (r.getWidth() < width && r.getY() == 0 && r.getX() > 0) {
		 * System.out.println("drawing right button");
		 * g2.drawImage(right.getImage(), right.getX(), right.getY(), null); }
		 * else if (r.getWidth() < width && r.getY() > 0 && r.getX() > 0) {
		 * System.out.println("drawing middle button");
		 * g2.drawImage(middle.getImage(), middle.getX(), middle.getY(), null);
		 * } }
		 */
		g2.dispose();

	}

	private BufferedImage getScaledImage(int width, int height) {

		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = newImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(lowPart, 0, 0, width, height, null);
		g.dispose();
		return newImage;
	}

	private void changeButtonState(String buttonName, String buttonState)
			throws IOException {
		// Graphics2D g2 = (Graphics2D) getGraphics();
		if (buttonName.equals("left")) {
			left.setState(buttonName, buttonState);
			// g2.drawImage(left.getImage(), left.getX(), left.getY(), null);
		} else if (buttonName.equals("right")) {
			right.setState(buttonName, buttonState);
			/*
			 * repaint(right.getX(), right.getY(), right.getWidth(),
			 * right.getHeight());
			 */
			// g2.drawImage(right.getImage(), right.getX(), right.getY(), null);
		} else if (buttonName.equals("middle")) {
			middle.setState(buttonName, buttonState);
			/*
			 * repaint(middle.getX(), middle.getY(), middle.getWidth(),
			 * middle.getHeight());
			 */
			// g2.drawImage(middle.getImage(), middle.getX(), middle.getY(),
			// null);
		}
		// g2.dispose();
		repaint();
	}

	public void pressButton(String buttonName) throws IOException {
		changeButtonState(buttonName, "pressed");
	}

	public void releaseButton(String buttonName) throws IOException {
		changeButtonState(buttonName, "released");
		synchronized (Refresher.activeMouseButtons) {
			Refresher.activeMouseButtons.put(buttonName,
					System.currentTimeMillis());
		}
	}

	public void setButtonToNormal(String buttonName) throws IOException {
		changeButtonState(buttonName, "normal");
	}

	public void mouseWell(boolean up, boolean press) throws IOException {

		// System.out.println("at mouse well up: " + up + " press: " + press);

		if (up) {
			if (press) {
				middle.setState("middle", "up");
			} else {
				middle.setState("middle", "up_released");
			}
		} else {
			if (press) {
				middle.setState("middle", "down");
			} else {
				middle.setState("middle", "down_released");
			}
		}

		repaint();
		synchronized (Refresher.activeMouseButtons) {
			Refresher.activeMouseButtons.put("middle",
					System.currentTimeMillis());
		}
	}

	public static void changeTheme(boolean black) {
		try {
			if (black) {
				lowPartImageFile = new File("Images" + File.separator + "Mouse"
						+ File.separator + "Black" + File.separator
						+ "mouse.png");
			} else {
				lowPartImageFile = new File("Images" + File.separator + "Mouse"
						+ File.separator + "White" + File.separator
						+ "mouse.png");
			}
			lowPart = ImageIO.read(lowPartImageFile);
			System.out.println("Mouse.Mouse()");
			MouseButtonProto.changeTheme(black);
			left = new MouseButtonProto(0, 0, 0, 176, 296);
			right = new MouseButtonProto(3, 86, 0, 176, 296);
			middle = new MouseButtonProto(6, 75, 28, 176, 296);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					Mouse.getInstance().repaint();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * @Override public Dimension getPreferredSize() { // TODO Auto-generated
	 * method stub return new Dimension(width,height); }
	 */

}
