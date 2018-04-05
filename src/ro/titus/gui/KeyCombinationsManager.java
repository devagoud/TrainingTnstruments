package ro.titus.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class KeyCombinationsManager extends JFrame {

	public static HashMap<String, Object[]> keyCombinationMap = new HashMap<>();

	static KeyCombinationsManager keyCombinationManager;
	static int yPosition = 3;
	static Color defaultBackgroundColor;
	final static JPanel show = new JPanel();
	final static JPanel conteiner = new JPanel();
	GridBagConstraints c = new GridBagConstraints();
	final static JPanel mainPan = new JPanel();
	static final JLabel message = new JLabel(
			"To add a new description for a keys combination, press those keys now");

	static volatile boolean editingTheDescription = false;

	static ActionListener l;
	static JButton impBut = new JButton("Import");

	public static KeyCombinationsManager getInstance(boolean create) {
		if (keyCombinationManager == null) {
			if (!create) {
				return null;
			}
			keyCombinationManager = new KeyCombinationsManager();
		}
		return keyCombinationManager;
	}

	public KeyCombinationsManager() {
		
		try {
			setIconImage(Settings.icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Logger Manager");
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Options");
		final JMenuItem imp = new JMenuItem("Import");
		final JMenuItem exp = new JMenuItem("Export");
		final JMenuItem rmv = new JMenuItem("Remove All");
		fileMenu.add(imp);
		fileMenu.add(exp);
		fileMenu.add(rmv);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

		final JCheckBox shiftPL = new JCheckBox(
				"Log \"Shift\" + \"Single Letter\"");
		final JCheckBox logSingleLetterLongPress = new JCheckBox(
				"Log single letter long press");

		logSingleLetterLongPress.setSelected(Settings.singleKeyLoggingEnable);
		shiftPL.setSelected(Settings.logShiftPlusSingleLetter);

		l = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("import")) {
					importKeysCombinations();
				} else if (e.getActionCommand().equals("export")) {
					exportKeysCombinations();
				} else if (e.getActionCommand().equals("rmv")) {
					synchronized (keyCombinationMap) {
						keyCombinationMap.clear();
						refresh();
					}
				} else if (e.getSource() == shiftPL) {
					Settings.logShiftPlusSingleLetter = shiftPL.isSelected();
				} else if (e.getSource() == logSingleLetterLongPress) {
					Settings.singleKeyLoggingEnable = logSingleLetterLongPress
							.isSelected();
				}
			}
		};

		imp.setActionCommand("import");
		exp.setActionCommand("export");
		rmv.setActionCommand("rmv");
		imp.addActionListener(l);
		exp.addActionListener(l);
		rmv.addActionListener(l);

		impBut.setActionCommand("import");
		impBut.addActionListener(l);

		mainPan.setLayout(new GridBagLayout());
		defaultBackgroundColor = mainPan.getBackground();

		JPanel conteiner = new JPanel();
		conteiner.setLayout(new BoxLayout(conteiner, BoxLayout.Y_AXIS));

		logSingleLetterLongPress.setActionCommand("longPress");
		shiftPL.setActionCommand("shiftSingle");

		logSingleLetterLongPress.addActionListener(l);
		shiftPL.addActionListener(l);

		JScrollPane scroll = new JScrollPane(mainPan);
		scroll.setPreferredSize(new Dimension(650, 400));
		conteiner.add(Box.createVerticalStrut(20));
		conteiner.add(shiftPL);
		conteiner.add(Box.createVerticalStrut(20));
		conteiner.add(logSingleLetterLongPress);
		conteiner.add(Box.createVerticalStrut(20));
		conteiner.add(message);
		conteiner.add(Box.createVerticalStrut(10));
		conteiner.add(show);
		conteiner.add(Box.createVerticalStrut(30));
		conteiner.add(scroll);
		getContentPane().add(conteiner);
		pack();
		setVisible(true);

	}

	@SuppressWarnings("unchecked")
	public void refresh() {

		mainPan.removeAll();

		show.removeAll();
		message.setText("To add a new description for a keys combination, press those keys now");
		message.setForeground(Color.black);

		yPosition = 2;

		synchronized (keyCombinationMap) {

			if (keyCombinationMap.size() == 0) {
				showImportButton();
			} else {
				addHeaders();
				HashMap<String, String> map = new HashMap<>();

				ArrayList<String> sorted = new ArrayList<>();
				for (String description : keyCombinationMap.keySet()) {
					Object[] obje = keyCombinationMap.get(description);
					ArrayList<String> values = (ArrayList<String>) obje[0];
					String str = "";
					for (String v : values) {
						str += v.toLowerCase().replaceAll("[^\\p{L}\\p{Nd}]",
								"");

					}
					sorted.add(str);
					map.put(str, description);
				}

				Collections.sort(sorted);

				System.out.println("keyscombination map size: "
						+ keyCombinationMap.size());

				int loopNr = 0;

				for (String key : sorted) {
					System.out.println("Loopint: " + loopNr++ + " y is: "
							+ yPosition);
					if (map.containsKey(key)) {
						String description = map.get(key);
						Object[] obje = keyCombinationMap.get(description);
						ArrayList<String> values = (ArrayList<String>) obje[0];
						if (keyCombinationMap.containsKey(description)) {
							if (values.size() == 0) {
								System.out
										.println("at map loop the keys arraylist size is 0");
								continue;
							}
							int[] dim = Settings.getKeyCombinationSize(values,
									description);
							int width = dim[0];
							LoggerKeyCombination keysCombination = new LoggerKeyCombination(
									values, width, Settings.defaultKeyWidth,
									"", 0, 0, 0);
							addNewKeyCombination(keysCombination, description,
									yPosition++, (boolean) obje[1]);
						} else {
							System.out.println("description not in the map");
						}
					} else {
						System.out.println("key not in the map");
					}
				}
			}
		}
		mainPan.revalidate();
		repaint();
		pack();
	}

	private void addNewKeyCombination(final BufferedImage img,
			final String desc, final int y, boolean activated) {

		final JCheckBox active = new JCheckBox();
		active.setSelected(activated);

		final JLabel image = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, null);
				g.dispose();
			}
		};

		image.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));

		final JTextField description = new JTextField(desc);
		final JButton remove = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				drawX(g2);
				g2.dispose();
			}
		};
		final JButton update = new JButton("Update");
		description.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				update.setText("<html><font color=red>Update<html>");
			}
		});

		remove.setPreferredSize(new Dimension(20, 20));
		remove.setMaximumSize(new Dimension(20, 20));
		update.setPreferredSize(new Dimension(110, 25));
		update.setMaximumSize(new Dimension(110, 25));

		System.out.println("adding: " + desc + " at: " + y);

		c.gridx = 0;
		c.gridy = y;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		mainPan.add(active, c);
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = y;
		c.fill = GridBagConstraints.HORIZONTAL;
		mainPan.add(image, c);
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = y;
		c.fill = GridBagConstraints.HORIZONTAL;
		mainPan.add(description, c);
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = y;
		c.anchor = GridBagConstraints.CENTER;
		mainPan.add(update, c);
		c.gridx = 4;
		c.gridy = y;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 1;
		mainPan.add(remove, c);

		ActionListener l = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("action fired");
				if (e.getSource() == remove) {

					synchronized (keyCombinationMap) {
						System.out.println("Removing keysCombination");
						keyCombinationMap.remove(desc);
					}

					System.out.println("the remove button was pressed");
					mainPan.remove(active);
					mainPan.remove(image);
					mainPan.remove(description);
					mainPan.remove(update);
					mainPan.remove(remove);

					active.removeActionListener(this);
					update.removeActionListener(this);
					remove.removeActionListener(this);
					mainPan.revalidate();
					mainPan.repaint();

					synchronized (keyCombinationMap) {
						if (keyCombinationMap.size() == 0) {
							mainPan.removeAll();
							showImportButton();
						}
					}
					new Thread(new Runnable() {

						@Override
						public void run() {
							Logger.getInstance().refresh();
						}
					}).start();

				} else if (e.getSource() == update) {
					System.out.println("Update: " + desc + " to: "
							+ description.getText());

					update.setText("<html><font color=green>Update<html>");

					synchronized (keyCombinationMap) {
						Object[] obj = keyCombinationMap.remove(desc);
						keyCombinationMap.put(description.getText(), obj);
					}

					mainPan.remove(active);
					mainPan.remove(image);
					mainPan.remove(description);
					mainPan.remove(update);
					mainPan.remove(remove);

					active.removeActionListener(this);
					update.removeActionListener(this);
					remove.removeActionListener(this);
					refresh();

					new Thread(new Runnable() {

						@Override
						public void run() {
							Logger.getInstance().refresh();
						}
					}).start();

				} else if (e.getSource() == active) {
					System.out.println((((JCheckBox) e.getSource())
							.isSelected() ? "Activate: " : "Dezactivate: ")
							+ desc);

					update.setText("<html><font color=red>Update<html>");

					synchronized (keyCombinationMap) {
						if (keyCombinationMap.containsKey(desc)) {
							Object[] obj = keyCombinationMap.get(desc);
							obj[1] = active.isSelected();
							keyCombinationMap.put(desc, obj);
						} else {
							System.out
									.println("The keycombination map doesn't contain the desctiption: "
											+ desc);
						}
					}

				}
			}
		};
		active.addActionListener(l);
		remove.addActionListener(l);
		update.addActionListener(l);

	}

	public void showKeyCombination(final ArrayList<String> k) {

		System.out.println("############## the show function was called: "
				+ k.size());
		message.setText("");

		final ArrayList<String> keys = new ArrayList<>(k);

		if (editingTheDescription) {
			return;
		}

		if (keys.size() == 0) {
			System.out.println("the keys arraylist size is 0");
			return;
		}

		show.removeAll();

		int[] dim = Settings.getKeyCombinationSize(keys, "");
		int width = dim[0];

		final LoggerKeyCombination img = new LoggerKeyCombination(keys, width,
				Settings.defaultKeyWidth, "", 0, 0, 0);

		show.setLayout(new BoxLayout(show, BoxLayout.X_AXIS));
		final JLabel image = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, null);
				g.dispose();
			}
		};

		image.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		image.setMinimumSize(new Dimension(img.getWidth(), img.getHeight()));
		image.setMaximumSize(new Dimension(img.getWidth(), img.getHeight()));

		System.out.println("image size: " + img.getWidth() + "x"
				+ img.getHeight());

		final JTextField description = new JTextField();
		final JButton save = new JButton("Save");
		final JButton remove = new JButton() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				drawX(g2);
				g2.dispose();
			}
		};

		remove.setPreferredSize(new Dimension(20, 20));
		remove.setMaximumSize(new Dimension(20, 20));

		final FocusListener fl = new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				editingTheDescription = false;
			}

			@Override
			public void focusGained(FocusEvent e) {
				editingTheDescription = true;
			}
		};

		ActionListener l = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == save) {
					if (description.getText().length() == 0) {
						description.setBackground(Color.red);
						return;
					} else {
						boolean alreadyExists = false;
						synchronized (KeyCombinationsManager.keyCombinationMap) {
							for (String desc : KeyCombinationsManager.keyCombinationMap
									.keySet()) {
								Object[] obj = KeyCombinationsManager.keyCombinationMap
										.get(desc);
								@SuppressWarnings("unchecked")
								ArrayList<String> registeredKeys = (ArrayList<String>) obj[0];
								if (registeredKeys.size() != 0
										&& registeredKeys.size() == keys.size()) {
									int all = keys.size();
									for (String key : keys) {
										if (registeredKeys.contains(key)) {
											all--;
										}
									}
									if (all == 0) {
										alreadyExists = true;
									}

								}
							}
						}

						if (alreadyExists) {
							message.setText("There is already a description for the current keys combination");
							message.setForeground(Color.red);
							System.out.println("already exists");
							return;
						}

						description.setBackground(Color.green);
						new Thread(new Runnable() {

							@Override
							public void run() {
								Logger.getInstance().refresh();
							}
						}).start();
					}
					synchronized (keyCombinationMap) {
						if (keys.size() > 0) {
							keyCombinationMap.put(description.getText(),
									new Object[] { keys, true });
						}
					}
					refresh();
					message.setText("The new keys combination description was added");
					message.setForeground(Color.green);

					save.removeActionListener(this);
					remove.removeActionListener(this);

					show.removeAll();
					show.revalidate();
					revalidate();

					editingTheDescription = false;
					description.removeFocusListener(fl);

				} else if (e.getSource() == remove) {
					save.removeActionListener(this);
					remove.removeActionListener(this);

					show.removeAll();
					show.revalidate();
					message.setText("To add a new description for a keys combination, press those keys now");
					message.setForeground(Color.black);
					revalidate();

					editingTheDescription = false;
					description.removeFocusListener(fl);
				}
			}
		};

		save.addActionListener(l);

		description.setMaximumSize(new Dimension(100, 20));
		JLabel equal = new JLabel(" = ");
		equal.setMaximumSize(new Dimension(20, 25));
		save.setMaximumSize(new Dimension(110, 25));

		description.addFocusListener(fl);
		remove.addActionListener(l);

		show.add(image);
		show.add(equal);
		show.add(description);
		show.add(Box.createHorizontalStrut(20));
		show.add(save);
		show.add(Box.createHorizontalStrut(15));
		show.add(remove);
		show.revalidate();
		revalidate();

	}

	private void importKeysCombinations() {
		JFileChooser chooser = new JFileChooser();
		int ret = chooser.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			final File file = chooser.getSelectedFile();
			new Thread(new Runnable() {

				@Override
				public void run() {

					try {

						File dir = new File("Settings");
						if (!dir.exists()) {
							dir.mkdir();
						}

						Files.copy(file.toPath(),
								new File(dir, file.getName()).toPath(),
								StandardCopyOption.REPLACE_EXISTING);
						Settings.loadSettingFromFile(file.getName());
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								refresh();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	private void exportKeysCombinations() {
		JFileChooser chooser = new JFileChooser();

		int ret = chooser.showSaveDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			final File file = chooser.getSelectedFile();
			new Thread(new Runnable() {

				@Override
				public void run() {

					try (FileOutputStream fos = new FileOutputStream(file)) {
						try (ObjectOutputStream oos = new ObjectOutputStream(
								fos)) {
							synchronized (keyCombinationMap) {
								oos.writeObject(keyCombinationMap);
								oos.flush();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();

		}

	}

	private void addHeaders() {
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 0, 20);
		c.anchor = GridBagConstraints.CENTER;
		mainPan.add(new JLabel("Active"), c);
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 0, 20);
		c.anchor = GridBagConstraints.CENTER;
		mainPan.add(new JLabel("Keys Combination"), c);
		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 0, 20);
		c.anchor = GridBagConstraints.CENTER;
		mainPan.add(new JLabel("Description"), c);
		c.gridx = 3;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 0, 20);
		c.anchor = GridBagConstraints.CENTER;
		mainPan.add(new JLabel("Update"), c);
		c.gridx = 4;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 0, 20);
		c.anchor = GridBagConstraints.CENTER;
		mainPan.add(new JLabel("Remove"), c);
	}

	private void showImportButton() {
		impBut.setPreferredSize(new Dimension(100, 25));
		impBut.setMaximumSize(new Dimension(100, 25));
		impBut.setMinimumSize(new Dimension(100, 25));
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;

		mainPan.add(impBut);
		revalidate();
		repaint();
	}

	private void drawX(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(defaultBackgroundColor);
		g2.fillRect(0, 0, getWidth(), getHeight());

		final Color red = Color.decode("#FF0000");
		final Color red3 = Color.decode("#FFC9C9");
		GradientPaint gPaint = new GradientPaint(0, 0, red3, 0, 20, red);
		g2.setPaint(gPaint);
		g2.fillOval(0, 0, 20, 20);

		BasicStroke stroke = new BasicStroke(4, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND);

		g2.setStroke(stroke);
		g2.setColor(Color.white);

		g2.drawLine(6, 6, 14, 14);
		g2.drawLine(6, 14, 14, 6);

	}
}
