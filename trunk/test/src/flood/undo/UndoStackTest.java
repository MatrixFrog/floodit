package flood.undo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UndoStackTest {

  String aString;

  /**
   * Test whether things get "erased" as they should when you go back
   * and "change history"
   */
  @Test public void eraseTest() {
    UndoStack<String> stack = new UndoStack<String>();
    stack.add("one");
    stack.add("two");
    stack.add("three");

    assertEquals(3, stack.size());
    assertEquals("three", stack.current());
    stack.undo();
    assertEquals("two", stack.current());
    stack.add("four");
    assertEquals("four", stack.current());
    assertEquals(3, stack.size());

    assertFalse(stack.contains("three"));
  }

  @Test public void basicTest() throws CannotUndoException, CannotRedoException {
    UndoStack<String> stack = new UndoStack<String>();

    assertEquals(0, stack.size());
    assertNull(stack.current());

    stack.add("one");
    assertEquals(1, stack.size());
    assertEquals("one",stack.current());

    stack.add("two");
    assertEquals(2, stack.size());
    assertEquals("two",stack.current());

    stack.add("three");
    assertEquals(3, stack.size());
    assertEquals("three",stack.current());

    aString = stack.undo();
    assertEquals("two", aString);
    assertEquals(3, stack.size());
    assertEquals("two", stack.current());

    aString = stack.undo();
    assertEquals("one", aString);
    assertEquals(3, stack.size());
    assertEquals("one", stack.current());

    try {
      stack.undo();
      fail("stack.undo() should have thrown an exception.");
    } catch (CannotUndoException e) {
      // test passes
    }

    aString = stack.redo();
    assertEquals("two", aString);
    assertEquals(3, stack.size());
    assertEquals("two", stack.current());

    aString = stack.redo();
    assertEquals("three", aString);
    assertEquals(3, stack.size());
    assertEquals("three", stack.current());

    try {
      stack.redo();
      fail("stack.redo() should have thrown an exception.");
    } catch (CannotRedoException e) {
      // test passes
    }
  }

}
