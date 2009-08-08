package flood;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for flood.Grid
 */
public class GridTest {

	@Test public void testGetNeighbors() {
		Grid[] grids = new Grid[] {
			new Grid(4, 3, 5),
			new Grid(15, 20, 3),
			new Grid(2, 2, 6),
			new Grid(45, 50, 8)
		};
		for (Grid grid : grids) {
			testGetNeighbors(grid);
		}
	}

	public void testGetNeighbors(Grid grid) {
		for (int i=0; i<grid.getWidth(); i++) {
			for (int j=0; j<grid.getHeight(); j++) {
				testGetNeighbors(grid, i, j);
			}
		}
	}

	public void testGetNeighbors(Grid grid, int i, int j) {
		List<Square> neighbors = grid.getNeighbors(i, j);
		int numNeighbors = 4;
		if (i==0 || i == grid.getWidth()-1) {
			numNeighbors--;
		}
		if (j==0 || j == grid.getHeight()-1) {
			numNeighbors--;
		}
		assertEquals(numNeighbors, neighbors.size());
	}

	public void testColors(int numColors) {
		Grid grid = new Grid(new Dimension(10, 10), numColors);
		List<Color> colors = grid.getColors();
		for (Color c : colors) {
			System.out.print(new Square(c));
		}
		System.out.println();
		assertEquals(numColors, colors.size());
	}

	@Test public void testColors() {
		for (int i=1; i<Square.colors().size(); i++) {
			testColors(i);
		}
	}

	public void testIteration(int width, int height) {
		Grid g = new Grid(width, height, 4);
		int i=0;
		for (Square square: g.allSquares()) {
			System.out.print(square);
			i++;
		}
		System.out.println();
		assertEquals(width*height, i);
	}

	/**
	 * Test the allSquares() method which iterates over all the squares.
	 */
	@Test public void testIteration() {
		testIteration(1, 1);
		testIteration(1, 5);
		testIteration(5, 1);
		testIteration(5, 5);
		testIteration(10, 10);
		testIteration(20, 30);
	}
}
