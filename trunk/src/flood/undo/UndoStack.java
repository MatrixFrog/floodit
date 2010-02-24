package flood.undo;

import java.util.LinkedList;
import java.util.List;

import common.CollectionUtils;

public class UndoStack<T> {

  private List<T> data = new LinkedList<T>();

  private int currentIndex = -1;

  public int size() {
    return data.size();
  }

  public void add(T e) {
    CollectionUtils.truncate(data, currentIndex + 1);
    data.add(e);
    currentIndex++;
  }

  public T undo() throws CannotUndoException {
    if (canUndo()) {
      currentIndex--;
      return current();
    }
    else {
      throw new CannotUndoException();
    }
  }

  public boolean canUndo() {
    return currentIndex > 0;
  }

  public T redo() throws CannotRedoException {
    if (canRedo()) {
      currentIndex++;
      return current();
    }
    else {
      throw new CannotRedoException();
    }
  }

  public boolean canRedo() {
    return currentIndex < data.size() - 1;
  }

  public T current() {
    if (data.isEmpty()) {
      return null;
    }
    else {
      return data.get(currentIndex);
    }
  }

  // Just for testing, at least for right now.
  boolean contains(T e) {
    return data.contains(e);
  }

  @Override
  public String toString() {
    return String.format("UndoStack <%d> %n%s", currentIndex, data);
  }
}
