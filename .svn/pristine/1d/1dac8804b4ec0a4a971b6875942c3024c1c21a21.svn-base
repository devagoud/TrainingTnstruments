

package ro.titus.java.video.rec;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import ro.titus.tools.SessionRecOptions;

public class DesktopScreenRecorder extends ScreenRecorder {

	private Robot robot;
	private BufferedImage mouseCursor;

	private Rectangle fullSize;
	private Rectangle diminishedQuality;
	private Rectangle captureArea;

	public DesktopScreenRecorder(OutputStream oStream,
			ScreenRecorderListener listener) {
		super(oStream, listener);

		try {

			String mouseCursorFile;

			mouseCursorFile = "white_cursor.png";

			/*
			 * URL cursorURL = getClass().getClassLoader().getResource(
			 * mouseCursorFile);
			 */

			mouseCursor = ImageIO.read(new File(mouseCursorFile));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Rectangle initialiseScreenCapture() {
		try {
			robot = new Robot();
		} catch (AWTException awe) {
			awe.printStackTrace();
			return null;
		}
		if (SessionRecOptions.selectedScreenBounds != null) {
			fullSize = SessionRecOptions.selectedScreenBounds;
		} else {
			fullSize = new Rectangle(Toolkit.getDefaultToolkit()
					.getScreenSize());
		}

		if (SessionRecOptions.quality <= 1) {
			diminishedQuality = null;
		} else {
			if (ScreenCaptureRectangle.captureRect == null) {
				diminishedQuality = new Rectangle(
						0,
						0,
						(int) (fullSize.getWidth() / SessionRecOptions.quality),
						(int) (fullSize.getHeight() / SessionRecOptions.quality));
			} else {
				diminishedQuality = new Rectangle(
						0,
						0,
						(int) (ScreenCaptureRectangle.captureRect.getWidth() / SessionRecOptions.quality),
						(int) (ScreenCaptureRectangle.captureRect.getHeight() / SessionRecOptions.quality));
			}
		}

		if (ScreenCaptureRectangle.captureRect == null) {
			captureArea = fullSize;
			if (SessionRecOptions.quality <= 1) {
				System.out.println("full size: " + fullSize);
				System.out.println("capture area: " + captureArea);
				System.out.println("diminised: " + diminishedQuality);
				return fullSize;
			} else {
				System.out.println("full size: " + fullSize);
				System.out.println("capture area: " + captureArea);
				System.out.println("diminised: " + diminishedQuality);
				return diminishedQuality;
			}
		} else {
			captureArea = ScreenCaptureRectangle.captureRect;
			if (SessionRecOptions.quality <= 1) {
				System.out.println("full size: " + fullSize);
				System.out.println("capture area: " + captureArea);
				System.out.println("diminised: " + diminishedQuality);
				return ScreenCaptureRectangle.captureRect;
			} else {
				System.out.println("full size: " + fullSize);
				System.out.println("capture area: " + captureArea);
				System.out.println("diminised: " + diminishedQuality);
				return diminishedQuality;
			}
		}

	}

	/*
	 * public Robot getRobot() { return robot; }
	 */

	public BufferedImage captureScreen(Rectangle recordArea) {
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
		BufferedImage image = robot.createScreenCapture(captureArea);// fullSize);
		Graphics2D grfx = image.createGraphics();// nImg.createGraphics();
		grfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		int x = mousePosition.x - 8, y = mousePosition.y - 5;
		// System.out.println("normal x: " + x + " normal y: " + y);

		if (captureArea != fullSize) {
			x = (int) (mousePosition.x - (captureArea.getX()));
			y = (int) (mousePosition.y - (captureArea.getY()));
		}

		// System.out.println("mouse x: " + x + " mouse y: " + y);
		// System.out.println("rec area: " + recordArea);
		// System.out.println("full size: " + fullSize);

		if (x > 0 && x < captureArea.getWidth() && y > 0
				&& y < captureArea.getHeight()) {
			grfx.drawImage(mouseCursor, x, y, null);
		}
		grfx.dispose();

		if (diminishedQuality != null) {
			BufferedImage nImg = new BufferedImage((int) recordArea.getWidth(),
					(int) recordArea.getHeight(), image.getType());
			grfx = nImg.createGraphics();
			grfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			grfx.drawImage(image, 0, 0, (int) recordArea.getWidth(),
					(int) recordArea.getHeight(), 0, 0,
					(int) captureArea.getWidth(),
					(int) captureArea.getHeight(), null);
			grfx.dispose();
			return nImg;
		}

		return image;
		/*
		 * } else { Point mousePosition =
		 * MouseInfo.getPointerInfo().getLocation(); BufferedImage image =
		 * robot.createScreenCapture(fullSize); BufferedImage nImg = new
		 * BufferedImage((int) recordArea.getWidth(), (int)
		 * recordArea.getHeight(), image.getType()); Graphics2D grfx =
		 * nImg.createGraphics();
		 * grfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		 * RenderingHints.VALUE_INTERPOLATION_BILINEAR); grfx.drawImage(image,
		 * 0, 0, (int) recordArea.getWidth(), (int) recordArea.getHeight(), 0,
		 * 0, (int) fullSize.getWidth(), (int) fullSize.getHeight(), null);
		 * 
		 * grfx.drawImage(mouseCursor, (int)((mousePosition.x/quality) - 8),
		 * (int)((mousePosition.y/quality) - 5), null); grfx.dispose();
		 * 
		 * return nImg;
		 * 
		 * }
		 */
	}
}