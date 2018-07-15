package org.sample;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class TestLocalVariable {
  
//  @Benchmark
//  @BenchmarkMode(Mode.AverageTime)
//  @OutputTimeUnit(TimeUnit.NANOSECONDS)
//  public void external(BenchMarkState state){
//	int temp = 0;
//    for(int i = 0; i < state.integer; i++)
//      temp++;
//  }
//  @Benchmark
//  @BenchmarkMode(Mode.AverageTime)
//  @OutputTimeUnit(TimeUnit.NANOSECONDS)
//  public void internal(BenchMarkState state){
//		int temp = 0;
//		int barrier = state.integer;
//	    for(int i = 0; i < barrier; i++)
//	      temp++;
//  }
  @State(Scope.Benchmark)
  public static class BenchMarkState {
    public int integer = 4000000;
  }
}
