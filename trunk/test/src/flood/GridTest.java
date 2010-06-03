package flood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.List;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class GridTest {
  private static final boolean DEBUG = false;

  @DataPoints
  public static final GameSettings[] settingsList = {
      new GameSettings(4, 4, 5),
      new GameSettings(10, 10, 3),
      new GameSettings(10, 20, 3),
      new GameSettings(5, 50, 6),
      new GameSettings(2, 2, 7),
      new GameSettings(6, 100, 4)
  };

  @Theory
  public void sanityCheck(GameSettings settings) {
    Grid grid = new Grid(settings);
    assertEquals(settings.width, grid.getWidth());
    assertEquals(settings.height, grid.getHeight());
    assertEquals(settings.numColors, grid.getColors().size());
  }

  @Theory
  public void testClone(GameSettings settings) {
    Grid orig = new Grid(settings);
    Grid clone = orig.clone();

    if (DEBUG) {
      System.out.println("Original:");
      System.out.println(orig);
      System.out.println("Clone:");
      System.out.println(clone);
    }
    assertEquals(orig.getWidth(), clone.getWidth());
    assertEquals(orig.getHeight(), clone.getHeight());
    assertEquals(orig.getColors().size(), clone.getColors().size());
    assertEquals(orig.getNumInUpperLeftGroup(), clone.getNumInUpperLeftGroup());

    for (Color color : orig.getColors()) {
      assertTrue(clone.getColors().contains(color));
    }

    for (int x=0; x<orig.getWidth(); x++) {
      for (int y=0; y<orig.getHeight(); y++) {
        Square origSquare = orig.get(x,y);
        Square cloneSquare = clone.get(x,y);
        assertNotSame(origSquare, cloneSquare);
        assertTrue(origSquare.sameColor(cloneSquare));
      }
    }
  }

  @Theory
  public void testGetNeighbors(GameSettings settings) {
    Grid grid = new Grid(settings);
    for (int x=0; x<grid.getWidth(); x++) {
      for (int y=0; y<grid.getHeight(); y++) {
        testGetNeighbors(grid, x, y);
      }
    }
  }

  private void testGetNeighbors(Grid grid, int x, int y) {
    List<Square> neighbors = grid.getNeighbors(x, y);
    int numNeighbors = 4;
    if (x==0 || x == grid.getWidth()-1) {
      numNeighbors--;
    }
    if (y==0 || y == grid.getHeight()-1) {
      numNeighbors--;
    }
    assertEquals(numNeighbors, neighbors.size());
  }

  @Theory
  public void testIteration(GameSettings settings) {
    Grid grid = new Grid(settings);
    int i=0;
    for (@SuppressWarnings("unused") Square square: grid.allSquares()) {
      i++;
    }
    assertEquals(grid.getWidth()*grid.getHeight(), i);
  }
}
