package advancedstreams;

import static java.util.stream.Stream.generate;

import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CubeSupplier implements Supplier<Integer> {
  private int number;

  public CubeSupplier() {
    number = 1;
  }

  public static Stream<Integer> cubeStream() {
    return generate(new CubeSupplier());
  }

  public static Stream<Integer> boundedCubeStream(int start, int end) {
    return cubeStream().limit(end).skip(start);
  }

  public static Stream<Integer> palindromicCubes(int start, int end) {
    return cubeStream()
        .limit(end)
        .skip(start)
        .filter(cube -> IsPalindrome.isPalindrome(cube.toString()));
  }

  @Override
  public Integer get() {
    if (number > Math.pow(Integer.MAX_VALUE, 1D / 3)) {
      throw new NoSuchElementException("Cube requested is too large.");
    }
    return (int) Math.pow(number++, 3);
  }
}
