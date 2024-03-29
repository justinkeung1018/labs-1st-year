package spreadsheet;

import common.api.CellLocation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    Spreadsheet spreadsheet = new Spreadsheet();
    BufferedReader in = new BufferedReader(new InputStreamReader(input));
    while (true) {
      output.print("> ");
      String line = in.readLine();
      if (line == null) {
        break;
      }
      String[] parts = line.split("=", 2);
      try {
        if (parts.length == 1) {
          output.println(spreadsheet.evaluateExpression(parts[0]));
        } else {
          spreadsheet.setCellExpression(new CellLocation(parts[0]), parts[1]);
        }
      } catch (InvalidSyntaxException e) {
        output.println("Expression not well-formed");
      }
    }
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
