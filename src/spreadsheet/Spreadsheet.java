package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Spreadsheet implements BasicSpreadsheet {
  //
  // start replacing
  //

  private final Map<CellLocation, Cell> cells;
  private final CycleDetector cycleDetector;

  /**
   * Construct an empty spreadsheet.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  Spreadsheet() {
    cells = new HashMap<>();
    cycleDetector = new CycleDetector(this);
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
  @Override
  public void setCellExpression(CellLocation location, String input)
      throws InvalidSyntaxException {
    Cell cell = getOrCreateNewCell(location);
    cell.setExpression(input);
    if (cycleDetector.hasCycleFrom(location)) {
      cell.setExpression("");
    } else {
      cell.recalculate();
    }
  }

  @Override
  public double getCellValue(CellLocation location) {
    return getOrCreateNewCell(location).getValue();
  }

  //
  // end replacing
  //

  @Override
  public String getCellExpression(CellLocation location) {
    return getOrCreateNewCell(location).getExpression();
  }

  @Override
  public String getCellDisplay(CellLocation location) {
    return getOrCreateNewCell(location).toString();
  }

  @Override
  public void addDependency(CellLocation dependent, CellLocation dependency) {
    getOrCreateNewCell(dependency).addDependent(dependent);
  }

  @Override
  public void removeDependency(CellLocation dependent, CellLocation dependency) {
    getOrCreateNewCell(dependency).removeDependent(dependent);
  }

  @Override
  public void recalculate(CellLocation location) {
    getOrCreateNewCell(location).recalculate();
  }

  @Override
  public void findCellReferences(CellLocation subject, Set<CellLocation> target) {
    getOrCreateNewCell(subject).findCellReferences(target);
  }

  private Cell getOrCreateNewCell(CellLocation location) {
    Cell cell = cells.get(location);
    if (cell == null) {
      cell = new Cell(this, location);
      cells.put(location, cell);
    }
    return cell;
  }
}
