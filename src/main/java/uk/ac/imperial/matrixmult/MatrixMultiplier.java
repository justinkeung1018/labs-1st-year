package uk.ac.imperial.matrixmult;

public class MatrixMultiplier {

  public static Matrix multiply(Matrix a, Matrix b) throws Exception {
    if (a.getNumColumns() != b.getNumRows()) {
      throw new IllegalArgumentException("Matrix dimensions do not match");
    }
    int resultNumRows = a.getNumRows();
    int resultNumCols = b.getNumColumns();
    Matrix result = new MatrixArray(resultNumRows, resultNumCols);
    for (int row = 0; row < resultNumRows; row++) {
      for (int col = 0; col < resultNumCols; col++) {
        double value = 0;
        for (int n = 0; n < a.getNumColumns(); n++) {
          value += a.get(row, n) * b.get(n, col);
        }
        result.set(row, col, value);
      }
    }
    return result;
  }
}
