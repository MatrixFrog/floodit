package flood;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import common.RandomUtil;

public class Square {
	private Color color;
	private static final Map<Color, Character> colorsNames = new HashMap<Color, Character>() {{
		put(Color.red, 'R');
		put(Color.green, 'G');
		put(Color.blue, 'B');
		put(Color.yellow, 'Y');
		put(Color.magenta, 'M');
		put(Color.black, 'L');
		put(Color.white, 'W');
	}};

	public static Collection<Color> colors() {
		return colorsNames.keySet();
	}

	public static Map<Color, Character> colorsNames() {
		return colorsNames;
	}

	public static Square getRandomInstance() {
		return new Square((Color) RandomUtil.choice(colorsNames.keySet().toArray()));
	}

	public Square(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}

	public boolean sameColor(Square other) {
		return (color.equals(other.getColor()));
	}

	@Override
	public String toString() {
		return colorsNames.get(color).toString();
	}

	/**
	 * Equivalent to paint(g, x, y, width, height, false)
	 */
	public void paint(Graphics g, int x, int y,
			int width, int height) {
		paint(g, x, y, width, height, false);
	}

	public void paint(Graphics g, int x, int y,
			int width, int height, boolean addDebugDot) {
		g.setColor(getColor());
		g.fillRect(x, y, width, height);
		if (addDebugDot) {
			g.setColor(Color.black);
			g.fillOval(x+width/3, y+height/3, width/3, height/3);
		}
	}

}