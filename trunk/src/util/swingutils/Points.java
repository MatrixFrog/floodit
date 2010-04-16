package util.swingutils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Points {
	private Points() {
	}

	/**
	 * @return A list containing the four points directly above, below, and
	 * to the sides of p. The order is not specified.
	 * @see #getAllNeighbors(Point)
	 * @see #getDiagonalNeighbors(Point)
	 */
	public static List<Point> getOrthoNeighbors(Point p) {
		return Arrays.asList(new Point[] {
				new Point(p.x-1, p.y),
				new Point(p.x+1, p.y),
				new Point(p.x, p.y+1),
				new Point(p.x, p.y-1)
		});
	}

	/**
	 * @return A list containing the four points diagonally adjacent
	 * to p. The order is not specified.
	 * @see #getAllNeighbors(Point)
	 * @see #getOrthoNeighbors(Point)
	 */
	public static List<Point> getDiagonalNeighbors(Point p) {
		return Arrays.asList(new Point[] {
				new Point(p.x-1, p.y-1),
				new Point(p.x-1, p.y+1),
				new Point(p.x+1, p.y-1),
				new Point(p.x+1, p.y+1)
		});
	}

	/**
	 * @return A list containing the eight points adjacent to p.
	 * The order is not specified.
	 * @see #getDiagonalNeighbors(Point)
	 * @see #getOrthoNeighbors(Point)
	 */
	public static List<Point> getAllNeighbors(Point p) {
		List<Point> neighbors = new ArrayList<Point>();
		neighbors.addAll(getOrthoNeighbors(p));
		neighbors.addAll(getDiagonalNeighbors(p));
		return neighbors;
	}
}
