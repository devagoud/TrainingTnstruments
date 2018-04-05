package ro.titus;

import javax.swing.SwingUtilities;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import ro.titus.gui.MainFrame;
import ro.titus.java.video.rec.JRecorder;
import ro.titus.tools.KeyboardAndMouseMonitor;
import ro.titus.tools.Refresher;
import ro.titus.tools.Settings;

public class Entrance {
	public static Refresher ref = new Refresher();
	public static KeyboardAndMouseMonitor km;

	public static void main(String[] args) {
		//Settings.setDir();
		Settings.setIcon();

		try {
			String systemType = System.getProperty("os.name");
			System.out.println("OS: " + systemType);
			if (systemType.toLowerCase().contains("mac")) {
				Settings.macSystem = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		ref.start();
		Settings.populateMap();
		// Settings.loadSettingFromFile();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainFrame.getInstance();
			}
		});

		try {
			km = new KeyboardAndMouseMonitor();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				GlobalScreen.getInstance().removeNativeKeyListener(km);
				GlobalScreen.getInstance().removeNativeMouseListener(km);
				GlobalScreen.getInstance().removeNativeMouseWheelListener(km);
				GlobalScreen.unregisterNativeHook();
				System.out.println("unregistering native listeners");
				ref.refreshRateChange();
				System.out.println("stoping the timer");
				Settings.saveSettings();
				JRecorder.shutdown();
			}
		}));

	}

}
