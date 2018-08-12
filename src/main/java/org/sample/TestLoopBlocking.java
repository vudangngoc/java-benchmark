package org.sample;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;

public class TestLoopBlocking {
  
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public int[][] naive(BenchMarkState state){
	  long count = 0;
  	for(int i = 0; i < 1024; i++)
		for(int j = 0; j < 1024; j++) {
			state.data2[j][i] = state.data1[i][j];
			count++;
		}
  	if(count != 1024*1024)
		throw new IllegalAccessError(count + "");
  	return state.data2;
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public int[][] blocking(BenchMarkState state){
	  	int blockSize = 64;
	  	long count = 0;
		for(int ii = 0; ii < 1024; ii+=blockSize)
			for(int jj = 0; jj < 1024; jj+=blockSize) {
				int i_upper = (ii + blockSize -1) < 1024 ? (ii + blockSize ): 1024;
				int j_upper = (jj + blockSize -1) < 1024 ? (jj + blockSize ): 1024;
				for(int i = ii; i < i_upper; i++)
					for(int j = jj; j < j_upper; j++) {
						state.data2[j][i] = state.data1[i][j];
						count++;
					}
			}
		if(count != 1024*1024)
			throw new IllegalAccessError(count + "");
    return state.data2;
  }
//  Benchmark                  Mode  Cnt  Score   Error  Units
//  TestLoopBlocking.blocking  avgt  200  1.835 ± 0.016  ms/op
//  TestLoopBlocking.naive     avgt  200  8.031 ± 0.066  ms/op
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
    public int[][] data2 = new int[1024][1024];
  }
}
