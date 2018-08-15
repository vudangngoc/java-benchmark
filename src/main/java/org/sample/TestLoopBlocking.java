package org.sample;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class TestLoopBlocking {
  
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public int naiveColumn(BenchMarkState state){
	  long count = 0;
	  int result = 0;
  	for(int i = 0; i < 1024; i++)
		for(int j = 0; j < 1024; j++) {
			result = state.data1[j][i];
			count++;
		}
  	if(count != 1024*1024)
		throw new IllegalAccessError(count + "");
  	return result;
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public int naiveRow(BenchMarkState state){
	  long count = 0;
	  int result = 0;
  	for(int i = 0; i < 1024; i++)
		for(int j = 0; j < 1024; j++) {
			result = state.data1[i][j];
			count++;
		}
  	if(count != 1024*1024)
		throw new IllegalAccessError(count + "");
  	return result;
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public int blockingColumn(BenchMarkState state){
	  	int blockSize = 32;
	  	long count = 0;
	  	int result = 0;
		for(int ii = 0; ii < 1024; ii+=blockSize)
			for(int jj = 0; jj < 1024; jj+=blockSize) {
				int i_upper = (ii + blockSize ) < 1024 ? (ii + blockSize ): 1024;
				int j_upper = (jj + blockSize ) < 1024 ? (jj + blockSize ): 1024;
				for(int i = ii; i < i_upper; i++)
					for(int j = jj; j < j_upper; j++) {
						result = state.data1[j][i];
						count++;
					}
			}
		if(count != 1024*1024)
			throw new IllegalAccessError(count + "");
		
    return result;
  }
//  Benchmark                        Mode  Cnt  Score   Error  Units
//  TestLoopBlocking.blockingColumn  avgt  200  0.799 ± 0.007  ms/op
//  TestLoopBlocking.naiveColumn     avgt  200  7.077 ± 0.039  ms/op
//  TestLoopBlocking.naiveRow        avgt  200  0.054 ± 0.001  ms/op
  @State(Scope.Benchmark)
  public static class BenchMarkState {
    @Setup(Level.Trial)
    public void doSetup() {
    	for(int i = 0; i < 1024; i++)
    		for(int j = 0; j < 1024; j++)
    			data1[i][j] = i + j;
    }
//    @TearDown(Level.Trial)
//    public void doTearDown() {
//      atomic.set(0);
//      integer = 0;
//    }
    public int[][] data1 = new int[1024][1024];
//    public int[][] data2 = new int[1024][1024];
  }
}
