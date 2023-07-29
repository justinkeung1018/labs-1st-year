package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import java.util.HashMap;
import java.util.Map;

public class Spreadsheet implements EvaluationContext {
  private final Map<CellLocation, Double> cellValues;
  
  /**
   * Construct an empty spreadsheet.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  Spreadsheet() {
    cellValues = new HashMap<>();
  }

  /**
   * Parse and evaluate an expression, using the spreadsheet as a context.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public double evaluateExpression(String expression) throws InvalidSyntaxException {
    try {
      return Parser.parse(expression).evaluate(this);
    } catch (Exception e) {
      throw new InvalidSyntaxException("Invalid syntax");
    }
  }

  /**
   * Assign an expression to a cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public void setCellExpression(CellLocation location, String input)
      throws InvalidSyntaxException {
    cellValues.put(location, evaluateExpression(input));
  }

  @Override
  public double getCellValue(CellLocation location) {
    return cellValues.getOrDefault(location, 0.0);
  }
}
