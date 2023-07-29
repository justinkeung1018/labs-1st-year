/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package uk.ac.imperial.matrixmult;

import java.util.Random;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Methods annotated with @Benchmark will be benchmarked by JMH.
 *
 * <p>To compile: mvn clean install This command will generate a "benchmarks.jar" inside the
 * "target" folder.
 *
 * <p>To run benchmarks: java -jar target/benchmarks.jar -f 1 -to 60s -foe true -tu ns Run the jar
 * with '-h' for more details about the command line flags
 */
public class MatrixBenchmark {

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void testMethod(Blackhole bh) throws Exception {

    // DENSE MATRIX BENCHMARK

    Matrix da = create(953, 1492, 123456789);
    Matrix db = create(1492, 833, 987654321);

    Matrix dres = MatrixMultiplier.multiply(da, db);

    // SPARSE MATRIX CHECK

    Matrix sa = create(1150, 1190, 523452365, true, 100);
    Matrix sb = create(1190, 1000, 472676354, true, 100);

    Matrix sres = MatrixMultiplier.multiply(da, db);

    // bh.consume(z);
  }

  public static Matrix create(int nRows, int nCols, long seed) {
    return create(nRows, nCols, seed, false, 1);
  }

  public static Matrix create(int nRows, int nCols, long seed, boolean sparse, int sparsity) {
    Matrix matrix;

    Random rnd = new Random(seed);

    if (!sparse) {
      matrix = MatrixBuilder.build(nRows, nCols);

      for (int x = 0; x < nRows; x++) {
        for (int y = 0; y < nCols; y++) {
          matrix.set(x, y, rnd.nextDouble());
        }
      }

      return matrix;
    } else {
      matrix = MatrixBuilder.build(nRows, nCols);
      int shots = nRows * nCols / sparsity;
      for (int n = shots; n > 0; n--) {
        int x = rnd.nextInt(nRows);
        int y = rnd.nextInt(nCols);
        double value = rnd.nextDouble();
        matrix.set(x, y, value);
      }

      return matrix;
    }
  }
}
