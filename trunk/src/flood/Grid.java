package flood;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import common.RandomUtils;

public class Grid {

	private Square[][] data;
	private Collection<Square> upperLeftGroup = new ArrayList<Square>();
	private List<Color> colors = new ArrayList<Color>();

	public Grid(int width, int height, int numColors) {
		initColors(numColors);
		data = new Square[width][height];
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				data[i][j] = new Square(RandomUtils.choice(colors));
			}
		}
		upperLeftGroup.add(get(0,0));
		update();
	}

	public Grid(Dimension gridSize, int numColors) {
		this(gridSize.width, gridSize.height, numColors);
	}

	private void initColors(int numColors) {
		List<Color> allColors = Square.colors();
		Collections.shuffle(allColors);

		for (int i=0; i<numColors; i++) {
			colors.add(allColors.get(i));
		}
	}

	public Color getUpperLeftColor() {
		return get(0,0).getColor();
	}

	public void changeUpperLeftGroupToColor(Color color) {
		for (Square square : upperLeftGroup) {
			square.setColor(color);
		}
		update();
	}

	private void expandUpperLeftGroup() {
		// TODO use allSquares() or other iterable (difficult because of the getNeighbors() call)
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

	public boolean upperLeftGroupContains(Square square) {
		return upperLeftGroup.contains(square);
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

	public boolean isAllSameColor() {
		Square upperLeft = get(0,0);
		for (Square square : allSquares()) {
			if (!upperLeft.sameColor(square)) {
				return false;
			}
		}
		return true;
	}

	public Iterable<Square> allSquares() {
		return new Iterable<Square>() {
			public Iterator<Square> iterator() {
				return new Iterator<Square>() {
					private int x=0, y=0;

					@Override public boolean hasNext() {
						return y != getHeight();
					}

					@Override public Square next() {
						Square square = get(x,y);
						x++;
						if (x == getWidth()) {
							x = 0;
							y++;
						}
						return square;
					}

					@Override public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public int getWidth() {
		return data.length;
	}

	public int getHeight() {
		return data[0].length;
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

	public List<Color> getColors() {
		return colors;
	}
}
