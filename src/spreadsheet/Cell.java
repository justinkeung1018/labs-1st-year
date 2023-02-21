package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import common.api.Expression;
import common.lexer.InvalidTokenException;
import java.util.HashSet;
import java.util.Set;

/**
 * A single cell in a spreadsheet, tracking the expression, value, and other parts of cell state.
 */
public class Cell {
  private final BasicSpreadsheet spreadsheet;
  private final CellLocation location;
  private double value;
  private Expression expression;
  private final Set<CellLocation> dependents;

  /**
   * Constructs a new cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param spreadsheet The parent spreadsheet,
   * @param location The location of this cell in the spreadsheet.
   */
  Cell(BasicSpreadsheet spreadsheet, CellLocation location) {
    this.spreadsheet = spreadsheet;
    this.location = location;
    this.value = 0.0;
    this.expression = null;
    this.dependents = new HashSet<>();
  }

  /**
   * Gets the cell's last calculated value.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @return the cell's value.
   */
  public double getValue() {
    return value;
  }

  /**
   * Gets the cell's last stored expression, in string form.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @return a string that parses to an equivalent expression to that last stored in the cell; if no
   *     expression is stored, we return the empty string.
   */
  public String getExpression() {
    return expression == null ? "" : expression.toString();
  }

  /**
   * Sets the cell's expression from a string.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param input The string representing the new cell expression.
   * @throws InvalidSyntaxException if the string cannot be parsed.
   */
  public void setExpression(String input) throws InvalidSyntaxException {
    try {
      Set<CellLocation> oldReferences = new HashSet<>();
      findCellReferences(oldReferences);
      for (CellLocation reference : oldReferences) {
        spreadsheet.removeDependency(location, reference);
      }
      expression = input.isEmpty() ? null : Parser.parse(input);
      Set<CellLocation> newReferences = new HashSet<>();
      findCellReferences(newReferences);
      for (CellLocation reference : newReferences) {
        spreadsheet.addDependency(location, reference);
      }
    } catch (InvalidTokenException e) {
      throw new InvalidSyntaxException("Invalid token");
    }
  }

  /**
   * Returns the string representation of the value.
   *
   * @return a string representing the value, if any, of this cell.
   */
  @Override
  public String toString() {
    return value == 0.0 ? "" : String.valueOf(value);
  }

  /**
   * Adds the given location to this cell's dependents.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param location the location to add.
   */
  public void addDependent(CellLocation location) {
    dependents.add(location);
  }

  /**
   * Adds the given location to this cell's dependents.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param location the location to add.
   */
  public void removeDependent(CellLocation location) {
    dependents.remove(location);
  }

  /**
   * Adds this cell's expression's references to a set.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param target The set that will receive the dependencies for this
   */
  public void findCellReferences(Set<CellLocation> target) {
    if (expression != null) {
      expression.findCellReferences(target);
    }
  }

  /**
   * Recalculates this cell's value based on its expression.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public void recalculate() {
    value = expression == null ? 0.0 : expression.evaluate(spreadsheet);
    for (CellLocation dependent : dependents) {
      spreadsheet.recalculate(dependent);
    }
  }
}
