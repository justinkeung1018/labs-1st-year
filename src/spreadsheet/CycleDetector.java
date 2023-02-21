package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import java.util.HashSet;
import java.util.Set;

/** Detects dependency cycles. */
public class CycleDetector {
  private final BasicSpreadsheet spreadsheet;

  /**
   * Constructs a new cycle detector.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param spreadsheet The parent spreadsheet, used for resolving cell locations.
   */
  CycleDetector(BasicSpreadsheet spreadsheet) {
    this.spreadsheet = spreadsheet;
  }

  /**
   * Checks for a cycle in the spreadsheet, starting at a particular cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param start The cell location where cycle detection should start.
   * @return Whether a cycle was detected in the dependency graph starting at the given cell.
   */
  public boolean hasCycleFrom(CellLocation start) {
    return hasCycleFrom(start, new HashSet<>());
  }

  private boolean hasCycleFrom(CellLocation start, Set<CellLocation> visited) {
    if (visited.contains(start)) {
      return true;
    }
    visited.add(start);
    Set<CellLocation> references = new HashSet<>();
    spreadsheet.findCellReferences(start, references);
    for (CellLocation reference : references) {
      if (hasCycleFrom(reference, visited)) {
        return true;
      }
    }
    visited.remove(start);
    return false;
  }
}
