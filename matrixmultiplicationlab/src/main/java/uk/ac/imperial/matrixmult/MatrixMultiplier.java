package uk.ac.imperial.matrixmult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplier {

  public static Matrix multiply(Matrix a, Matrix b) throws Exception {
    assert a != null;
    assert b != null;
    assert a.getNumColumns() == b.getNumRows();
    int resultNumRows = a.getNumRows();
    int resultNumCols = b.getNumColumns();
    Matrix result = new MatrixArray(resultNumRows, resultNumCols);
    ExecutorService executor = Executors.newFixedThreadPool(7);
    for (int row = 0; row < resultNumRows; row++) {
      for (int col = 0; col < resultNumCols; col++) {
        executor.execute(new MatrixCellMultiplier(a, b, result, row, col));
      }
    }
    executor.shutdown();
    // The code below is taken from Oracle docs on ExecutorService
    try {
      // Wait a while for existing tasks to terminate
      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow(); // Cancel currently executing tasks
        // Wait a while for tasks to respond to being cancelled
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          System.err.println("Pool did not terminate");
        }
      }
    } catch (InterruptedException ie) {
      // (Re-)Cancel if current thread also interrupted
      executor.shutdownNow();
      // Preserve interrupt status
      Thread.currentThread().interrupt();
    }
    return result;
  }

  //  public static Matrix multiply(Matrix a, Matrix b) throws Exception {
  //    if (a.getNumColumns() != b.getNumRows()) {
  //      throw new IllegalArgumentException("Matrix dimensions do not match");
  //    }
  //    int resultNumRows = a.getNumRows();
  //    int resultNumCols = b.getNumColumns();
  //    Matrix result = new MatrixArray(resultNumRows, resultNumCols);
  //    for (int row = 0; row < resultNumRows; row++) {
  //      for (int col = 0; col < resultNumCols; col++) {
  //        double value = 0;
  //        for (int n = 0; n < a.getNumColumns(); n++) {
  //          value += a.get(row, n) * b.get(n, col);
  //        }
  //        result.set(row, col, value);
  //      }
  //    }
  //    return result;
  //  }
}
