package uk.ac.imperial.matrixmult;

public interface Matrix {

  public double get(int row, int column);

  public void set(int row, int column, double value);

  public int getNumRows();

  public int getNumColumns();

  public default boolean equals(Matrix m, double delta) {
    if ((m.getNumRows() != this.getNumRows()) || (m.getNumColumns() != this.getNumColumns())) {
      return false;
    }

    int rows = m.getNumRows();
    int columns = m.getNumColumns();

    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        if (Math.abs(this.get(row, column) - m.get(row, column)) > delta) {
          return false;
        }
      }
    }
    return true;
  }
}
