package ro.titus.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import ro.titus.gui.KeyCombinationsManager;

public class Settings {

	public static volatile boolean macSystem = false;

	public static volatile String keysCombinationsFileName = "DescriptionsForKeyCombinations";
	public static volatile int singleKeyLoggingTime = 3000;
	public static volatile boolean singleKeyLoggingEnable = true;

	public static Image icon;

	public static volatile boolean macKeyboardBig = false;

	// public static File dir;

	public static volatile boolean logShiftPlusSingleLetter = false;

	public static volatile int keyboardButtonsFontSize = 17;
	public static final int defaultKeyWidth = 40;

	public static volatile boolean keyboardActive = true;
	public static volatile boolean mouseActive = true;
	public static volatile boolean loggerActive = true;

	public static volatile boolean registerNewKeyCombination = false;
	public static final Object registerNewKeyCombinationLock = new Object();

	public static final Object componentsStateLock = new Object();

	public static volatile boolean keepAspectRatio = true;
	public static volatile int refreshRate = 100;
	public static volatile int activeTime = 1000;

	public static volatile int fontSize = 10;
	public static volatile int fontSize2Rows = 10;
	public static volatile int fontSizeBigLable = 10;

	public static volatile Color background1Start = Color.decode("#636363");
	public static volatile Color background1End = Color.decode("#919191");
	public static volatile Color foreground1Start = Color.decode("#DEDEDE");
	public static volatile Color foreground1End = Color.decode("#FFFFFF");

	public static volatile Color background2Start = Color.decode("#1F6B00");
	public static volatile Color background2End = Color.decode("#247D00");
	public static volatile Color foreground2Start = Color.decode("#288C00");
	public static volatile Color foreground2End = Color.decode("#15FF00");

	public static volatile Color background3Start = Color.decode("#004DA6");
	public static volatile Color background3End = Color.decode("#004CA3");
	public static volatile Color foreground3Start = Color.decode("#0033FF");
	public static volatile Color foreground3End = Color.decode("#5C87FF");

	public static final HashMap<Integer, String> codeToKey = new HashMap<>();

	public static volatile Color textColor = Color.black;

	public static volatile boolean horizontalLogger = false;
	public static final Object horizontalLoggerLock = new Object();

	public static void changeTheme(int theme) {

		System.out.println("changeing theme: " + theme);

		if (theme == 0) {
			background1Start = Color.decode("#262626");
			background1End = Color.decode("#4D4D4D");
			foreground1Start = Color.decode("#000000");
			foreground1End = Color.decode("#4D4D4D");

			background2Start = Color.decode("#1F6B00");
			background2End = Color.decode("#247D00");
			foreground2Start = Color.decode("#288C00");
			foreground2End = Color.decode("#15FF00");

			background3Start = Color.decode("#004DA6");
			background3End = Color.decode("#004CA3");
			foreground3Start = Color.decode("#0033FF");
			foreground3End = Color.decode("#5C87FF");

			textColor = Color.white;
		} else {

			background1Start = Color.decode("#636363");
			background1End = Color.decode("#919191");
			foreground1Start = Color.decode("#DEDEDE");
			foreground1End = Color.decode("#FFFFFF");

			background2Start = Color.decode("#1F6B00");
			background2End = Color.decode("#247D00");
			foreground2Start = Color.decode("#288C00");
			foreground2End = Color.decode("#15FF00");

			background3Start = Color.decode("#004DA6");
			background3End = Color.decode("#004CA3");
			foreground3Start = Color.decode("#0033FF");
			foreground3End = Color.decode("#5C87FF");

			textColor = Color.black;
		}
	}

