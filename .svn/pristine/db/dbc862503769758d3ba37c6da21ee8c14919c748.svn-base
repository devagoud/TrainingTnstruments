package ro.titus.gui.menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ro.titus.gui.ActiveTimeManager;
import ro.titus.gui.MainFrame;
import ro.titus.java.video.rec.RecordingOptions;
import ro.titus.player.PlayerGUI;
import ro.titus.tools.Settings;

@SuppressWarnings("serial")
public class SettingsMenu extends JMenu {

	public SettingsMenu() {
		setText("Settings");
		final JMenuItem activeTime = new JMenuItem("Time Settings");
		final JCheckBox keepAR = new JCheckBox("Keep Aspect Ratio");
		final JMenuItem playVideo = new JMenuItem("Play Video");
		//final JMenuItem video = new JMenuItem("Start Video Recording");
		final JMenuItem videoOptions = new JMenuItem("Video Options");

		

		// final JMenuItem keyboardD = new JMenuItem("Start Keyboard Debug");
		// final JMenuItem mouseD = new JMenuItem("Start Mouse Debug");

		keepAR.setSelected(Settings.keepAspectRatio);
		add(keepAR);
		add(activeTime);

		// add(keyboardD);
		// add(mouseD);

		//add(video);
		add(playVideo);
		add(videoOptions);

		//video.setBackground(Color.orange);
		playVideo.setBackground(Color.blue);
		videoOptions.setBackground(Color.GREEN);

		ActionListener l = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == activeTime) {

					ActiveTimeManager.getInstance().setVisible(true);

				} else if (e.getSource() == keepAR) {
					Settings.keepAspectRatio = keepAR.isSelected();
					System.out.println("keep aspect ratio: "
							+ Settings.keepAspectRatio);
					MainFrame.keyboard.repaint();
					MainFrame.getInstance().repaint();
				} /*
				 * else if (e.getSource() == keyboardD) { if
				 * (keyboardD.getText().startsWith("Stop")) {
				 * keyboardD.setText("Start Keyboard Debug"); new Thread(new
				 * Runnable() {
				 * 
				 * @Override public void run() { File dir = new File("Debug");
				 * if (!dir.exists()) { dir.mkdir(); } try (PrintWriter bos =
				 * new PrintWriter( new FileOutputStream( new File( dir,
				 * "KeyboardDebug" + String.valueOf(System .currentTimeMillis())
				 * + ".txt")))) { synchronized
				 * (KeyboardAndMouseMonitor.keyboardDebug) { for (String line :
				 * KeyboardAndMouseMonitor.keyboardDebug) { bos.append(line +
				 * "\n"); } bos.flush(); bos.close(); } } catch (Exception e2) {
				 * e2.printStackTrace(); } } }).start(); } else {
				 * keyboardD.setText("Stop Keyboard Debug"); synchronized
				 * (KeyboardAndMouseMonitor.keyboardDebug) {
				 * KeyboardAndMouseMonitor.keyboardDebug.clear(); } }
				 * 
				 * } else if (e.getSource() == mouseD) {
				 * 
				 * if (mouseD.getText().startsWith("Stop")) {
				 * mouseD.setText("Start Mouse Debug"); new Thread(new
				 * Runnable() {
				 * 
				 * @Override public void run() { File dir = new File("Debug");
				 * if (!dir.exists()) { dir.mkdir(); } try (PrintWriter bos =
				 * new PrintWriter( new FileOutputStream( new File( dir,
				 * "MouseDebug" + String.valueOf(System .currentTimeMillis()) +
				 * ".txt")))) { synchronized
				 * (KeyboardAndMouseMonitor.mouseDebug) { for (String line :
				 * KeyboardAndMouseMonitor.mouseDebug) { bos.append(line +
				 * "\n"); } bos.flush(); bos.close(); } } catch (Exception e2) {
				 * e2.printStackTrace(); } } }).start(); } else {
				 * mouseD.setText("Stop Mouse Debug"); synchronized
				 * (KeyboardAndMouseMonitor.mouseDebug) {
				 * KeyboardAndMouseMonitor.mouseDebug.clear(); } }
				 * 
				 * }
				 else if (e.getSource() == video) {
					if (video.getText().startsWith("Start")) {
						video.setBackground(Color.red);
						VideoRecording.startRecording();
						video.setText("Stop Video Recording");
					} else {
						video.setBackground(Color.orange);
						VideoRecording.stopRecording();
						video.setText("Start Video Recording");
					}
				}*/ else if (e.getSource() == playVideo) {
					if (playVideo.getText().startsWith("Play")) {
						/*
						 * playVideo.setBackground(Color.green);
						 * playVideo.setText("Stop Video");
						 */
						new PlayerGUI();
						// VideoRecording.playVideo();
					} else {
						playVideo.setBackground(Color.blue);
						playVideo.setText("Play Video");
					}
				} else if (e.getSource() == videoOptions) {
					new RecordingOptions();
				}
			}
		};

		keepAR.addActionListener(l);
		activeTime.addActionListener(l);
		// keyboardD.addActionListener(l);
		// mouseD.addActionListener(l);
		//video.addActionListener(l);
		playVideo.addActionListener(l);
		videoOptions.addActionListener(l);

	}
}
