package util.swingutils;

import java.awt.Color;

/**
 * Utility methods related to the java.awt.Color class
 */
public class Colors {
	private Colors() {}

	/**
	 * @return the "opposite" of original
	 */
	public static Color getOpposite(Color original) {
		return new Color(
			invert(original.getRed()),
			invert(original.getGreen()),
			invert(original.getBlue())
		);
	}

	private static int invert(int num) {
		return 255-num;
	}
}
