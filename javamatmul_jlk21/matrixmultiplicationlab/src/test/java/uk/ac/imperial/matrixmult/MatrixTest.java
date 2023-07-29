package uk.ac.imperial.matrixmult;

import org.junit.Assert;
import org.junit.Test;

public class MatrixTest {

  @Test
  public void basicCheck() {
    int rows = 10;
    int cols = 10;
    Matrix m = MatrixBuilder.build(rows, cols);
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        m.set(row, col, col * cols + row);
      }
    }

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Assert.assertEquals(col * cols + row, m.get(row, col), 0.0001);
      }
    }
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void rowIndexExceptionChk() {
    int rows = 10;
    int cols = 10;
    Matrix m = MatrixBuilder.build(rows, cols - 1);
    m.set(rows, cols, 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void columnIndexExceptionChk() {
    int rows = 10;
    int cols = 10;
    Matrix m = MatrixBuilder.build(rows - 1, cols);
    m.set(rows, cols, 0.0);
  }
}
