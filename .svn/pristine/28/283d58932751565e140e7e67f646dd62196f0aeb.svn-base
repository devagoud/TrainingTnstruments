package ro.titus.java.video.rec;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ro.titus.tools.SessionRecOptions;

public class ScreenCaptureRectangle {

	public static Rectangle captureRect;
	JDialog dial;

	ScreenCaptureRectangle(final BufferedImage screen) {
		final BufferedImage screenCopy = new BufferedImage(screen.getWidth(),
				screen.getHeight(), screen.getType());
		final JLabel screenLabel = new JLabel(new ImageIcon(screenCopy));
		JScrollPane screenScroll = new JScrollPane(screenLabel);

		screenScroll
				.setPreferredSize(new Dimension((int) (screen.getWidth() + 10),
						(int) (screen.getHeight() + 10)));

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(screenScroll, BorderLayout.CENTER);

		repaint(screen, screenCopy);
		screenLabel.repaint();

		screenLabel.addMouseMotionListener(new MouseMotionAdapter() {

			Point start = new Point();

			@Override
			public void mouseMoved(MouseEvent me) {
				start = me.getPoint();
				repaint(screen, screenCopy);
				screenLabel.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent me) {
				Point end = me.getPoint();
				captureRect = new Rectangle(start, new Dimension(end.x
						- start.x, end.y - start.y));
				repaint(screen, screenCopy);
				screenLabel.repaint();
			}
		});

		dial = new JDialog();
		/*
		 * dial.setMinimumSize(new Dimension(screenCopy.getWidth() + 15, screen
		 * .getHeight() + 55)); dial.setMaximumSize(new
		 * Dimension(screenCopy.getWidth() + 15, screen .getHeight() + 55));
		 * dial.setPreferredSize(new Dimension(screenCopy.getWidth() + 15,
		 * screen .getHeight() + 55));
		 */
		dial.setTitle("Select the area you want to record");
		dial.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dial.setAlwaysOnTop(true);

		JButton saveArea = new JButton("Save area");
		saveArea.setAlignmentX(JButton.CENTER_ALIGNMENT);
		saveArea.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (captureRect != null && captureRect.width > 0
						&& captureRect.height > 0) {
					captureRect = new Rectangle(
							(int) ((captureRect.getX() * 2) + SessionRecOptions.selectedScreenBounds
									.getX()), (int) (captureRect.getY() * 2),
							(int) (captureRect.getWidth() * 2),
							(int) (captureRect.getHeight() * 2));
					dial.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "No area was selected");
				}

			}
		});

		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(panel);
		content.add(saveArea);

		dial.getContentPane().add(content);
		dial.pack();
		dial.setVisible(true);

	}

	public void repaint(BufferedImage orig, BufferedImage copy) {
		Graphics2D g = copy.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(orig, 0, 0, null);
		if (captureRect != null) {
			g.setColor(Color.RED);
			g.draw(captureRect);
			g.setColor(new Color(255, 255, 255, 150));
			g.fill(captureRect);
		}
		g.dispose();
	}

}