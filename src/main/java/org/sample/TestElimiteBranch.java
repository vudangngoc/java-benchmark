package org.sample;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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
import org.openjdk.jmh.infra.Blackhole;

public class TestElimiteBranch {
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void ifToCheck(BenchMarkState state,Blackhole blackhole){
	  long result = 0;
	  int temp;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	temp = state.data[i];
    	if(temp % 2 == 1) {
    		result += temp;
    	}
    }
    blackhole.consume(result);
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void checkByBitOperation(BenchMarkState state,Blackhole blackhole){
	  long result = 0;
	  int temp;
	  int odd;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	temp = state.data[i];
    	odd = temp & 1;
    	result += temp*odd;
    }
    blackhole.consume(result);
  }
//  Benchmark                              Mode  Cnt  Score   Error  Units
//  TestElimiteBranch.checkByBitOperation  avgt  200  0.563 ± 0.005  ms/op
//  TestElimiteBranch.ifToCheck            avgt  200  4.666 ± 0.022  ms/op

  @State(Scope.Benchmark)
  public static class BenchMarkState {
    @Setup(Level.Trial)
    public void doSetup() {
    	resetData();
    }
    @TearDown(Level.Trial)
    public void doTearDown() {
    	resetData();
    }
    private void resetData(){
    	data = new int[BIG_NUMBER];
    	for(int i = 0; i < BIG_NUMBER; i++){
    		data[i] = rand.nextInt();
    	}
    	
    }
    public final int BIG_NUMBER = 1000000;
    public final Random rand = new Random();
    public int[] data = new int[BIG_NUMBER];
  }
}
