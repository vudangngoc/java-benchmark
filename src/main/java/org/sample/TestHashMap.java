package org.sample;

import java.util.HashMap;
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

public class TestHashMap {
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @Threads(10)
  public void concurrentHashMap(BenchMarkState state){
    while(state.chm.get("1") < 100000)
      state.chm.put("1", state.chm.get("1") + 1);
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @Threads(10)
  public void hashMap(BenchMarkState state){
    while(state.hm.get("1") < 100000){
      synchronized (state.hm) {
        if(state.hm.get("1") < 100000)
          state.hm.put("1", state.hm.get("1") + 1);
      }
    }
  }
  @State(Scope.Benchmark)
  public static class BenchMarkState {
    @Setup(Level.Trial)
    public void doSetup() {
      hm.put("1",  1);
      chm.put("1",  1);
    }
    @TearDown(Level.Trial)
    public void doTearDown() {
      hm.put("1",  1);
      chm.put("1",  1);
    }
    public HashMap<String, Integer> hm = new HashMap<>();
    public ConcurrentHashMap<String, Integer> chm = new ConcurrentHashMap<>();
  }
}
