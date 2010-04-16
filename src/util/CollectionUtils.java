package util;

import java.util.List;

public class CollectionUtils {
  private CollectionUtils() {}

  /**
   * Remove everything from {@code index} to the end of
   * the list. For example:
   *
   * <pre>
   *   List<Character> list = new ArrayList<Character>();
   *   list.add('A');
   *   list.add('B');
   *   list.add('C');
   *   list.add('D');
   *   list.add('E');
   *
   *   CollectionUtils.truncate(list, 2);
   *
   *   System.out.println(list); // prints [A, B]
   * </pre>
   *
   * Has no effect if {@code index > list.size()}.
   *
   */
  public static <T> void truncate(List<T> list, int index) {
    if (index < 0) {
      throw new IllegalArgumentException("The argument 'index' cannot be negative.");
    }
    while (list.size() > index) {
      list.remove(index);
    }
  }
}
