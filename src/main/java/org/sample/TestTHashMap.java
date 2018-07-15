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

public class TestTHashMap {
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void putHashMapWithSmallSize(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(31));
    	state.hms.put(value, value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void getHashMapWithSmallSize(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(31));
    	state.hms.get(value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void putTHashMapWithSmallSize(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(31));
    	state.thms.put(value, value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void getTHashMapWithSmallSize(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(31));
    	state.thms.get(value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void putHashMapWithLargeSize(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
    	state.hml.put(value, value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void getHashMapWithLargeSize(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
    	state.hml.get(value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void putTHashMapWithLargeSize(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
    	state.thml.put(value, value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void getTHashMapWithLargeSize(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
    	state.thml.get(value);
    }
  }
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
    	hms = new HashMap<>(32);
    	thms = new gnu.trove.map.hash.THashMap<>(32);
    	for(int i = 0; i < 32; i++){
    		final Integer value = Integer.valueOf(i);
			hms.put(value, value);
			thms.put(value, value);
    	}
    	hml = new HashMap<>(BIG_NUMBER);
    	thml = new gnu.trove.map.hash.THashMap<>(BIG_NUMBER);
    	for(int i = 0; i < BIG_NUMBER; i++){
    		final Integer value = Integer.valueOf(i);
			hml.put(value, value);
			thml.put(value, value);
    	}
    	
    }
    public final int BIG_NUMBER = 1000000;
    public final Random rand = new Random();
    public java.util.Map<Integer, Integer> hms = new HashMap<>(32);
    public java.util.Map<Integer, Integer> hml = new HashMap<>(BIG_NUMBER);
    public java.util.Map<Integer, Integer> thms = new gnu.trove.map.hash.THashMap<>(32);
    public java.util.Map<Integer, Integer> thml = new gnu.trove.map.hash.THashMap<>(BIG_NUMBER);
  }
}
