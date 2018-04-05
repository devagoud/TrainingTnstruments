package ro.titus.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ro.titus.gui.Logger;
import ro.titus.gui.MainFrame;
import ro.titus.gui.Mouse;
import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class ThemeMenu extends JMenu {

	public ThemeMenu() {
		setText("Theme");
		final JMenuItem theme1 = new JMenuItem("Black");
		final JMenuItem theme2 = new JMenuItem("White");
		add(theme1);
		add(theme2);
		ActionListener l = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				
				Mouse.changeTheme(e.getSource() == theme1);
				
				
				if (e.getSource() == theme1) {
					Settings.changeTheme(0);
					MainFrame.keyboard.repaint();
					MainFrame.getInstance().revalidate();
					new Thread(new Runnable() {

						@Override
						public void run() {
							Logger.getInstance().refresh();
						}
					}).start();
				} else if (e.getSource() == theme2) {
					Settings.changeTheme(1);
					MainFrame.keyboard.repaint();
					MainFrame.getInstance().revalidate();
					new Thread(new Runnable() {

						@Override
						public void run() {
							Logger.getInstance().refresh();
						}
					}).start();
				}

			}
		};

		theme1.addActionListener(l);
		theme2.addActionListener(l);
	}

}
