package ro.titus.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;

public class Main {

	public static void main(String[] args) {

		Mixer.Info[] inf = AudioSystem.getMixerInfo();
		Port p;
		try {
			p = (Port) AudioSystem.getLine(Port.Info.SPEAKER);
			System.out.println(p);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		DataLine.Info info = new DataLine.Info(TargetDataLine.class,
				getAudioFormat());
		System.out.println("dataline.info: " + info);
		TargetDataLine line = null;
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			System.out.println("line: " + line.getLineInfo());
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		Line.Info desired = new Line.Info(TargetDataLine.class);
		Line.Info[] lineInfo = AudioSystem.getTargetLineInfo(desired);

		for (int i = 0; i < lineInfo.length; ++i) {
			if (lineInfo[i] instanceof DataLine.Info) {
				System.out.println("Line: " + lineInfo[i]);
				AudioFormat[] forms = ((DataLine.Info) lineInfo[i]).getFormats();
				for (int n = 0; n < forms.length; ++n) {
					System.out.println("format: " + forms[n]);
				}
			}
		}
		for (int i = 0; i < inf.length; i++) {
			System.out.println("Mixer: " + inf[i]);
			Mixer m = AudioSystem.getMixer(inf[i]); 

			for (Line.Info lInf : m.getSourceLineInfo()) {
				System.out.println("     Line: " + lInf);
			}
			try {
				TargetDataLine ln = (TargetDataLine) m.getLine(info);
				System.out
						.println("             Mixer Target Data Line: " + ln);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

	}

	public static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}// end getAudioFormat
}
