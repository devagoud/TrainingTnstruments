package ro.titus.player;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class ToolsBar {

	private Image playNormal, playHover, pauseNormal, pauseHover, stopNormal,
			stopHover, soundIconN, soundIconH, noSoundIconN, noSoundIconH;;
	private volatile boolean playState = false;
	private volatile boolean soundState = true;
	private volatile boolean playIsHover = false;
	private volatile boolean stopIsHover = false;
	private volatile boolean soundIsHover = false;
	private volatile int alpha = 100;
	private long totalTime;
	private int dim;

	public ToolsBar(int width, int height) {
		dim = height;
		try {
			playNormal = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "play_n.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);
			playHover = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "play_h.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);

			pauseNormal = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "pause_n.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);
			pauseHover = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "pause_h.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);

			stopNormal = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "stop_n.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);
			stopHover = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "stop_h.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);
			soundIconN = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "sound_n.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);
			soundIconH = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "sound_h.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);

			noSoundIconN = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "no_sound_n.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);
			noSoundIconH = ImageIO.read(
					new File("Images" + File.separator + "Buttons"
							+ File.separator + "no_sound_h.png"))
					.getScaledInstance(height, height, Image.SCALE_SMOOTH);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	long progress = 50;
	Color back1 = Color.DARK_GRAY;
	Color back2 = Color.GRAY;
	Color front1 = Color.decode("#FF9900");
	Color front2 = Color.YELLOW;
	Color back3 = Color.decode("#477519");
	Color back4 = Color.decode("#ADC299");
	Color front3 = Color.decode("#002900");
	Color front4 = Color.decode("#80B280");
	GradientPaint back;
	GradientPaint front;
	long current;
	int soundVolum = 70;
	int prevSoundVolume = 70;

	public BufferedImage getToolBarImage(int width, long frameTime) {
		setProgress(frameTime);
		return getToolBarImage(width);
	}

	public BufferedImage getToolBarImage(int width) {
		int progressBarWidth = width - (dim * 7);
		int soundBarWidth = (dim * 4);
		BufferedImage principalImage = new BufferedImage(width, dim,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) principalImage.getGraphics();
		int type = AlphaComposite.SRC_OVER;
		g2.setComposite((AlphaComposite.getInstance(type, (alpha <= 10) ? 0
				: ((float) alpha / 100f))));
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(getPlayButon(), 0, 0, null);
		g2.drawImage(getStopButton(), dim, 0, null);
		g2.drawImage(getVideoBar(progressBarWidth, dim), dim * 2, 0, null);
		g2.drawImage(getSoundButton(), (progressBarWidth + (dim * 2)), 0, null);
		g2.drawImage(getVolumBar(soundBarWidth, dim),
				(progressBarWidth + (dim * 3)), 0, null);
		g2.dispose();
		return principalImage;
	}

	private String getTime(long time) {
		long hour = Math.max(0, TimeUnit.MILLISECONDS.toHours(time));
		long min = Math.max(
				0,
				(TimeUnit.MILLISECONDS.toMinutes(time
						- TimeUnit.HOURS.toMillis(hour))));
		long sec = Math.max(
				0,
				(TimeUnit.MILLISECONDS.toSeconds(time
						- TimeUnit.MINUTES.toMillis(min)
						- TimeUnit.HOURS.toMillis(hour))));

		return String.format("%02d:%02d:%02d", hour, min, sec);
	}

	private Image getPlayButon() {
		if (playIsHover) {
			if (playState) {
				return pauseHover;
			} else {
				return playHover;
			}
		} else {
			if (playState) {
				return pauseNormal;
			} else {
				return playNormal;
			}
		}
	}

	private Image getStopButton() {
		if (stopIsHover) {
			return stopHover;
		} else {
			return stopNormal;
		}
	}

	private Image getSoundButton() {
		if (soundIsHover) {
			if (soundState) {
				return soundIconH;
			} else {
				return noSoundIconH;
			}
		} else {
			if (soundState) {
				return soundIconN;
			} else {
				return noSoundIconN;
			}
		}
	}

	private BufferedImage getVolumBar(int w, int h) {
		BufferedImage snd = new BufferedImage(w, h,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D sndG2 = (Graphics2D) snd.getGraphics();
		sndG2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		back = new GradientPaint(0, 0, back1, 0, (h / 2), back2, true);
		front = new GradientPaint(0, 0, front3, 0, (h / 2), front4, true);
		sndG2.setPaint(front);
		sndG2.fillRoundRect(0, 0, 5, h, 15, 15);
		int frontWidth = soundVolum;
		sndG2.fillRect(3, 0, frontWidth, h);
		sndG2.setPaint(back);
		frontWidth = Math.max(frontWidth, 3);
		sndG2.fillRect(frontWidth, 0, (w - frontWidth), h);
		sndG2.dispose();
		return snd;
	}

	private BufferedImage getVideoBar(int w, int h) {
		BufferedImage comp = new BufferedImage(w, h,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) comp.getGraphics();

		back = new GradientPaint(0, 0, back1, 0, (h / 2), back2, true);
		front = new GradientPaint(0, 0, front1, 0, (h / 2), front2, true);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setPaint(front);
		int frontWidth = (int) (((float) w / 100f) * progress);
		g2.fillRoundRect(0, 0, 5, h, 5, 5);
		g2.fillRect(3, 0, frontWidth, h);
		g2.setPaint(back);
		frontWidth = Math.max(frontWidth, 3);
		g2.fillRect(frontWidth, 0, (w - frontWidth), h);
		String totalStamp = getTime(totalTime);
		String currentStamp = getTime(current);
		FontRenderContext frc = g2.getFontRenderContext();
		Font f = new Font(null, Font.BOLD, h / 2);
		Rectangle timeStampSize = f.getStringBounds(totalStamp, frc)
				.getBounds();
		int offsetTop = (int) ((h / 2) + (timeStampSize.getHeight() / 3));
		int offsetLeft = (int) ((w - timeStampSize.getWidth()) - 5);
		g2.setFont(f);
		g2.setColor(Color.WHITE);
		g2.drawString(currentStamp, 4, offsetTop);
		g2.drawString(totalStamp, offsetLeft, offsetTop);

		g2.dispose();
		return comp;
	}

	public void setAlpha(int a) {
		alpha = a;
	}

	public void playToggleHover() {
		playIsHover = !playIsHover;
	}

	public void stopToggleHover() {
		stopIsHover = !stopIsHover;
	}

	public void soundToggleHover() {
		soundIsHover = !soundIsHover;
	}

	public boolean isPlayHover() {
		return playIsHover;
	}

	public boolean isStopHover() {
		return stopIsHover;
	}

	public boolean isSoundHover() {
		return soundIsHover;
	}

	public void changePlayState() {
		playState = !playState;
	}

	public void changeSoundState() {
		soundState = !soundState;
	}

	public boolean getSoundState() {
		return soundState;
	}

	public boolean getPlayState() {
		return playState;
	}

	public void setProgress(long mill) {
		current = mill;
		progress = (int) ((float) ((float) mill / (float) totalTime) * 100);
	}

	public void setProgress(int progress) {
		this.progress = Math.min(100, progress);
		current = (int) (((float) this.progress / 100f) * totalTime);
		System.out.println("progress was changed to: " + current);
	}

	public void setVolum(int v) {
		if (v < 3) {
			mute();
		} else {
			if (soundState == false) {
				soundState = true;
			}
			soundVolum = v;
		}
	}

	public void mute() {
		soundState = false;
		prevSoundVolume = soundVolum;
		soundVolum = 0;
	}

	public void unmute() {
		soundState = true;
		soundVolum = Math.max(prevSoundVolume, 10);
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public long getCurrent() {
		return current;
	}
}
