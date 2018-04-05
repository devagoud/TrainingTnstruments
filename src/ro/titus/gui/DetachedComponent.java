package ro.titus.gui;

import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import ro.titus.gui.menus.KeyboardMenu;
import ro.titus.gui.menus.LoggerMenu;
import ro.titus.gui.menus.MouseMenu;
import ro.titus.gui.menus.SettingsMenu;
import ro.titus.gui.menus.ThemeMenu;
import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class DetachedComponent extends JFrame implements WindowListener {
	String componentName = "";

	public DetachedComponent(Component component, String componentName) {
		
		try {
			setIconImage(Settings.icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.componentName = componentName;
		setTitle(componentName);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(component);

		if (component instanceof JScrollPane) {
			JScrollBar upAndDown = ((JScrollPane) component)
					.getHorizontalScrollBar();
			JScrollBar leftAndRight = ((JScrollPane) component)
					.getVerticalScrollBar();

			AdjustmentListener adjLis = new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					System.out
							.println("MainFrame.MainFrame().new AdjustmentListener() {...}.adjustmentValueChanged()");
					System.out.println(e);
					repaint();
				}
			};

			upAndDown.addAdjustmentListener(adjLis);
			leftAndRight.addAdjustmentListener(adjLis);
		}

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuBar.add(new SettingsMenu());
		menuBar.add(new ThemeMenu());
		
		if (componentName.equals("mouse")) {
			menuBar.add(MouseMenu.getInstance());
		} else if (componentName.equals("keyboard")) {
			menuBar.add(KeyboardMenu.getInstance());
		}else if(componentName.equals("logger")){
			menuBar.add(LoggerMenu.getInstance());
		}

		pack();
		setVisible(true);
		setAlwaysOnTop(true);
		addWindowListener(this);
	}

	public String getComponentName() {
		return componentName;
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println(e);

		MainFrame.getInstance().closeComponent(componentName);
	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

}
