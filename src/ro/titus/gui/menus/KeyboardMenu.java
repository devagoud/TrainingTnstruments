package ro.titus.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ro.titus.gui.MainFrame;
import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class KeyboardMenu extends JMenu {
	static KeyboardMenu keyboardMenu;
	static final JMenuItem closeKeyboard = new JMenuItem("Close");
	static final JMenuItem detachKeyboard = new JMenuItem("Detach");
	static final JMenuItem bigKeyboard = new JMenuItem("Desktop Keyboard");
	static final JMenuItem smallKeyboard = new JMenuItem("Laptop Keyboard");

	public static KeyboardMenu getInstance() {
		if (keyboardMenu == null) {
			keyboardMenu = new KeyboardMenu();
		}
		return keyboardMenu;
	}

	private KeyboardMenu() {
		setText("Keyboard");

		add(closeKeyboard);
		add(detachKeyboard);

		if (Settings.macSystem) {
			add(smallKeyboard);
			add(bigKeyboard);
		}

		ActionListener l = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == detachKeyboard) {
					if (detachKeyboard.getText().equals("Detach")) {
						MainFrame.getInstance().detachComponent(
								MainFrame.keyboard, "keyboard");
						detachKeyboard.setText("Attach");
					} else {
						System.out.println("Attaching keyboard");
						MainFrame.getInstance().attachComponent("keyboard");
						detachKeyboard.setText("Detach");
						MainFrame.components.add(keyboardMenu);
					}

					MainFrame.getInstance().pack();

				} else if (e.getSource() == closeKeyboard) {
					if (closeKeyboard.getText().equals("Close")) {
						MainFrame.getInstance().closeComponent("keyboard");
					} else {
						MainFrame.getInstance().openComponent("keyboard");
					}
				} else if (e.getSource() == smallKeyboard) {
					MainFrame.getInstance().changeKeyboardLayout(true);
				} else if (e.getSource() == bigKeyboard) {
					MainFrame.getInstance().changeKeyboardLayout(false);
				}
			}
		};

		closeKeyboard.addActionListener(l);
		detachKeyboard.addActionListener(l);
		smallKeyboard.addActionListener(l);
		bigKeyboard.addActionListener(l);

	}

	public void changeTitle(int menu, boolean active) {
		if (menu == 0) {
			if (active) {
				detachKeyboard.setText("Attach");
			} else {
				detachKeyboard.setText("Detach");
			}
		} else {
			if (active) {
				closeKeyboard.setText("Open");
			} else {
				closeKeyboard.setText("Close");
			}
		}
	}

	public void removeMenuItem() {
		remove(detachKeyboard);
	}

	public void addMenuItem() {
		add(detachKeyboard);

	}

}
