package flood;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;


public class SquareTest {

  @DataPoints
  public static final Square[] squares = {
    new Square(Color.RED),
    new Square(Color.BLACK),
    new Square(Color.GREEN)
  };

  @Theory public void testClone(Square orig) {
    Square clone = orig.clone();
    assertNotSame(orig, clone);
    assertTrue(orig.sameColor(clone));
  }

}
