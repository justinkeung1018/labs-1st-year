package uk.ac.imperial.matrixmult;

public class MatrixCellMultiplier implements Runnable {
  private final Matrix a;
  private final Matrix b;
  private final Matrix result;
  private final int row;
  private final int col;

  public MatrixCellMultiplier(Matrix a, Matrix b, Matrix result, int row, int col) {
    this.a = a;
    this.b = b;
    this.result = result;
    this.row = row;
    this.col = col;
  }

  @Override
  public void run() {
    double value = 0;
    for (int i = 0; i < a.getNumColumns(); i++) {
      value += a.get(row, i) * b.get(i, col);
    }
    result.set(row, col, value);
  }
}
