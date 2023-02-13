package spreadsheet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class Main {
  /**
   * Main entry-point for the read-eval-print loop.
   *
   * <p>This method allows any `input` and `output` streams to be used. You should use these
   * wherever you would normally use `System.in` or `System.out`. This enables the REPL to be tested
   * without interfering with the process' standard input and output.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public static void interact(InputStream input, PrintStream output) throws IOException {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Starting point of the program.
   *
   * <p>This method delegates all the work to the interact() method, using the process' standard
   * input and output.
   *
   * <p>For Part 1 of the coursework, you shouldn't have to modify this.
   */
  public static void main(String[] args) throws IOException {
    interact(System.in, System.out);
  }
}
