package flood;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Grid {

	private Square[][] data;

	private Collection<Square> upperLeftGroup = new ArrayList<Square>();

	public void changeUpperLeftGroupToColor(Color color) {
		for (Square square : upperLeftGroup) {
			square.setColor(color);
		}
		update();
	}

	public void expandUpperLeftGroup() {
		upperLeftGroup.add(data[0][0]);
		for (int i=0; i<getWidth(); i++) {
			for (int j=0; j<getHeight(); j++) {
				Square square = get(i, j);
				for (Point p : getNeighbors(new Point(i, j))) {
					if (this.contains(p)) {
						Square neighbor = get(p);
						if (upperLeftGroup.contains(neighbor) &&
							square.sameColor(neighbor)) {
							upperLeftGroup.add(square);
						}
					}
				}
			}
		}
	}

	public boolean contains(Point p) {
		return (0<=p.x && p.x<getWidth() &&
				0<=p.y && p.y<getHeight());
	}

	public static List<Point> getNeighbors(Point p) {
		return Arrays.asList(new Point(p.x-1, p.y),
				new Point(p.x+1, p.y),
				new Point(p.x, p.y+1),
				new Point(p.x, p.y-1));
	}

	public void update() {
		expandUpperLeftGroup();
	}

	public int getWidth() {
		return data.length;
	}

	public int getHeight() {
		return data[0].length;
	}

	public Grid(int width, int height) {
		data = new Square[width][height];
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				data[i][j] = Square.getRandomInstance();
			}
		}
		upperLeftGroup.add(get(0,0));
		update();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<getWidth(); i++) {
			for (int j=0; j<getHeight(); j++) {
				sb.append(data[i][j].toString().charAt(0));
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public Square get(Point p) {
		return get(p.x, p.y);
	}

	public Square get(int i, int j) {
		return data[i][j];
	}

	public boolean upperLeftGroupContains(Square square) {
		return upperLeftGroup.contains(square);
	}
}