	public static void populateMap() {

		loadSettingFromFile(keysCombinationsFileName);
		synchronized (KeyCombinationsManager.keyCombinationMap) {
			if (KeyCombinationsManager.keyCombinationMap.size() == 0) {
				System.out.println("the descriptions map is empty");
				ArrayList<String> combination = new ArrayList<>();
				combination.add("CTRL");
				combination.add("C");
				KeyCombinationsManager.keyCombinationMap.put("Copy",
						new Object[] { combination, true });
				combination = new ArrayList<>();

				combination.add("CTRL");
				combination.add("Z");
				KeyCombinationsManager.keyCombinationMap.put("Undo",
						new Object[] { combination, true });
				combination = new ArrayList<>();

				combination.add("CTRL");
				combination.add("X");
				KeyCombinationsManager.keyCombinationMap.put("Cut",
						new Object[] { combination, true });
				combination = new ArrayList<>();

				combination.add("CTRL");
				combination.add("V");
				KeyCombinationsManager.keyCombinationMap.put("Paste",
						new Object[] { combination, true });
				combination = new ArrayList<>();

				combination.add("CTRL");
				combination.add("ALT");
				combination.add("DELETE");
				KeyCombinationsManager.keyCombinationMap.put("Task Manages",
						new Object[] { combination, true });
			}
		}
		/*
		 * for (Map.Entry<String, ArrayList<String>> entry :
		 * KeyCombinationsManager.keyCombinationMap .entrySet()) {
		 * System.out.println(entry); }
		 */

		codeToKey.put(0, "KEY_LOCATION_UNKNOWN");
		codeToKey.put(1, "KEY_LOCATION_STANDARD");
		codeToKey.put(2, "KEY_LOCATION_LEFT");
		codeToKey.put(3, "KEY_LOCATION_RIGHT");
		codeToKey.put(4, "KEY_LOCATION_NUMPAD");
		codeToKey.put(8, "BACKSPACE");

		codeToKey.put(12, "CLEAR");
		codeToKey.put(258, "CODE_INPUT");

		codeToKey.put(259, "JAPANESE_KATAKANA");

		codeToKey.put(256, "ALL_CANDIDATES");
		codeToKey.put(19, "PAUSE BREAK");
		codeToKey.put(257, "PREVIOUS_CANDIDATE");

		codeToKey.put(262, "KANA_LOCK");
		codeToKey.put(21, "KANA");
		codeToKey.put(263, "INPUT_METHOD_ON_OFF");
		codeToKey.put(20, "CAPSLOCK");
		codeToKey.put(260, "JAPANESE_HIRAGANA");
		codeToKey.put(261, "JAPANESE_ROMAN");
		codeToKey.put(25, "KANJI");
		codeToKey.put(24, "FINAL");
		codeToKey.put(27, "esc");
		codeToKey.put(29, "NONCONVERT");
		codeToKey.put(28, "CONVERT");
		codeToKey.put(31, "MODECHANGE");
		codeToKey.put(30, "ACCEPT");
		codeToKey.put(516, "EURO_SIGN");
		codeToKey.put(34, "PAGE DOWN");
		codeToKey.put(517, "EXCLAMATION_MARK");
		codeToKey.put(35, "END");
		codeToKey.put(518, "INVERTED_EXCLAMATION_MARK");
		codeToKey.put(32, "SPACE");
		codeToKey.put(65312, "COMPOSE");
		codeToKey.put(519, "LEFT_PARENTHESIS");
		codeToKey.put(33, "PAGE UP");
		codeToKey.put(512, "AT");
		codeToKey.put(513, "COLON");
		codeToKey.put(514, "CIRCUMFLEX");
		codeToKey.put(36, "HOME");
		codeToKey.put(515, "DOLLAR");
		codeToKey.put(524, "super");
		codeToKey.put(525, "meta");

		codeToKey.put(520, "NUMBER_SIGN");
		codeToKey.put(46, ".>");
		codeToKey.put(521, "PLUS");
		codeToKey.put(47, "/?");
		codeToKey.put(522, "RIGHT_PARENTHESIS");
		codeToKey.put(44, ",<");
		codeToKey.put(523, "VK_UNDERSCORE");
		codeToKey.put(45, "-_");
		codeToKey.put(51, "3#");
		codeToKey.put(50, "2@");
		codeToKey.put(49, "1!");
		codeToKey.put(48, "0)");
		codeToKey.put(55, "7&");
		codeToKey.put(54, "6^");
		codeToKey.put(53, "5%");
		codeToKey.put(52, "4$");
		codeToKey.put(59, ";:");
		codeToKey.put(57, "9(");
		codeToKey.put(56, "8*");
		codeToKey.put(61, "=+");
		codeToKey.put(68, "D");
		codeToKey.put(69, "E");
		codeToKey.put(70, "F");
		codeToKey.put(71, "G");
		codeToKey.put(65, "A");
		codeToKey.put(66, "B");
		codeToKey.put(67, "C");
		codeToKey.put(76, "L");
		codeToKey.put(77, "M");
		codeToKey.put(78, "N");
		codeToKey.put(79, "O");
		codeToKey.put(72, "H");
		codeToKey.put(73, "I");
		codeToKey.put(74, "J");
		codeToKey.put(75, "K");
		codeToKey.put(85, "U");
		codeToKey.put(84, "T");
		codeToKey.put(87, "W");
		codeToKey.put(86, "V");
		codeToKey.put(81, "Q");
		codeToKey.put(80, "P");
		codeToKey.put(83, "S");
		codeToKey.put(82, "R");
		codeToKey.put(93, "]}");
		codeToKey.put(92, "\\|");
		codeToKey.put(65368, "BEGIN");
		codeToKey.put(89, "Y");
		codeToKey.put(88, "X");
		codeToKey.put(91, "[{");
		codeToKey.put(90, "Z");
		codeToKey.put(102, "6 >");
		codeToKey.put(103, "7 HOME");
		codeToKey.put(100, "4 <");
		codeToKey.put(101, "5 5 5");
		codeToKey.put(98, "2 V");
		codeToKey.put(99, "3 PG DN");
		codeToKey.put(96, "0 INSERT");
		codeToKey.put(97, "1 END");
		codeToKey.put(110, ". DEL");
		codeToKey.put(111, "\\");
		codeToKey.put(108, "SEPARATOR");
		codeToKey.put(109, "-");
		codeToKey.put(106, "*");
		codeToKey.put(107, "+");
		codeToKey.put(104, "8 ^");
		codeToKey.put(105, "9 PG UP");
		codeToKey.put(119, "F8");
		codeToKey.put(118, "F7");
		codeToKey.put(117, "F6");
		codeToKey.put(116, "F5");
		codeToKey.put(115, "F4");
		codeToKey.put(114, "F3");
		codeToKey.put(113, "F2");
		codeToKey.put(112, "F1");
		codeToKey.put(65406, "ALT_GRAPH");
		codeToKey.put(127, "DELETE");
		codeToKey.put(123, "F12");
		codeToKey.put(122, "F11");
		codeToKey.put(121, "F10");
		codeToKey.put(120, "F9");
		codeToKey.put(137, "DEAD_DOUBLEACUTE");
		codeToKey.put(136, "DEAD_ABOVERING");
		codeToKey.put(139, "DEAD_CEDILLA");
		codeToKey.put(138, "DEAD_CARON");
		codeToKey.put(141, "DEAD_IOTA");
		codeToKey.put(140, "DEAD_OGONEK");
		codeToKey.put(143, "DEAD_SEMIVOICED_SOUND");
		codeToKey.put(142, "DEAD_VOICED_SOUND");
		codeToKey.put(129, "/?");
		codeToKey.put(402, "KEY_RELEASED");
		codeToKey.put(128, "DEAD_GRAVE");
		codeToKey.put(131, "DEAD_TILDE");
		codeToKey.put(400, "KEY_TYPED");
		codeToKey.put(130, "DEAD_CIRCUMFLEX");
		codeToKey.put(401, "KEY_PRESSED");
		codeToKey.put(133, "DEAD_BREVE");
		codeToKey.put(132, "DEAD_MACRON");
		codeToKey.put(135, "DEAD_DIAERESIS");
		codeToKey.put(134, "DEAD_ABOVEDOT");
		codeToKey.put(152, "QUOTEDBL");
		codeToKey.put(153, "LESS");
		codeToKey.put(154, "Prt Scm Sys Req");
		codeToKey.put(155, "INSERT");
		codeToKey.put(156, "HELP");
		codeToKey.put(157, "META");
		codeToKey.put(144, "NUM LOCK");
		codeToKey.put(145, "SCROLL LOCK");
		codeToKey.put(150, "AMPERSAND");
		codeToKey.put(151, "ASTERISK");
		codeToKey.put(162, "BRACERIGHT");
		codeToKey.put(161, "BRACELEFT");
		codeToKey.put(160, "GREATER");
		codeToKey.put(65485, "COPY");
		codeToKey.put(65487, "PASTE");
		codeToKey.put(65480, "STOP");
		codeToKey.put(65481, "AGAIN");
		codeToKey.put(65482, "PROPS");
		codeToKey.put(65483, "UNDO");
		codeToKey.put(192, "`~");
		codeToKey.put(222, "'\"");
		codeToKey.put(65489, "CUT");
		codeToKey.put(65488, "FIND");
		codeToKey.put(61451, "F24");
		codeToKey.put(61450, "F23");
		codeToKey.put(61449, "F22");
		codeToKey.put(61448, "F21");
		codeToKey.put(61447, "F20");
		codeToKey.put(61446, "F19");
		codeToKey.put(61445, "F18");
		codeToKey.put(61444, "F17");
		codeToKey.put(227, "KP_RIGHT");
		codeToKey.put(61443, "F16");
		codeToKey.put(226, "KP_LEFT");
		codeToKey.put(61442, "F15");
		codeToKey.put(225, "KP_DOWN");
		codeToKey.put(61441, "F14");
		codeToKey.put(224, "KP_UP");
		codeToKey.put(61440, "F13");
		codeToKey.put(65535, "CHAR_UNDEFINED");
		codeToKey.put(244, "HALF_WIDTH");
		codeToKey.put(245, "ROMAN_CHARACTERS");
		codeToKey.put(242, "HIRAGANA");
		codeToKey.put(243, "FULL_WIDTH");
		codeToKey.put(240, "ALPHANUMERIC");
		codeToKey.put(241, "KATAKANA");

		if (macSystem) {
			codeToKey.put(17, "control");
			codeToKey.put(16, "shift");
			codeToKey.put(18, "option");
			codeToKey.put(524, "command");
			codeToKey.put(20, "caps lock");
			codeToKey.put(8, "delete");
			codeToKey.put(9, "tab");
			codeToKey.put(10, "return");
			codeToKey.put(38, "up");
			codeToKey.put(39, "right");
			codeToKey.put(37, "left");
			codeToKey.put(40, "down");
		} else {
			codeToKey.put(17, "CTRL");
			codeToKey.put(16, "SHIFT");
			codeToKey.put(18, "ALT");
			codeToKey.put(524, "super");
			codeToKey.put(20, "CAPSLOCK");
			codeToKey.put(8, "BACKSPACE");
			codeToKey.put(9, "TAB");
			codeToKey.put(10, "ENTER");
			codeToKey.put(38, "^");
			codeToKey.put(39, ">");
			codeToKey.put(37, "<");
			codeToKey.put(40, "V1");
		}

	}

