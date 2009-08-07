package flood.test;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import flood.Grid;
import flood.Square;

/**
 * Unit test for flood.Grid
 */
public class GridTest extends TestCase {

	/**
	 * Test the colors() method.
	 */
	public void testColors(int numColors) {
		List<Color> colors = new Grid(new Dimension(10, 10), numColors).getColors();
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
			i++;
		}
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
