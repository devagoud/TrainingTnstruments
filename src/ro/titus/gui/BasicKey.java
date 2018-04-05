package ro.titus.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import ro.titus.tools.Settings;

public class BasicKey extends BufferedImage {

	Color gEnd = Settings.background1End;
	Color gStart = Settings.background1Start;
	Color frontS = Settings.foreground1Start;
	Color frontE = Settings.foreground1End;

	int state = 0;
	Object lable = "x";

	int w;
	int h;
	int x;
	int y;
	static int theme = 0;

	double offserH = 0;
	double offserV = 0;

	static int defMargin = 5;
	static int corner = 10;
	int fontSize = Settings.keyboardButtonsFontSize;

	public BasicKey(int width, int height, Object l, int x, int y, int state,
			int fontSize) {
		super(width, height, TYPE_INT_ARGB);
		this.fontSize = fontSize;
		setPosition(x, y);
		init(width, height, l, state);
	}

	private void paintBack(Graphics2D g2) {
		GradientPaint grad = new GradientPaint(0, h, gEnd, w, 0, gStart);
		RoundRectangle2D back = new RoundRectangle2D.Double(0, 0, w, h, corner,
				corner);
		g2.setPaint(grad);
		g2.fill(back);
	}

	private void paintMiddle(Graphics2D g2) {

		double middM = (defMargin - (defMargin * 0.1));

		double offsetX = middM / 2;
		double offsetY = middM / 2;

		RoundRectangle2D back = new RoundRectangle2D.Double(offsetX, offsetY, w
				- middM, h - middM, (corner * 1.5), (corner * 0.5));
		g2.setPaint(gEnd);
		g2.fill(back);
	}

	private void paintFront(Graphics2D g2) {

		offserH = defMargin / 3;
		offserV = defMargin / 3;

		RoundRectangle2D back = new RoundRectangle2D.Double(offserH, offserV, w
				- defMargin, h - defMargin, corner, corner);

		GradientPaint p = new GradientPaint(0, 0, frontS, w / 2, 0, frontE,
				true);
		g2.setPaint(p);
		g2.fill(back);
	}

