package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;

import java.util.Set;

public class CellReference implements Expression {
  private final CellLocation cellLocation;

  public CellReference(CellLocation cellLocation) {
    assert cellLocation != null;
    this.cellLocation = cellLocation;
  }

  @Override
  public double evaluate(EvaluationContext context) {
    return 0;
  }

  @Override
  public void findCellReferences(Set<common.api.CellLocation> dependencies) {

  }

  @Override
  public String toString() {
    return cellLocation.toString();
  }
}