	public static Dimension getDimensions(int totalWidth, int totalHeight,
			int defaultWidth, int defaultHeight) {
		double ratio;
		boolean horizontal = false;

		int resultWidth = 1, resultHeight = 1;

		if (defaultWidth > defaultHeight) {
			horizontal = true;
			ratio = (double) ((double) defaultWidth / (double) defaultHeight);
		} else {
			horizontal = false;
			ratio = (double) ((double) defaultHeight / (double) defaultWidth);
		}

		if (horizontal) {
			resultWidth = totalWidth;
			resultHeight = (int) ((double) totalWidth / ratio);
			if (resultHeight > totalHeight) {
				resultHeight = totalHeight;
				resultWidth = (int) ((double) totalHeight * ratio);
			}
		} else {
			resultWidth = (int) ((double) totalHeight / ratio);
			resultHeight = totalHeight;
			if (resultWidth > totalWidth) {
				resultWidth = totalWidth;
				resultHeight = (int) ((double) resultWidth * ratio);
			}
		}

		/*
		 * System.out.println("default dim: " + defaultWidth + "x" +
		 * defaultHeight + " current size: " + totalWidth + "x" + totalHeight +
		 * " ratio " + ratio + " result: " + resultWidth + "x" + resultHeight);
		 */

		return new Dimension(resultWidth, resultHeight);

	}

