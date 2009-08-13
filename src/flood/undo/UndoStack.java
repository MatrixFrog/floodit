package flood.undo;

import java.util.LinkedList;
import java.util.List;

public class UndoStack<T> {

  private List<T> data = new LinkedList<T>();

  private int currentIndex = -1;

  public int size() {
    return data.size();
  }

  public void push(T e) {
    while (data.size() > currentIndex+1) {
      data.remove(currentIndex+1);
    }
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
    return currentIndex < data.size()-1;
  }

  public T current() {
    if (data.isEmpty()) {
      return null;
    }
    else {
      return data.get(currentIndex);
    }
  }

  @Override
  public String toString() {
    return "UndoStack" + data;
  }
}