	private void paintLable(Graphics2D g2) {
		offserH = defMargin / 3;
		offserV = defMargin / 3;

		if ((lable instanceof String && lable.equals("Enter2"))) {
			Font font = new Font(null, Font.BOLD, (int)(fontSize/1.3));
			FontRenderContext frc = g2.getFontRenderContext();
			Rectangle2D tSize = font.getStringBounds("Enter",
					frc);
			g2.setFont(font);
			g2.setColor(Settings.textColor);
			g2.drawString("Enter", (int) (defMargin * 1.4),
					(int) (tSize.getHeight() + (defMargin / 2)));
		} else if (lable instanceof String && !lable.equals("super")
				&& !lable.equals("super2")) {

			String l = (String) lable;

			if (l.matches("[A-Z][a-z]+\\d")) {
				l = l.replaceAll("\\d", "");
			}

			if (l.length() == 2 && !l.startsWith("F") && !l.equals("8^")
					&& !l.equals("V1")) {

				// draw the button label on 2 rows
				String firstRow = l.substring(1, 2);
				String secondRow = l.substring(0, 1);

				Font font = new Font(null, Font.BOLD, fontSize);
				FontRenderContext frc = g2.getFontRenderContext();
				Rectangle2D tSize = font.getStringBounds((String) lable, frc);
				int offsetLeft = (int) (defMargin * 1.4);
				int offsetTop = (int) (tSize.getHeight() + (defMargin / 2));

				g2.setColor(Settings.textColor);
				g2.setFont(font);
				g2.drawString(firstRow, offsetLeft, offsetTop);

				g2.drawString(secondRow, offsetLeft,
						(int) (offsetTop + tSize.getHeight() + 2));

			} else if (l.split(" ").length >= 2 || l.equals("Insert")
					|| l.equals("Home") || l.equals("Delete")
					|| l.equals("End")) {

				String firstRow = "";
				String secondRow = "";
				if (l.split(" ").length == 2) {
					firstRow = l.split(" ")[0];
					secondRow = l.split(" ")[1];
				} else if (l.equals("Prt Scm Sys Req")) {
					// draw the button label on 2 rows
					firstRow = "Prt Scm";
					secondRow = "Sys Req";
				} else if (l.equals("Page Up")) {
					firstRow = "Page";
					secondRow = "Up";
				} else if (l.equals("Page Down")) {
					firstRow = "Page";
					secondRow = "Down";
				} else if (l.equals("Scroll Lock")) {
					firstRow = "Scroll";
					secondRow = "Lock";
				} else if (l.equals("Pause Break")) {
					firstRow = "Pause";
					secondRow = "Break";
				} else if (l.equals("Insert") || l.equals("Home")
						|| l.equals("Delete") || l.equals("End")) {
					firstRow = l;
					secondRow = "";
				} else if (l.equals("9 Pg Up")) {
					firstRow = "9";
					secondRow = "Pg Up";
				} else if (l.equals("3 Pg Dn")) {
					firstRow = "3";
					secondRow = "Pg Dn";
				} else if (l.equals("5 5 5")) {
					firstRow = "5";
					secondRow = "";
				}

				Font font = new Font(null, Font.BOLD, (int) (fontSize / 1.4));
				FontRenderContext frc = g2.getFontRenderContext();
				Rectangle2D tSize = font.getStringBounds((String) lable, frc);
				int offsetLeft = (int) (defMargin * 1.4);
				int offsetTop = (int) (tSize.getHeight() + (defMargin / 2));

				g2.setColor(Settings.textColor);
				g2.setFont(font);
				g2.drawString(firstRow, offsetLeft, offsetTop);
				if (secondRow.equals("<") || secondRow.equals(">")
						|| secondRow.equals("^") || secondRow.equals("V")) {

					drawArrow(g2, getRotation(secondRow), fontSize, offsetLeft,
							(int) (offsetTop + tSize.getHeight() + 2));
				} else {
					g2.drawString(secondRow, offsetLeft, (int) (offsetTop
							+ tSize.getHeight() + 2));
				}
			} else if (l.equals("<") || l.equals(">") || l.equals("V1")
					|| l.equals("^")) {

				drawArrow(g2, getRotation(l), fontSize,
						(w - defMargin - fontSize) / 2,
						(h - defMargin - fontSize) / 2);
			} else if (l.equals("meta")) {
				drawMeta(g2, (Math.min(w, h) - defMargin) / 2);
			} else {

				if (Settings.macSystem
						&& (l.toLowerCase().equals("ctrl")
								|| l.toLowerCase().equals("alt") || l
								.toLowerCase().equals("enter"))) {

					if (l.toLowerCase().equals("ctrl")) {
						l = "Control";
					} else if (l.toLowerCase().equals("alt")) {
						l = "Option";
					} else if (l.toLowerCase().equals("enter")) {
						l = "Return";
					}

					Font font = new Font(null, Font.BOLD,
							(int) (fontSize * 0.9));
					FontRenderContext frc = g2.getFontRenderContext();
					Rectangle2D tSize = font.getStringBounds((String) lable,
							frc);

					g2.setFont(font);
					g2.setColor(Settings.textColor);
					g2.drawString(l, (int) (defMargin * 1.4),
							(int) (tSize.getHeight() + (defMargin / 2)));

				} else {

					Font font = new Font(null, Font.BOLD, fontSize);
					FontRenderContext frc = g2.getFontRenderContext();
					Rectangle2D tSize = font.getStringBounds((String) lable,
							frc);

					g2.setFont(font);
					g2.setColor(Settings.textColor);
					g2.drawString(l, (int) (defMargin * 1.4),
							(int) (tSize.getHeight() + (defMargin / 2)));
				}
			}
		} else {

			if (Settings.macSystem) {
				Font font = new Font(null, Font.BOLD, (int) (fontSize * 0.6));
				FontRenderContext frc = g2.getFontRenderContext();
				Rectangle2D tSize = font.getStringBounds((String) lable, frc);

				g2.setFont(font);
				g2.setColor(Settings.textColor);
				g2.drawString("Command", (int) (defMargin * 1.4),
						(int) (tSize.getHeight() + (defMargin / 2)));

			} else {
				drawWindowsIcon(g2);
			}
		}
	}

