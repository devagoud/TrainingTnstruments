package ro.titus.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ro.titus.Entrance;
import ro.titus.tools.Refresher;
import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class ActiveTimeManager extends JFrame {

	static ActiveTimeManager activeTimeManager;

	public static ActiveTimeManager getInstance() {
		if (activeTimeManager == null) {
			activeTimeManager = new ActiveTimeManager();
		}
		return activeTimeManager;
	}

	JTextField time = new JTextField(String.valueOf(Settings.activeTime/1000));
	JTextField time2 = new JTextField(String.valueOf(Settings.singleKeyLoggingTime/1000));

	public ActiveTimeManager() {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);

		final JLabel message = new JLabel(
				"Set how long the buttons should remain active (blue)");

		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

		pan.add(message);

		JPanel horizontal = new JPanel();
		horizontal.setLayout(new BoxLayout(horizontal, BoxLayout.X_AXIS));
		JPanel horizontal2 = new JPanel();
		horizontal2.setLayout(new BoxLayout(horizontal2, BoxLayout.X_AXIS));

		time.setPreferredSize(new Dimension(40, 25));
		time.setMinimumSize(new Dimension(40, 25));
		time.setMaximumSize(new Dimension(40, 25));

		time2.setPreferredSize(new Dimension(40, 25));
		time2.setMinimumSize(new Dimension(40, 25));
		time2.setMaximumSize(new Dimension(40, 25));

		horizontal.add(new JLabel("Time: "));
		horizontal.add(time);
		horizontal.add(new JLabel(" in seconds"));

		pan.add(Box.createVerticalStrut(10));
		pan.add(horizontal);

		pan.add(Box.createVerticalStrut(20));
		pan.add(new JLabel("Single key logging time"));
		
		final JCheckBox active = new JCheckBox("Enable");
		active.setSelected(Settings.singleKeyLoggingEnable);
		
		pan.add(active);

		horizontal2.add(new JLabel("Time: "));
		horizontal2.add(time2);
		horizontal2.add(new JLabel(" in seconds"));

		pan.add(Box.createVerticalStrut(10));
		pan.add(horizontal2);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

		final JButton save = new JButton("Save");
		final JButton cancel = new JButton("Cancel");

		ActionListener l = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == save) {
					if (time.getText().matches("\\d+")) {
						time.setBackground(Color.green);
						try {
							int timeInt = Integer.parseInt(time.getText());

							if (timeInt > 10 || timeInt < 1) {
								message.setText("The time should be a value betwin 1 and 10");
								message.setForeground(Color.red);
							} else {
								Settings.activeTime = timeInt * 1000;
								Settings.refreshRate = (int) Math
										.floor(((timeInt * 1000) / 5));
								synchronized (Entrance.ref.t) {
									Entrance.ref.t.cancel();
									Entrance.ref = new Refresher();
									Entrance.ref.start();
								}

								dispose();
							}
						} catch (Exception ex) {
							time.setBackground(Color.red);
							ex.printStackTrace();
						}

					} else {
						time.setBackground(Color.red);
					}

					if (time2.getText().matches("\\d+")) {
						time2.setBackground(Color.green);
						try {
							int timeInt = Integer.parseInt(time2.getText());
							if (timeInt > 10 || timeInt < 1) {
								message.setText("The time should be a value betwin 1 and 10");
								message.setForeground(Color.red);
							} else {
								Settings.singleKeyLoggingTime = timeInt * 1000;
								dispose();
							}
						} catch (Exception ex) {
							time2.setBackground(Color.red);
							ex.printStackTrace();
						}

						Settings.singleKeyLoggingEnable = active.isSelected();
						
					} else {
						time2.setBackground(Color.red);
					}

				} else {
					dispose();
				}
			}
		};

		save.addActionListener(l);
		cancel.addActionListener(l);

		buttons.add(save);
		buttons.add(cancel);

		pan.add(buttons);

		getContentPane().add(pan);

		pack();
	}

}
