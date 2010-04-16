package util;

import java.util.List;
import java.util.Random;

/**
 * Utility methods relating to randomness.
 */
public class RandomUtils {
	private RandomUtils() {}

	/**
	 * Chooses one of the objects from the array at random.
	 *
	 * Roughly equivalent to Python's random.choice()
	 */
	public static <T> T choice(T[] array) {
		int i = new Random().nextInt(array.length);
		return array[i];
	}

	/**
   * Chooses one of the objects from the list at random.
   *
   * Roughly equivalent to Python's random.choice()
   */
	public static <T> T choice(List<T> list) {
		int i = new Random().nextInt(list.size());
		return list.get(i);
	}
}