	private void paintLableMac(Graphics2D g2) {

		FontRenderContext frc = g2.getFontRenderContext();
		Font font;
		Rectangle2D tSize;
		int offsetLeft;
		int offsetTop;
		g2.setColor(Settings.textColor);

		if (lable instanceof String) {

			if (((String) lable).equals("delete2")
					|| ((String) lable).equals("home")
					|| ((String) lable).equals("clear")
					|| ((String) lable).equals("end")
					|| ((String) lable).equals("fn")) {
				String str = ((String) lable);
				str = str.replaceAll("\\d+", "");
				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds(str, frc);
				offsetLeft = (int) ((w - tSize.getWidth() - defMargin) / 2);
				offsetTop = (int) ((((h) - tSize.getHeight()) / 2) + tSize
						.getHeight() / 2);
				g2.setFont(font);
				g2.drawString(str, offsetLeft, offsetTop);
			} else if (((String) lable).contains("page")) {
				String top = "page";
				String bottom = (((String) lable).contains("down") ? "down"
						: "up");

				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds(top, frc);
				offsetLeft = (int) ((w - tSize.getWidth() - defMargin) / 2);
				offsetTop = (int) (tSize.getHeight() + (tSize.getHeight() / 2));

				g2.setFont(font);
				g2.drawString(top, offsetLeft, offsetTop);
				g2.drawString(bottom, offsetLeft,
						(int) (offsetTop + tSize.getHeight()));
			} else if (((String) lable).equals("enter")) {
				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds("enter", frc);
				offsetLeft = (int) (2);
				offsetTop = (int) ((((h) - tSize.getHeight())));
				g2.setFont(font);
				g2.drawString("enter", offsetLeft, offsetTop);
			} else if (((String) lable).length() == 1
					|| ((String) lable).equals("esc")) {
				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds((String) lable, frc);
				offsetLeft = (int) ((w - tSize.getWidth() - defMargin) / 2);
				offsetTop = (int) ((((h) - tSize.getHeight()) / 2) + tSize
						.getHeight() / 2);
				g2.setFont(font);
				g2.drawString((String) lable, offsetLeft, offsetTop);
			} else if (((String) lable).startsWith("F")) {
				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds((String) lable, frc);
				offsetLeft = (int) ((w - tSize.getWidth() - defMargin));
				offsetTop = (int) ((((h) - tSize.getHeight())) + (tSize
						.getHeight() / 2));
				g2.setFont(font);
				g2.drawString((String) lable, offsetLeft, offsetTop);

				int dim = Math.min(w, h);

				int iconX = (int) ((dim - (dim - tSize.getHeight()) / 1.8) / 2);
				int iconY = (int) ((dim - (dim - tSize.getHeight()) / 1.8) / 2);
				int iconWidth = (int) ((dim - tSize.getHeight()) / 1.8);
				int iconHieght = (int) ((dim - tSize.getHeight()) / 1.8);

				if (lable.equals("F1")) {
					f1Icon(g2, iconX, iconY, iconWidth, iconHieght);
				} else if (lable.equals("F2")) {
					f2Icon(g2, iconX, iconY, iconWidth, iconHieght);
				} else if (lable.equals("F3")) {
					// f3Icon(g2, iconX, iconY, iconWidth, iconHieght);
					f3Icon(g2, iconX, iconY, iconWidth,
							(int) (iconHieght / 1.3));
				} else if (lable.equals("F4")) {
					f4Icon(g2, iconX, iconY, iconWidth, iconHieght);
				} else if (lable.equals("F5")) {
					f5Icon(g2, iconX, iconY + iconHieght / 4, iconWidth,
							iconHieght);
				} else if (lable.equals("F6")) {
					f6Icon(g2, iconX, iconY + iconHieght / 6, iconWidth,
							iconHieght);
				} else if (lable.equals("F7")) {
					f7Icon(g2, iconX, iconY, iconWidth, iconHieght);
				} else if (lable.equals("F8")) {
					f8Icon(g2, iconX, iconY, iconWidth, iconHieght);
				} else if (lable.equals("F9")) {
					f9Icon(g2, iconX, iconY, iconWidth, iconHieght);
				} else if (lable.equals("F10")) {
					f10Icon(g2, iconX + (iconWidth / 4), iconY, iconWidth,
							iconHieght);
				} else if (lable.equals("F11")) {
					f11Icon(g2, iconX + (iconWidth / 10), iconY, iconWidth,
							iconHieght);
				} else if (lable.equals("F12")) {
					f12Icon(g2, iconX, iconY, iconWidth, iconHieght);
				}

			} else if (((String) lable).length() == 2
					&& !((String) lable).equals("up")
					&& !((String) lable).equals("fn")) {
				String top = ((String) lable).substring(1);
				String bottom = ((String) lable).substring(0, 1);

				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds(top, frc);
				offsetLeft = (int) ((w - tSize.getWidth() - defMargin) / 2);
				offsetTop = (int) (tSize.getHeight() + (tSize.getHeight() / 2));

				g2.setFont(font);
				g2.drawString(top, offsetLeft, offsetTop);
				g2.drawString(bottom, offsetLeft,
						(int) (offsetTop + tSize.getHeight()));
			} else if (((String) lable).contains("command")) {
				String labelStr = ((String) lable).replace("2", "");
				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds(labelStr, frc);
				offsetLeft = (int) (2);
				offsetTop = (int) ((((h) - tSize.getHeight())));
				g2.setFont(font);
				g2.drawString(labelStr, offsetLeft, offsetTop);
				if (((String) lable).contains("2")) {
					drawCommandIcon(g2, w / 12, ((w / 5)), ((w / 12)) + 2);
				} else {
					drawCommandIcon(g2, w / 12, w - ((w / 5)) - defMargin,
							((w / 12)) + 2);
				}
			} else if (((String) lable).contains("option")
					|| ((String) lable).equals("return")) {
				String top = "alt";
				String bottom = "option";

				if (((String) lable).equals("return")) {
					top = "enter";
					bottom = "return";
				}

				font = new Font(null, Font.BOLD, (int) (fontSize * 0.9));
				tSize = font.getStringBounds(top, frc);
				if (((String) lable).equals("option2")) {
					offsetLeft = (int) (w - tSize.getWidth() - defMargin);
				} else {
					offsetLeft = (int) (2);
				}
				offsetTop = (int) (tSize.getHeight() + (tSize.getHeight() / 4));
				g2.setFont(font);
				g2.drawString(top, offsetLeft, offsetTop);

				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds(bottom, frc);
				offsetTop = (int) ((((h) - tSize.getHeight())));

				if (((String) lable).equals("option2")) {
					offsetLeft = (int) (w - tSize.getWidth() - defMargin);
				}

				g2.setFont(font);
				g2.drawString(bottom, offsetLeft, offsetTop);

			} else if (((String) lable).equals("up")
					|| (((String) lable).equals("down"))
					|| (((String) lable).equals("left"))
					|| (((String) lable).equals("right"))) {
				offsetTop = (h - fontSize) / 2;
				offsetLeft = (w - defMargin - fontSize) / 2;
				drawArrow(g2, getRotation(((String) lable)), fontSize,
						offsetLeft, offsetTop);
			} else if (((String) lable).equals("tab")
					|| (((String) lable).contains("shift"))
					|| (((String) lable).equals("delete"))
					|| (((String) lable).startsWith("control"))
					|| (((String) lable).equals("fn"))) {

				String labelStr = ((String) lable).replace("2", "");

				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds(labelStr, frc);
				if (((String) lable).contains("2")) {
					offsetLeft = (int) (w - defMargin - tSize.getWidth());
				} else {
					offsetLeft = (int) (2);
				}
				offsetTop = (int) ((((h) - tSize.getHeight())));
				g2.setFont(font);
				g2.drawString(labelStr, offsetLeft, offsetTop);
			} else if (((String) lable).equals("caps lock")) {
				font = new Font(null, Font.BOLD, (int) (fontSize));
				tSize = font.getStringBounds(((String) lable), frc);
				offsetLeft = (int) (2);
				offsetTop = (int) ((((h) - tSize.getHeight())));
				g2.setFont(font);
				g2.drawString(((String) lable), offsetLeft, offsetTop);
				g2.fillOval(fontSize / 2, fontSize / 2, fontSize / 2,
						fontSize / 2);
			} else if (((String) lable).equals("eject")) {

				offsetLeft = ((w - fontSize) / 2) - defMargin;
				offsetTop = (int) (fontSize * 2.5);
				g2.fillRect(offsetLeft, offsetTop, fontSize * 2, fontSize / 2);
				Path2D path = new Path2D.Double();
				path.moveTo(offsetLeft, offsetTop - 1);
				path.lineTo(offsetLeft + fontSize * 2, offsetTop - 1);
				path.lineTo(offsetLeft + fontSize, (offsetTop - 1) - fontSize);
				path.lineTo(offsetLeft, offsetTop - 1);
				g2.fill(path);
			}
		}

	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getXX() {
		return x;
	}

	public int getYY() {
		return y;
	}

	public void changeState(int state) {

		if (state != 0 && this.state == state) {
			return;
		}
		this.state = state;
		if (state == 0) {
			gStart = Settings.background1Start;
			gEnd = Settings.background1End;
			frontS = Settings.foreground1Start;
			frontE = Settings.foreground1End;
		} else if (state == 1) {
			gStart = Settings.background2Start;
			gEnd = Settings.background2End;
			frontS = Settings.foreground2Start;
			frontE = Settings.foreground2End;
		} else if (state == 2) {
			gStart = Settings.background3Start;
			gEnd = Settings.background3End;
			frontS = Settings.foreground3Start;
			frontE = Settings.foreground3End;

		}
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		paintBack(g2);
		paintMiddle(g2);
		paintFront(g2);
		if (Settings.macSystem) {
			paintLableMac(g2);
		} else {
			paintLable(g2);
		}

		g2.dispose();
	}

	public int getStatus() {
		return state;
	}

	public static void setTheme(int t) {
		if (theme == t) {
			return;
		} else {
			theme = t;
		}
	}

	private void init(int width, int height, Object l, int state) {
		w = width;
		h = height;
		lable = l;
		defMargin = 5;
		corner = 10;

		if (w < 50) {
			defMargin = 5;
			corner = 3;
		}

		if (h > 100) {
			defMargin = 10;
			corner = 20;
		}

		if (h > 150) {
			defMargin = 20;
			corner = 30;
		}

		if (h > 300) {
			defMargin = 30;
			corner = 40;
		}

		changeState(state);
	}

	public void changeSize(int width, int height) {
		init(width, height, lable, state);
	}

	public Object getLable() {
		return lable;
	}

	private void drawWindowsIcon(Graphics2D g2) {

		double dim = ((((h - offserH) / 4d) / 3d) * 4d)
				+ (((((h - offserH) / 4d) / 3d) * 4d) / 5d);
		double x = (w - dim - defMargin) / 2d;
		double y = (h - dim - defMargin) / 2d;

		Shape s1 = getIconPrototipe2(x, y, true);
		double space = s1.getBounds().getWidth() / 10d;
		Shape s2 = getIconPrototipe1(s1.getBounds().getMaxX() + space, s1
				.getBounds().getY(), true);

		Shape s3 = getIconPrototipe2(x, s1.getBounds().getMaxY() + space, false);
		Shape s4 = getIconPrototipe1(s3.getBounds().getMaxX() + space, s2
				.getBounds().getMaxY() + space, false);

		GradientPaint grad = new GradientPaint(0, 0, Color.decode("#FF0012"),
				(int) s1.getBounds().getMaxX(), 0, Color.decode("#FFA8AE"));

		g2.setPaint(grad);
		g2.fill(s1);
		grad = new GradientPaint(0, 0, Color.decode("#26FF00"), (int) s2
				.getBounds().getMaxX(), 0, Color.decode("#B3FFA6"));
		g2.setPaint(grad);
		g2.fill(s2);
		grad = new GradientPaint(0, 0, Color.decode("#0059FF"), (int) s3
				.getBounds().getMaxX(), 0, Color.decode("#ADCAFF"));
		g2.setPaint(grad);
		g2.fill(s3);
		grad = new GradientPaint(0, 0, Color.decode("#FFFB00"), (int) s4
				.getBounds().getMaxX(), 0, Color.decode("#FFFEA8"));
		g2.setPaint(grad);
		g2.fill(s4);

	}

	private Path2D getIconPrototipe1(double x, double y, boolean first) {

		double dim = ((h - offserH) / 4) / 3;
		if (first) {
			y += dim;
		} else {
			y -= dim;

		}

		Path2D.Double path = new Path2D.Double();

		CubicCurve2D curve = new CubicCurve2D.Float();
		curve.setCurve(x, y, (x += dim), (y += dim), (x += dim), y, (x += dim),
				(y -= dim));
		path.append(curve, false);
		path.lineTo(x -= (dim / 2), ((y += dim * 2)));
		curve = new CubicCurve2D.Float();
		curve.setCurve(x, y, (x -= dim), (y += dim), (x -= dim), y, (x -= dim),
				(y -= dim));
		path.append(curve, true);
		path.lineTo(x, y);
		path.clone();
		return path;
	}

	private Path2D getIconPrototipe2(double x, double y, boolean first) {

		double dim = ((h - offserH) / 4) / 3;
		if (!first) {
			x -= (dim / 2);
		}

		Path2D.Double path = new Path2D.Double();

		CubicCurve2D curve = new CubicCurve2D.Float();
		curve.setCurve(x, y, (x += dim), (y -= dim), (x += dim), y, (x += dim),
				(y += dim));
		path.append(curve, false);
		path.lineTo(x -= (dim / 2), (y += dim * 2));// y is 40 smaller
		curve = new CubicCurve2D.Float();
		curve.setCurve(x, y, (x -= dim), (y -= dim), (x -= dim), y, (x -= dim),
				(y += dim));
		path.append(curve, true);
		path.lineTo(x, y);
		path.clone();
		return path;
	}

	private void drawArrow(Graphics2D g2, int rotation, double size, double x,
			double y) {
		Path2D.Double path = new Path2D.Double();

		/*
		 * System.out.println("drawing arrow: " + rotation + " size: " + size +
		 * " x: " + x + " y: " + y);
		 */

		y += size / 2;// is half down

		path.moveTo(x, y);// x is at 0 y is at half down
		path.lineTo(x + size, y - size / 2);// no change here
		path.moveTo(x, y);// no change here
		path.lineTo((x += size), (y += size / 2));// x is all the way left y
													// is all the way down
		CubicCurve2D curve = new CubicCurve2D.Float();
		curve.setCurve(x, y, (x -= size / 3), y -= size / 3, x,
				(y -= size / 3), (x += size / 3), y -= size / 3);
		path.append(curve, true);
		path.closePath();
		Shape s = path;
		if (rotation != 0) {
			AffineTransform tr = new AffineTransform();
			if (rotation == 1) {
				tr.rotate(45, s.getBounds().getCenterX(), s.getBounds()
						.getCenterY());
			} else {
				tr.quadrantRotate(rotation, s.getBounds().getCenterX(), s
						.getBounds().getCenterY());
			}
			s = tr.createTransformedShape(s);
		}

		g2.setPaint(Settings.textColor);
		g2.fill(s);

	}

	private void drawMeta(Graphics2D g2, double size) {

		double x = (w - size - defMargin) / 2;
		double y = (h - size / 2 - defMargin) / 2;

		Path2D.Double path = new Path2D.Double();

		for (int i = 0; i < 4; i++) {
			path.moveTo(x, y);
			path.lineTo(x + size, y);
			path.lineTo(x + size, y - (size / 4));
			path.lineTo(x, y - (size / 4));
			path.lineTo(x, y);
			y += (size / 4);
		}

		path.closePath();
		g2.setColor(Color.GRAY);
		g2.draw(path);
		path = new Path2D.Double();
		y = (h - size - defMargin) / 2;
		y += (size / 4);
		path.moveTo(x, y);
		path.lineTo(x + size, y);
		path.lineTo(x + size, y - (size / 4));
		path.lineTo(x, y - (size / 4));
		path.lineTo(x, y);
		path.closePath();
		g2.fill(path);

		drawArrow(g2, 1, size / 2, x + size / 2, y);

	}

	private int getRotation(String str) {
		int type = 0;
		if (str.equals("^") || str.equals("up")) {
			type = 45;
		}
		if (str.equals(">") || str.equals("right")) {
			type = 90;
		}
		if (str.contains("V") || str.equals("down")) {
			type = -45;
		}
		return type;
	}

	private void drawCommandIcon(Graphics2D g2, int size, int x, int y) {

		g2.drawRect(x, y, size, size);
		g2.drawOval(x - size, y - size, size, size);
		g2.drawOval(x + size, y - size, size, size);
		g2.drawOval(x - size, y + size, size, size);
		g2.drawOval(x + size, y + size, size, size);

	}

	private void f1Icon(Graphics2D g2, int x, int y, int width, int height) {

		System.out.println("F1 x: " + x + " y: " + y + " width: " + width
				+ " height: " + height);

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);

		int ovalWidth = (int) ((width / 2));
		int ovalHeight = (int) ((height / 2));

		int ovalX = (int) ((width / 4));
		int ovalY = (int) ((height / 4));

		g2.setTransform(tr);

		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));
		g2.drawOval(ovalX, ovalY, ovalWidth, ovalHeight);

		Shape s = new Line2D.Double(((width / 2)), 0, ((width / 2)),
				((height / 8)));

		g2.draw(s);
		s = new Line2D.Double(((width / 2)), ((height - (height / 8))),
				((width / 2)), height);
		g2.draw(s);
		s = new Line2D.Double(0, height / 2, ((width / 8)), height / 2);
		g2.draw(s);
		s = new Line2D.Double(((width - (width / 8))), height / 2, width,
				height / 2);
		g2.draw(s);
		s = new Line2D.Double(width / 10, height / 10, width / 6, height / 6);
		g2.draw(s);
		s = new Line2D.Double(width - width / 10, height - height / 10, width
				- width / 6, height - height / 6);
		g2.draw(s);
		s = new Line2D.Double(width - width / 10, height / 10, width - width
				/ 6, height / 6);
		g2.draw(s);
		s = new Line2D.Double((width / 10), (height - (height / 10)),
				(width / 6), (height - (height / 6)));
		g2.draw(s);

		g2.setTransform(defaultTr);

	}

	private void f2Icon(Graphics2D g2, int x, int y, int width, int height) {
		System.out.println("F2 x: " + x + " y: " + y + " width: " + width
				+ " height: " + height);
		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);

		int ovalWidth = (int) ((width / 2));
		int ovalHeight = (int) ((height / 2));

		int ovalX = (int) ((width / 4));
		int ovalY = (int) ((height / 4));

		g2.setTransform(tr);

		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));
		g2.drawOval(ovalX, ovalY, ovalWidth, ovalHeight);

		Shape s = new Line2D.Double(((width / 2)), 0, ((width / 2)),
				((height / 6)));

		g2.draw(s);
		s = new Line2D.Double(((width / 2)), ((height - (height / 6))),
				((width / 2)), height);
		g2.draw(s);
		s = new Line2D.Double(0, height / 2, ((width / 6)), height / 2);
		g2.draw(s);
		s = new Line2D.Double(((width - (width / 6))), height / 2, width,
				height / 2);
		g2.draw(s);
		s = new Line2D.Double(width / 8, height / 8, width / 4, height / 4);
		g2.draw(s);
		s = new Line2D.Double(width - width / 8, height - height / 8, width
				- width / 4, height - height / 4);
		g2.draw(s);
		s = new Line2D.Double(width - width / 8, height / 8, width - width / 4,
				height / 4);
		g2.draw(s);
		s = new Line2D.Double((width / 8), (height - (height / 8)),
				(width / 4), (height - (height / 4)));
		g2.draw(s);

		g2.setTransform(defaultTr);

	}

	private void f3Icon(Graphics2D g2, int x, int y, int width, int height) {
		System.out.println("F3 x: " + x + " y: " + y + " width: " + width
				+ " height: " + height);
		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);
		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));
		g2.setTransform(tr);

		Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, width, height);
		g2.draw(rect);
		rect = new Rectangle2D.Double(width / 8, height / 8, width / 3,
				height / 3);
		g2.draw(rect);
		rect = new Rectangle2D.Double(width - (width / 2.5), (height / 6),
				width / 4, height / 2.5);
		g2.draw(rect);
		rect = new Rectangle2D.Double(width / 6, height / 1.5, width / 2,
				height / 4);
		g2.draw(rect);

		g2.setTransform(defaultTr);

	}

	private void f4Icon(Graphics2D g2, int x, int y, int width, int height) {

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);

		g2.setTransform(tr);

		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));

		g2.drawOval(0, 0, width, height);
		Path2D.Double p = new Path2D.Double();

		p.moveTo(width / 2 - (width / 12), height - 2);
		p.lineTo(width / 2 + (width / 12), height - 2);
		p.lineTo(width / 2, height / 3);
		p.lineTo(width / 2 - (width / 12), height - 2);

		g2.fill(p);
		Line2D.Double s = new Line2D.Double(width / 2, 0, width / 2, height / 6);
		g2.draw(s);

		s = new Line2D.Double(width / 8, height / 8, width / 4, height / 4);
		g2.draw(s);
		s = new Line2D.Double(width - width / 8, height / 8, width - width / 4,
				height / 4);
		g2.draw(s);
		s = new Line2D.Double(0, height / 2, ((width / 6)), height / 2);
		g2.draw(s);
		s = new Line2D.Double(((width - (width / 6))), height / 2, width,
				height / 2);
		g2.draw(s);

		g2.setTransform(defaultTr);

	}

	private void f5Icon(Graphics2D g2, int x, int y, int width, int height) {

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);

		g2.setTransform(tr);

		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));

		Line2D.Double s = new Line2D.Double(width / 2, 0, width / 2, height / 8);
		g2.draw(s);

		s = new Line2D.Double(width / 10, height / 10, width / 6, height / 6);
		g2.draw(s);
		s = new Line2D.Double(width - width / 10, height / 10, width - width
				/ 6, height / 6);
		g2.draw(s);
		s = new Line2D.Double(0, height / 3, ((width / 6)), height / 3);
		g2.draw(s);
		s = new Line2D.Double(((width - (width / 8))), height / 3, width,
				height / 3);
		g2.draw(s);

		g2.setStroke(new BasicStroke((width < 20) ? 2 : 4));
		g2.drawLine(width / 3, height / 3 + 3, width - width / 3,
				height / 3 + 3);

		g2.setTransform(defaultTr);

	}

	private void f6Icon(Graphics2D g2, int x, int y, int width, int height) {

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);

		g2.setTransform(tr);

		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));

		Line2D.Double s = new Line2D.Double(width / 2, 0, width / 2, height / 6);
		g2.draw(s);

		s = new Line2D.Double(width / 8, height / 8, width / 4, height / 4);
		g2.draw(s);
		s = new Line2D.Double(width - width / 8, height / 8, width - width / 4,
				height / 4);
		g2.draw(s);
		s = new Line2D.Double(0, height / 2, ((width / 6)), height / 2);
		g2.draw(s);
		s = new Line2D.Double(((width - (width / 6))), height / 2, width,
				height / 2);
		g2.draw(s);

		g2.setStroke(new BasicStroke((width < 20) ? 3 : 5));
		g2.drawLine(width / 3, height / 2 + 3, width - width / 3,
				height / 2 + 3);

		g2.setTransform(defaultTr);

	}

	private void f7Icon(Graphics2D g2, int x, int y, int width, int height) {

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);
		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));
		g2.setTransform(tr);

		Path2D.Double p = new Path2D.Double();
		p.moveTo(0, height / 2);
		p.lineTo(width / 2, 0);
		p.lineTo(width / 2, height);
		p.lineTo(0, height / 2);
		g2.fill(p);
		p = new Path2D.Double();
		p.moveTo(width / 2, height / 2);
		p.lineTo(width, 0);
		p.lineTo(width, height);
		p.lineTo(width / 2, height / 2);
		g2.fill(p);
		g2.setTransform(defaultTr);

		g2.setTransform(defaultTr);

	}

	private void f8Icon(Graphics2D g2, int x, int y, int width, int height) {

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);
		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));
		g2.setTransform(tr);

		Path2D.Double p = new Path2D.Double();
		p.moveTo(width / 2, height / 2);
		p.lineTo(0, 0);
		p.lineTo(0, height);
		p.lineTo(width / 2, height / 2);
		g2.fill(p);

		Rectangle2D.Double rect = new Rectangle2D.Double(width / 2, 0,
				width / 5, height);
		g2.fill(rect);
		rect = new Rectangle2D.Double(width - width / 4, 0, width / 5, height);
		g2.fill(rect);

		g2.setTransform(defaultTr);

	}

	private void f9Icon(Graphics2D g2, int x, int y, int width, int height) {

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);
		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));
		g2.setTransform(tr);

		Path2D.Double p = new Path2D.Double();
		p.moveTo(width / 2, height / 2);
		p.lineTo(0, 0);
		p.lineTo(0, height);
		p.lineTo(width / 2, height / 2);
		g2.fill(p);

		p = new Path2D.Double();
		p.moveTo(width / 2, 0);
		p.lineTo(width / 2, height);
		p.lineTo(width, height / 2);
		p.lineTo(width / 2, 0);
		g2.fill(p);

		g2.setTransform(defaultTr);

	}

	private void f10Icon(Graphics2D g2, int x, int y, int width, int height) {
		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);
		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));
		g2.setTransform(tr);

		RoundRectangle2D.Double r = new RoundRectangle2D.Double(0, height / 4,
				width / 2, height / 2, 5, 5);
		g2.fill(r);

		Arc2D.Double arc = new Arc2D.Double(width / 3, 0, width / 2, height,
				90, 180, Arc2D.CHORD);
		g2.fill(arc);
		g2.setTransform(defaultTr);
	}

	private void f11Icon(Graphics2D g2, int x, int y, int width, int height) {

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);
		g2.setStroke(new BasicStroke((width < 20) ? 1 : 2));
		g2.setTransform(tr);

		RoundRectangle2D.Double r = new RoundRectangle2D.Double(0, height / 4,
				width / 2, height / 2, 5, 5);
		g2.fill(r);

		Arc2D.Double arc = new Arc2D.Double(width / 3, 0, width / 2, height,
				90, 180, Arc2D.CHORD);
		g2.fill(arc);

		arc = new Arc2D.Double(width / 3, height / 8, width / 2, height
				- (height / 4), 60, -120, Arc2D.OPEN);
		g2.setStroke(new BasicStroke((width < 20) ? 3 : 5));
		g2.draw(arc);

		g2.setTransform(defaultTr);

	}

	private void f12Icon(Graphics2D g2, int x, int y, int width, int height) {

		AffineTransform defaultTr = g2.getTransform();
		AffineTransform tr = new AffineTransform();
		tr.translate(x, y);
		g2.setStroke(new BasicStroke((width < 20) ? 2 : 3));
		g2.setTransform(tr);

		RoundRectangle2D.Double r = new RoundRectangle2D.Double(0, height / 4,
				width / 2, height / 2, 5, 5);
		g2.fill(r);

		Arc2D.Double arc = new Arc2D.Double(width / 3, 0, width / 2, height,
				90, 180, Arc2D.CHORD);
		g2.fill(arc);

		arc = new Arc2D.Double(width / 3, height / 8, width / 2, height
				- (height / 4), 60, -120, Arc2D.OPEN);

		g2.draw(arc);
		arc = new Arc2D.Double(width / 1.8, height / 20, width / 2, height
				- (height / 10), 60, -120, Arc2D.OPEN);

		g2.draw(arc);

		arc = new Arc2D.Double(width / 1.3, 0, width / 2, height, 60, -120,
				Arc2D.OPEN);

		g2.draw(arc);

		g2.setTransform(defaultTr);

	}

}
