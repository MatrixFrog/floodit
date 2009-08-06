package flood.test;

import junit.framework.TestCase;

import org.junit.Test;

import flood.Grid;
import flood.Square;

/**
 * Unit test for flood.Grid
 */
public class GridTest extends TestCase {

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
