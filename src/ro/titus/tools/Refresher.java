package ro.titus.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import ro.titus.gui.Keyboard;
import ro.titus.gui.KeyboardMacSmall;
import ro.titus.gui.Mouse;

public class Refresher extends Thread implements Runnable {
	public static HashMap<String, Long> pressed = new HashMap<>();
	public static final HashMap<String, Long> activeMouseButtons = new HashMap<>();
	public Timer t;

	@Override
	public void run() {

		System.out.println("createing a new refresh timer");

		t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				long now = System.currentTimeMillis();
				synchronized (pressed) {
					Iterator<Map.Entry<String, Long>> it = pressed.entrySet()
							.iterator();
					while (it.hasNext()) {
						Map.Entry<String, Long> pairs = (Map.Entry<String, Long>) it
								.next();
						final String name = pairs.getKey();
						long added = pairs.getValue();
						if (now - added > Settings.activeTime) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									if (Settings.macSystem) {
										KeyboardMacSmall.getInstance()
												.changeKeyState(name, 0);
									} else {
										Keyboard.getInstance().changeKeyState(
												name, 0);
									}
								}
							});
							it.remove();
						}
					}

				}

				synchronized (activeMouseButtons) {
					Iterator<Map.Entry<String, Long>> it = activeMouseButtons
							.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Long> entry = it.next();
						final String buttonName = entry.getKey();
						final long time = entry.getValue();
						if (now - time > Settings.activeTime) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									try {
										Mouse.getInstance().setButtonToNormal(
												buttonName);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							});
							it.remove();
						}
					}
				}

			}
		}, 2000, Settings.refreshRate);

		System.out.println("The refresh timer was canceled");

		super.run();
	}

	public void refreshRateChange() {
		if (t != null) {
			t.cancel();
		}
	}

}
