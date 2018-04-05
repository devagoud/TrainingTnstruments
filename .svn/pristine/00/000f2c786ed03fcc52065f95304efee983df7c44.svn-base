package ro.titus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ro.titus.gui.Keyboard;
import ro.titus.gui.KeyboardMacBig;
import ro.titus.tools.Settings;

public class Tester {

	static int a = 0;
	static int i1 = 1;
	static int i2 = 2;
	static int[] arr = { a, i1, i2 };

	/*
	 * public static void main(String[] args) { SwingUtilities.invokeLater(new
	 * Runnable() {
	 * 
	 * @Override public void run() { Settings.macSystem = true; JFrame f = new
	 * JFrame(); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 * 
	 * f.setPreferredSize(new Dimension(400, 400));
	 * 
	 * @SuppressWarnings("serial") JPanel canvas = new JPanel() {
	 * 
	 * @Override protected void paintComponent(Graphics g) {
	 * super.paintComponent(g); Graphics2D g2 = (Graphics2D) g;
	 * g2.setColor(Color.blue); g2.fillRect(0, 0, 400, 400);
	 * g2.setColor(Color.black); f1Icon(g2, 100, 100, 40, 40); g2.dispose();
	 * 
	 * } };
	 * 
	 * f.getContentPane().add(canvas, BorderLayout.CENTER); f.pack();
	 * f.setVisible(true); } }); }
	 * 
	 * public static void f1Icon(Graphics2D g2, int x, int y, int width, int
	 * height) {
	 * 
	 * AffineTransform defaultTr = g2.getTransform(); AffineTransform tr = new
	 * AffineTransform(); tr.translate(x, y); g2.setStroke(new
	 * BasicStroke((width < 20) ? 2 : 3)); g2.setTransform(tr);
	 * 
	 * RoundRectangle2D.Double r = new RoundRectangle2D.Double(0, height / 4,
	 * width / 2, height / 2, 5, 5); g2.fill(r);
	 * 
	 * Arc2D.Double arc = new Arc2D.Double(width / 3, 0, width/2, height, 90,
	 * 180, Arc2D.CHORD); g2.fill(arc);
	 * 
	 * arc = new Arc2D.Double(width / 3, height/8, width/2, height-(height/4),
	 * 60, -120, Arc2D.OPEN);
	 * 
	 * g2.draw(arc); arc = new Arc2D.Double(width / 1.8, height/20, width/2,
	 * height-(height/10), 60, -120, Arc2D.OPEN);
	 * 
	 * g2.draw(arc);
	 * 
	 * 
	 * arc = new Arc2D.Double(width / 1.3, 0, width/2, height, 60, -120,
	 * Arc2D.OPEN);
	 * 
	 * g2.draw(arc);
	 * 
	 * 
	 * g2.setTransform(defaultTr);
	 * 
	 * 
	 * i1 = 100; i2 = 200; for (int i = 0; i < arr.length; i++) {
	 * System.out.println(arr[i]); }
	 * 
	 * 
	 * 
	 * }
	 */

	public static void main(String[] args) {
		
		Settings.macSystem = true;
		Settings.macKeyboardBig = true;
		Settings.populateMap();
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame f = new JFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Keyboard k = KeyboardMacBig.getInstance();
				
				k.setPreferredSize(new Dimension(800,235));
				
				f.getContentPane().add(k, BorderLayout.CENTER);
				f.pack();
				f.setVisible(true);
				
				
				BufferedImage image = new BufferedImage(f.getContentPane().getWidth(), f.getContentPane().getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
				f.getContentPane().paint(image.getGraphics());
				try {
					ImageIO.write(image, "png", new File("image.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
	}

}
