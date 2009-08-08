package flood;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import common.RandomUtils;
import common.swingutils.Points;
public class Grid {

	private Square[][] data;
	private Collection<Square> upperLeftGroup = new HashSet<Square>();
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
		boolean squareWasAdded;
		do {
			squareWasAdded=false;
			for (int x=0; x<getWidth(); x++) {
				for (int y=0; y<getHeight(); y++) {
					Square square = get(x, y);
					for (Square neighbor : getNeighbors(x, y)) {
						if (upperLeftGroup.contains(square) &&
								square.sameColor(neighbor) &&
								!upperLeftGroup.contains(neighbor)) {

							upperLeftGroup.add(neighbor);
							squareWasAdded = true;

						}
					}
				}
			}
		} while (squareWasAdded);
	}

	/**
	 * @return All the squares orthogonally adjacent to the square at (i, j).
	 */
	List<Square> getNeighbors(int i, int j) {
		List<Square> neighbors = new ArrayList<Square>();
		for (Point p : Points.getOrthoNeighbors(new Point(i, j))) {
			if (this.contains(p)) {
				neighbors.add(this.get(p));
			}
		}
		return neighbors;
	}

	private boolean contains(Point p) {
		return (0<=p.x && p.x<getWidth() &&
				0<=p.y && p.y<getHeight());
	}

	public void paint(Graphics g, int width, int height) {
		int squareWidth = width / getWidth();
		int squareHeight = height / getHeight();
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				this.get(x,y).paint(g,
						x*squareWidth, y*squareHeight,
						squareWidth, squareHeight, upperLeftGroup.contains(get(x,y)));
			}
		}
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
				Square square = get(i,j);
				String str = square.toString();
				if (upperLeftGroup.contains(square)) {
					str = str.toUpperCase();
				}
				sb.append(str);
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	private Square get(Point p) {
		return get(p.x, p.y);
	}

	public Square get(int i, int j) {
		return data[i][j];
	}

	public List<Color> getColors() {
		return colors;
	}
}