	@SuppressWarnings("unchecked")
	public static void loadSettingFromFile(String fileName) {
		File dir = new File("Settings");
		if (!dir.exists()) {
			dir.mkdir();
		}
		keysCombinationsFileName = fileName;
		File file = new File(dir, fileName);
		System.out.println("loading object: " + file.getAbsolutePath());
		if (file.exists()) {
			try (FileInputStream fis = new FileInputStream(file)) {
				try (ObjectInputStream ois = new ObjectInputStream(fis)) {

					HashMap<String, Object[]> map = (HashMap<String, Object[]>) ois
							.readObject();

					synchronized (KeyCombinationsManager.keyCombinationMap) {
						KeyCombinationsManager.keyCombinationMap.putAll(map);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (KeyCombinationsManager.keyCombinationMap == null) {
				KeyCombinationsManager.keyCombinationMap = new HashMap<>();
			}
		}
	}

	public static void saveSettings() {

		File dir = new File("Settings");
		if (!dir.exists()) {
			dir.mkdir();
		}

		try (FileOutputStream fos = new FileOutputStream(new File(dir,
				keysCombinationsFileName))) {
			try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				synchronized (KeyCombinationsManager.keyCombinationMap) {
					oos.writeObject(KeyCombinationsManager.keyCombinationMap);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int[] getKeyCombinationSize(ArrayList<String> keys,
			String text) {
		int width = 0;
		int height = 0;
		int textWidth = 0;
		for (String key : keys) {
			width += detButtonsSize(key);
		}

		if (Settings.horizontalLogger) {
			height = (int) (Settings.defaultKeyWidth * 1.5);
		} else {
			height = Settings.defaultKeyWidth;
		}

		width += (keys.size() - 1) * 12;
		BufferedImage img = new BufferedImage(100, 100,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		Font f = new Font(null, Font.BOLD,
				(int) (keyboardButtonsFontSize * 0.7));
		FontRenderContext frc = g2.getFontRenderContext();
		g2.dispose();
		img = null;
		Rectangle2D tSize = f.getStringBounds((String) text, frc);
		textWidth = (int) tSize.getWidth();
		return new int[] { width, height, textWidth };
	}

	public static int detButtonsSize(String key) {
		int width = 0;
		if (key.equalsIgnoreCase("ctrl") || key.equalsIgnoreCase("command")
				|| key.equalsIgnoreCase("shift") || key.equalsIgnoreCase("tab")
				|| key.equalsIgnoreCase("capslock")
				|| key.equalsIgnoreCase("caps lock")
				|| key.equalsIgnoreCase("option")
				|| key.equalsIgnoreCase("control")
				|| key.equalsIgnoreCase("backspace")
				|| key.equalsIgnoreCase("return")
				|| key.equalsIgnoreCase("enter")) {
			width = (Settings.defaultKeyWidth * 2);
		} else if (key.equalsIgnoreCase("space")) {
			width = (Settings.defaultKeyWidth * 3);
		} else {
			width = Settings.defaultKeyWidth;
		}
		return width;
	}

	public static void setIcon() {
		try {
			icon = ImageIO.read(new File("catchon.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void setDir() { dir = dir(); if (new File(dir,
	 * "VLCPortable" + File.separator + "App" + File.separator + "vlc" +
	 * File.separator + "libvlccore.dll") .exists()) {
	 * System.out.println("dir set using method 1"); return; } else { dir =
	 * dir2(); } if (new File(dir, "VLCPortable" + File.separator + "App" +
	 * File.separator + "vlc" + File.separator + "libvlccore.dll") .exists()) {
	 * System.out.println("dir set using method 1"); return; } else { dir =
	 * dir3(); } if (new File(dir, "VLCPortable" + File.separator + "App" +
	 * File.separator + "vlc" + File.separator + "libvlccore.dll") .exists()) {
	 * System.out.println("dir set using method 1"); return; } else { dir =
	 * dir4(); } if (new File(dir, "VLCPortable" + File.separator + "App" +
	 * File.separator + "vlc" + File.separator + "libvlccore.dll") .exists()) {
	 * System.out.println("dir set using method 1"); return; } else { dir =
	 * dir5(); } if (new File(dir, "VLCPortable" + File.separator + "App" +
	 * File.separator + "vlc" + File.separator + "libvlccore.dll") .exists()) {
	 * System.out.println("dir set using method 1"); return; } else { dir =
	 * dir6(); } if (new File(dir, "VLCPortable" + File.separator + "App" +
	 * File.separator + "vlc" + File.separator + "libvlccore.dll") .exists()) {
	 * System.out.println("dir set using method 1"); return; } else { dir =
	 * dir7(); } if (new File(dir, "VLCPortable" + File.separator + "App" +
	 * File.separator + "vlc" + File.separator + "libvlccore.dll") .exists()) {
	 * System.out.println("dir set using method 1"); return; } else {
	 * System.out.println("unable to set base directory"); dir = new File(".");
	 * }
	 * 
	 * }
	 * 
	 * public static File dir() { return new
	 * File(Entrance.class.getProtectionDomain().getCodeSource()
	 * .getLocation().getPath()); }
	 * 
	 * public static File dir2() { return new File("."); }
	 * 
	 * public static File dir3() { return new File(".."); }
	 * 
	 * public static File dir4() { return new File("/"); }
	 * 
	 * public static File dir5() { return new File("./"); }
	 * 
	 * public static File dir6() { return new File(File.separator); }
	 * 
	 * public static File dir7() { File f = new
	 * File(System.getProperty("java.class.path")); File dir =
	 * f.getAbsoluteFile().getParentFile(); return dir; }
	 */

}
