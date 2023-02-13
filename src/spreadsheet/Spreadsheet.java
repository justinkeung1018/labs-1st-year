package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;

public class Spreadsheet implements EvaluationContext {
  /**
   * Construct an empty spreadsheet.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  Spreadsheet() {}

  /**
   * Parse and evaluate an expression, using the spreadsheet as a context.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public double evaluateExpression(String expression) throws InvalidSyntaxException {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Assign an expression to a cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public void setCellExpression(CellLocation location, String input)
      throws InvalidSyntaxException {}

  @Override
  public double getCellValue(CellLocation location) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
