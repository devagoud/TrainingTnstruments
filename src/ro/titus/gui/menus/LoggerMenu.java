package ro.titus.gui.menus;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import ro.titus.gui.DetachedComponent;
import ro.titus.gui.KeyCombinationsManager;
import ro.titus.gui.Logger;
import ro.titus.gui.MainFrame;
import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class LoggerMenu extends JMenu {
	static LoggerMenu loggerMenu;
	static final JMenuItem closeLogger = new JMenuItem("Close");
	static final JMenuItem detachLogger = new JMenuItem("Detach");
	static final JMenuItem loggerOrientetion = new JMenuItem(
			"Change Orientation");
	static final JMenuItem addKeyCombination = new JMenuItem(
			"Manage Keys Combinations");

	public static LoggerMenu getInstance() {
		if (loggerMenu == null) {
			loggerMenu = new LoggerMenu();
		}
		return loggerMenu;
	}

	private LoggerMenu() {
		setText("Logger");

		add(closeLogger);
		add(detachLogger);
		add(loggerOrientetion);
		add(addKeyCombination);
		ActionListener l = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == detachLogger) {
					if (detachLogger.getText().equals("Detach")) {
						MainFrame.getInstance().detachComponent(
								MainFrame.loggerWithScroll, "logger");
						detachLogger.setText("Attach");
					} else {
						System.out.println("Attaching logger");
						MainFrame.getInstance().attachComponent("logger");
						detachLogger.setText("Detach");
						MainFrame.components.add(loggerMenu);
					}

					MainFrame.getInstance().pack();

				} else if (e.getSource() == closeLogger) {
					if (closeLogger.getText().equals("Close")) {
						MainFrame.getInstance().closeComponent("logger");
					} else {
						MainFrame.getInstance().openComponent("logger");
					}
				} else if (e.getSource() == addKeyCombination) {
					KeyCombinationsManager kcm = KeyCombinationsManager
							.getInstance(true);
					kcm.setVisible(true);
					kcm.setAlwaysOnTop(true);
					kcm.refresh();
				} else if (e.getSource() == loggerOrientetion) {

					synchronized (Settings.horizontalLoggerLock) {

						Settings.horizontalLogger = !Settings.horizontalLogger;

						if (Settings.horizontalLogger) {
							MainFrame.loggerWithScroll
									.setPreferredSize(new Dimension(getWidth(),
											75));
							MainFrame.loggerWithScroll
									.setMaximumSize(new Dimension(2000, 75));
							MainFrame.loggerWithScroll
									.setMinimumSize(new Dimension(70, 75));
							MainFrame.loggerWithScroll
									.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
						} else {
							MainFrame.loggerWithScroll
									.setPreferredSize(new Dimension(210, 254));
							MainFrame.loggerWithScroll
									.setMinimumSize(new Dimension(210, 70));
							MainFrame.loggerWithScroll
									.setMaximumSize(new Dimension(210, 2000));
							MainFrame.loggerWithScroll
									.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						}

						System.out
								.println("at Change orientation logger is visible: "
										+ MainFrame.loggerIsVisible);
						
						Logger.getInstance().refresh();

						if (MainFrame.loggerIsVisible) {
							MainFrame.getInstance()
									.removeComponentFromMainFrame(
											MainFrame.loggerWithScroll);
							MainFrame.getInstance().showComponentInMainFrame(
									"logger");
						} else {
							synchronized (MainFrame.detachedComponents) {
								for (DetachedComponent dc : MainFrame.detachedComponents) {
									System.out
											.println("loopint through detached components: "
													+ dc.getComponentName());
									if (dc != null
											&& dc.getComponentName().equals(
													"logger")) {
										System.out
												.println("found the logger frame");
										synchronized (Settings.horizontalLoggerLock) {
											if (Settings.horizontalLogger) {
												dc.setPreferredSize(new Dimension(
														500, 100));
											} else {
												dc.setPreferredSize(new Dimension(
														210, 260));

											}
											dc.pack();
										}

									}
								}
							}
						}
					}

				}
			}
		};

		closeLogger.addActionListener(l);
		detachLogger.addActionListener(l);
		loggerOrientetion.addActionListener(l);
		addKeyCombination.addActionListener(l);

	}

	public void changeTitle(int menu, boolean active) {
		if (menu == 0) {
			if (active) {
				detachLogger.setText("Attach");
			} else {
				detachLogger.setText("Detach");
			}
		} else {
			if (active) {
				closeLogger.setText("Open");
			} else {
				closeLogger.setText("Close");
			}
		}
	}

	public void removeMenuItem() {
		remove(detachLogger);
		remove(loggerOrientetion);
	}

	public void addMenuItem() {
		add(detachLogger);
		add(loggerOrientetion);
	}

}
