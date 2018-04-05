package ro.titus.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ro.titus.gui.MainFrame;

@SuppressWarnings("serial")
public class MouseMenu extends JMenu {
	static MouseMenu mouseMenu;
	static final JMenuItem closeMouse = new JMenuItem("Close");
	static final JMenuItem detachMouse = new JMenuItem("Detach");

	public static MouseMenu getInstance() {
		if (mouseMenu == null) {
			mouseMenu = new MouseMenu();
		}
		return mouseMenu;
	}

	private MouseMenu() {
		setText("Mouse");

		add(closeMouse);
		add(detachMouse);
		ActionListener l = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == detachMouse) {
					if (detachMouse.getText().equals("Detach")) {
						MainFrame.getInstance().detachComponent(
								MainFrame.mouse, "mouse");
						detachMouse.setText("Attach");
					} else {
						System.out.println("Attaching mouse");
						MainFrame.getInstance().attachComponent("mouse");
						detachMouse.setText("Detach");
						MainFrame.components.add(mouseMenu);
					}

					MainFrame.getInstance().pack();

				} else if (e.getSource() == closeMouse) {
					if (closeMouse.getText().equals("Close")) {
						MainFrame.getInstance().closeComponent("mouse");
					} else {
						MainFrame.getInstance().openComponent("mouse");
					}
				}
			}
		};

		closeMouse.addActionListener(l);
		detachMouse.addActionListener(l);

	}

	public void changeTitle(int menu, boolean active) {
		if (menu == 0) {
			if (active) {
				detachMouse.setText("Attach");
			} else {
				detachMouse.setText("Detach");
			}
		} else {
			if (active) {
				closeMouse.setText("Open");
			} else {
				closeMouse.setText("Close");
			}
		}
	}

	public void removeMenuItem() {
		remove(detachMouse);
	}

	public void addMenuItem() {
		add(detachMouse);

	}

}
