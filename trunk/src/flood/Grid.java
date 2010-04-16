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

import util.RandomUtils;
import util.swingutils.Points;


public class Grid {

  private Square[][] data;
  Collection<Square> upperLeftGroup = new HashSet<Square>();
  private List<Color> colors = new ArrayList<Color>();

  public Grid(int width, int height, int numColors) {
    initColors(numColors);
    data = new Square[width][height];
    initSquares();
    initUpperLeftGroup();
  }

  public Grid(Dimension gridSize, int numColors) {
    this(gridSize.width, gridSize.height, numColors);
  }

  public Grid(GameSettings settings) {
    this(settings.width, settings.height, settings.numColors);
  }

  /**
   * Used only in {@link #clone()}
   */
  private Grid(int width, int height) {
    data = new Square[width][height];
  }

  private void initSquares() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        data[x][y] = new Square(RandomUtils.choice(colors));
      }
    }
  }

  private void initUpperLeftGroup() {
    upperLeftGroup.clear();
    upperLeftGroup.add(get(0, 0));
    update();
  }

  @Override
  protected Grid clone() {
    Grid clone = new Grid(this.getWidth(), this.getHeight());
    clone.colors = this.colors;
    for (int x=0; x<getWidth(); x++) {
      for (int y=0; y<getHeight(); y++) {
        clone.data[x][y] = this.data[x][y].clone();
      }
    }
    clone.initUpperLeftGroup();
    return clone;
  }

  private void initColors(int numColors) {
    List<Color> allColors = Square.colors();

    if (numColors > allColors.size()) {
      throw new IllegalArgumentException(
          String.format("Flood It only knows of %d colors, so you cannot " +
              "have more than that in your grid.", allColors.size()));
    }
    Collections.shuffle(allColors);

    for (int i = 0; i < numColors; i++) {
      colors.add(allColors.get(i));
    }
  }

  public Color getUpperLeftColor() {
    return get(0, 0).getColor();
  }

  public int getNumInUpperLeftGroup() {
    return upperLeftGroup.size();
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
      squareWasAdded = false;
      for (int x = 0; x < getWidth(); x++) {
        for (int y = 0; y < getHeight(); y++) {
          Square square = get(x, y);
          for (Square neighbor : getNeighbors(x, y)) {
            if (upperLeftGroup.contains(square) && square.sameColor(neighbor)
                && !upperLeftGroup.contains(neighbor)) {

              upperLeftGroup.add(neighbor);
              squareWasAdded = true;

            }
          }
        }
      }
    } while (squareWasAdded);
  }

  /**
   * @return All the squares orthogonally adjacent to the square at (x, y).
   */
  List<Square> getNeighbors(int x, int y) {
    List<Square> neighbors = new ArrayList<Square>();
    for (Point p : Points.getOrthoNeighbors(new Point(x, y))) {
      if (this.contains(p)) {
        neighbors.add(this.get(p));
      }
    }
    return neighbors;
  }

  private boolean contains(Point p) {
    return (0 <= p.x && p.x < getWidth() && 0 <= p.y && p.y < getHeight());
  }

  public void paint(Graphics g, int width, int height) {
    int squareWidth = width / getWidth();
    int squareHeight = height / getHeight();
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        this.get(x, y).paint(g, x * squareWidth, y * squareHeight, squareWidth,
            squareHeight, upperLeftGroup.contains(get(x, y)));
      }
    }
  }

  public void update() {
    expandUpperLeftGroup();
  }

  public boolean isAllSameColor() {
    return getNumInUpperLeftGroup() == getWidth()*getHeight();
  }

  public Iterable<Square> allSquares() {
    return new Iterable<Square>() {
      public Iterator<Square> iterator() {
        return new Iterator<Square>() {
          private int x = 0, y = 0;

          @Override
          public boolean hasNext() {
            return y != getHeight();
          }

          @Override
          public Square next() {
            Square square = get(x, y);
            x++;
            if (x == getWidth()) {
              x = 0;
              y++;
            }
            return square;
          }

          @Override
          public void remove() {
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
    StringBuilder sb = new StringBuilder("\n");
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Square square = get(x, y);
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

  public Square get(int x, int y) {
    return data[x][y];
  }

  public List<Color> getColors() {
    return colors;
  }

  public boolean containsColor(Color color) {
    for (int x=0; x<getWidth(); x++) {
      for (int y=0; y<getHeight(); y++) {
        if (get(x,y).getColor().equals(color)) {
          return true;
        }
      }
    }
    return false;
  }
}
