package flood;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;


public class SquareTest {

  @Test public void testClone() {
    Square orig = new Square(Color.red);
    Square clone = orig.clone();
    assertNotSame(orig, clone);
    assertTrue(orig.sameColor(clone));
  }

}
