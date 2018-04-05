package ro.titus.tools;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class But extends JButton {

	public But(BufferedImage normal, BufferedImage hover,
			BufferedImage pressed, int width, int height) {
		super(new ImageIcon(normal));
		setRolloverIcon(new ImageIcon(hover));
		setPressedIcon(new ImageIcon(pressed));
		setBorder(BorderFactory.createEmptyBorder());
		setFocusPainted(false);
		setContentAreaFilled(false);

		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(height, width));
	}

	public void changeNormal(BufferedImage img) {
		setIcon(new ImageIcon(img));
	}

	public void changeHover(BufferedImage img) {
		setRolloverIcon(new ImageIcon(img));
	}

	public void changePressed(BufferedImage img) {
		setPressedIcon(new ImageIcon(img));
	}
}