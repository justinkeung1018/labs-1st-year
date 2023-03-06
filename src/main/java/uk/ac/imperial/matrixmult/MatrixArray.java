package uk.ac.imperial.matrixmult;

public class MatrixArray implements Matrix {
  private final double[][] matrix;

  public MatrixArray(double[][] source) {
    if (source.length == 0 || source[0].length == 0) {
      throw new IllegalArgumentException("Source must be non-empty");
    }
    matrix = source;
  }

  public MatrixArray(int nRows, int nCols) {
    if (nRows <= 0 || nCols <= 0) {
      throw new IllegalArgumentException("Matrix must have at least one row and one column");
    }
    matrix = new double[nRows][nCols];
  }

  @Override
  public double get(int row, int column) {
    return matrix[row][column];
  }

  @Override
  public void set(int row, int column, double value) {
    matrix[row][column] = value;
  }

  @Override
  public int getNumRows() {
    return matrix.length;
  }

  @Override
  public int getNumColumns() {
    return matrix[0].length;
  }
}
