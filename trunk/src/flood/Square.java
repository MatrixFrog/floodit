package flood;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.RandomUtils;

public class Square {
	private Color color;
	private static final Map<Color, Character> colorsAndNames = new HashMap<Color, Character>() {{
		put(Color.red, 'r');
		put(Color.green, 'g');
		put(Color.blue, 'b');
		put(Color.yellow, 'y');
		put(Color.magenta, 'm');
		put(Color.black, 'l');
		put(Color.white, 'w');
	}};

	public static List<Color> colors() {
		List<Color> colorList = new ArrayList<Color>();
		for (Color color : colorsAndNames.keySet()) {
			colorList.add(color);
		}
		return colorList;
	}

	public static Map<Color, Character> colorsNames() {
		return colorsAndNames;
	}

	public static Square getRandomInstance() {
		return new Square((Color) RandomUtils.choice(colorsAndNames.keySet().toArray()));
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
		return colorsAndNames.get(color).toString();
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