package uk.ac.imperial.matrixmult;


public class MatrixBuilder {

  public static Matrix build(double[][] source) {
    return new MatrixArray(source);
  }

  public static Matrix build(int nRows, int nCols) {
    return new MatrixArray(nRows, nCols);
  }
}
