package uk.ac.imperial.matrixmult;

import org.junit.Assert;
import org.junit.Test;

public class MatrixMultiplierTest {

  @Test
  public void basicCheck() throws Exception {
    Matrix m = MatrixBuilder.build(1, 1);
    m.set(0, 0, 3.0);
    Matrix n = MatrixBuilder.build(1, 1);
    n.set(0, 0, 5.0);

    Matrix result = MatrixMultiplier.multiply(m, n);

    Assert.assertEquals(15.0, result.get(0, 0), 0.00001);
  }

  @Test
  public void identityCheck() throws Exception {
    int size = 10;

    Matrix m = MatrixBuilder.build(size, size);
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        m.set(row, col, col * size + row);
      }
    }

    Matrix id = MatrixBuilder.build(size, size);
    for (int n = 0; n < size; n++) {
      id.set(n, n, 1.0);
    }

    Matrix result = MatrixMultiplier.multiply(m, id);

    Assert.assertTrue(result.equals(m, 0.000001));
  }

  @Test
  public void zeroCheck() throws Exception {
    int size = 10;

    Matrix m = MatrixBuilder.build(size, size);
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        m.set(row, col, col * size + row);
      }
    }

    Matrix zero = MatrixBuilder.build(size, size);

    Matrix result = MatrixMultiplier.multiply(m, zero);

    Assert.assertTrue(result.equals(zero, 0.000001));
  }

  @Test
  public void associativityCheck() throws Exception {
    int size = 10;

    Matrix a = MatrixBenchmark.create(size + 10, size + 5, 34286073);
    Matrix b = MatrixBenchmark.create(size + 5, size + 30, 92830465);
    Matrix c = MatrixBenchmark.create(size + 30, size, 71539248);

    Matrix axb = MatrixMultiplier.multiply(a, b);
    Matrix bxc = MatrixMultiplier.multiply(b, c);

    Matrix r1 = MatrixMultiplier.multiply(axb, c);
    Matrix r2 = MatrixMultiplier.multiply(a, bxc);

    Assert.assertTrue(r1.equals(r2, 0.000001));
  }

  @Test
  public void computationCheck() throws Exception {
    int size = 10;

    double[][] a = {
      {1.0, 2.0, 3.0},
      {4.0, 5.0, 6.0}
    };

    double[][] b = {
      {7.0, 8.0},
      {9.0, 10.0},
      {11.0, 12.0}
    };

    double[][] c = {
      {58.0, 64.0},
      {139.0, 154.0}
    };

    Matrix ma = MatrixBuilder.build(a);
    Matrix mb = MatrixBuilder.build(b);
    Matrix mc = MatrixBuilder.build(c);

    Matrix res = MatrixMultiplier.multiply(ma, mb);

    Assert.assertTrue(res.equals(mc, 0.000001));
  }
}
