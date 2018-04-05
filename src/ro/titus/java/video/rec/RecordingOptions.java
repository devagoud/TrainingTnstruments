package ro.titus.java.video.rec;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import ro.titus.tools.SessionRecOptions;

@SuppressWarnings("serial")
public class RecordingOptions extends JDialog {
	private static JDialog current;
	private File file;
	private boolean multipleScreens = false;

	public RecordingOptions() {
		setAlwaysOnTop(true);
		current = this;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JButton full = new JButton("Full Screen");
		JButton select = new JButton("Select Area");
		full.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScreenCaptureRectangle.captureRect = null;
			}
		});
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Robot r;
						try {
							r = new Robot();
							Dimension d = Toolkit.getDefaultToolkit()
									.getScreenSize();

							final BufferedImage bufImg = new BufferedImage(
									(int) (d.getWidth() / 2), (int) (d
											.getHeight() / 2),
									BufferedImage.TYPE_4BYTE_ABGR);
							Image img = r.createScreenCapture(new Rectangle(0,
									0, (int) d.getWidth(), (int) d.getHeight()));
							Graphics2D g2 = (Graphics2D) bufImg.getGraphics();
							g2.setRenderingHint(
									RenderingHints.KEY_INTERPOLATION,
									RenderingHints.VALUE_INTERPOLATION_BILINEAR);
							g2.drawImage(img, 0, 0, (int) bufImg.getWidth(),
									(int) bufImg.getHeight(), 0, 0,
									(int) d.getWidth(), (int) d.getHeight(),
									null);

							g2.dispose();
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									new ScreenCaptureRectangle(bufImg);
								}
							});

						} catch (AWTException e1) {
							e1.printStackTrace();
						}
					}
				}).start();

			}
		});

		JButton output = new JButton("Set Output Directory");
		output.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i = fileChooser.showOpenDialog(current);
				if (i == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
				}
			}
		});

		final JSlider qualitySlider = new JSlider(JSlider.HORIZONTAL, 100, 0);

		qualitySlider.setMajorTickSpacing(10);
		qualitySlider.setMinorTickSpacing(1);
		qualitySlider.setValue(190 - SessionRecOptions.FramesPerSecond);

		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		pan.add(full);
		pan.add(select);

		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

		JPanel pan2 = new JPanel();
		pan2.setLayout(new BoxLayout(pan2, BoxLayout.Y_AXIS));
		pan2.add(output);

		pan.setBorder(BorderFactory.createTitledBorder("Recording Area"));
		pan2.setBorder(BorderFactory.createTitledBorder("Output Location"));

		JPanel pan3 = new JPanel();
		pan3.setLayout(new BoxLayout(pan3, BoxLayout.Y_AXIS));
		pan3.setBorder(BorderFactory.createTitledBorder("Set Video Quality"));
		pan3.add(qualitySlider);

		JPanel pan4 = new JPanel();
		pan4.setLayout(new BoxLayout(pan4, BoxLayout.X_AXIS));
		pan4.setBorder(BorderFactory.createTitledBorder("Set Audio Options"));

		final JCheckBox systemAudio = new JCheckBox("System Audio");
		final JCheckBox micAudio = new JCheckBox("Mic Audio");
		systemAudio.setSelected(false);
		micAudio.setSelected(true);

		pan4.add(systemAudio);
		pan4.add(micAudio);

		JPanel pan5 = new JPanel();
		pan5.setLayout(new BoxLayout(pan5, BoxLayout.X_AXIS));

		JButton save = new JButton("Save");
		JButton cancel = new JButton("Cancel");

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ScreenCaptureRectangle.captureRect != null) {
					SessionRecOptions.recordingArea = ScreenCaptureRectangle.captureRect;
				} else {
					SessionRecOptions.recordingArea = null;
				}
				int q = qualitySlider.getValue();
				SessionRecOptions.FramesPerSecond = 180 - q;

				System.out.println("value of q: " + q);
				if (q >= 97) {
					SessionRecOptions.quality = 1;
				} else if (q <= 97 && q > 60) {
					SessionRecOptions.quality = 1.4f;
				} else if (q <= 60 && q > 40) {
					SessionRecOptions.quality = 1.7f;
				} else if (q <= 40 && q > 20) {
					SessionRecOptions.quality = 2f;
				} else if (q <= 20 && q > 0) {
					SessionRecOptions.quality = 2.5f;
				} else if (q == 0) {
					SessionRecOptions.quality = 3f;
				}

				SessionRecOptions.folder = file;
				SessionRecOptions.includeSystemAudio = systemAudio.isSelected();
				SessionRecOptions.includeMicAudio = micAudio.isSelected();

				System.out.println("recording area: "
						+ SessionRecOptions.recordingArea);
				System.out.println("FPS: " + SessionRecOptions.FramesPerSecond);
				System.out.println("quality: " + SessionRecOptions.quality);
				System.out.println("folder: " + SessionRecOptions.folder);
				System.out.println("system audio: "
						+ SessionRecOptions.includeSystemAudio);
				System.out.println("mic audio: "
						+ SessionRecOptions.includeMicAudio);
				current.dispose();
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				current.dispose();
			}
		});

		pan5.add(save);
		pan5.add(Box.createHorizontalStrut(30));
		pan5.add(cancel);

		final JPanel pan6 = new JPanel();
		pan6.setLayout(new BoxLayout(pan6, BoxLayout.X_AXIS));
		pan6.setBorder(BorderFactory.createTitledBorder("Select Screen"));

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();

		multipleScreens = gs.length > 1;
		JLabel nrOfMonitors = new JLabel("Number of monitors detected: "
				+ String.valueOf(gs.length));
		final HashMap<String, Rectangle> boundsMap = new HashMap<String, Rectangle>();

		if (multipleScreens) {
			for (int i = 0; i < gs.length; i++) {
				GraphicsDevice dev = gs[i];
				final Rectangle bounds = dev.getDefaultConfiguration()
						.getBounds();

				JButton but = new JButton("Screen " + String.valueOf(i + 1)
						+ " (" + String.valueOf((int) bounds.getWidth()) + "x"
						+ String.valueOf((int) bounds.getHeight()) + ")");
				boundsMap.put(but.getText(), bounds);
				but.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// SessionRecOptions.selectedScreenBounds = bounds;
						ArrayList<Rectangle> selected = new ArrayList<Rectangle>();
						JButton thisBut = (JButton) e.getSource();
						boolean hasSelected = false;
						for (Component c : pan6.getComponents()) {
							if (c instanceof JButton && c != e.getSource()) {
								if (((JButton) c).getForeground() == Color.green) {
									selected.add(boundsMap.get(((JButton) c)
											.getText()));
									hasSelected = true;
								}
							}
						}
						if (thisBut.getForeground() != Color.green) {
							thisBut.setForeground(Color.green);
							selected.add(boundsMap.get(thisBut.getText()));
						} else {
							if (hasSelected) {
								thisBut.setForeground(Color.black);
							}else{
								selected.add(boundsMap.get(thisBut.getText()));
							}
						}
						int x = 1000;
						int width = 0;
						int height = 0;
						for (Rectangle sel : selected) {
							if (sel.x < x) {
								x = sel.x;
							}
							if(sel.height>height){
								height = sel.height;
							}
							width += sel.width;
						}
						SessionRecOptions.selectedScreenBounds = new Rectangle(x,0,width,height);
					}
				});
				if (i == 0) {
					SessionRecOptions.selectedScreenBounds = bounds;
					but.setForeground(Color.green);
				}
				pan6.add(but);
			}
		}

		main.add(pan);
		main.add(Box.createVerticalStrut(10));
		main.add(pan2);
		main.add(Box.createVerticalStrut(10));
		main.add(pan3);
		main.add(Box.createVerticalStrut(10));
		main.add(pan4);
		if (multipleScreens) {
			main.add(Box.createVerticalStrut(10));
			main.add(pan6);
		}
		main.add(Box.createVerticalStrut(20));
		main.add(pan5);
		main.add(Box.createVerticalStrut(20));
		main.add(nrOfMonitors);

		getContentPane().add(main);
		pack();
		setVisible(true);

	}
}
