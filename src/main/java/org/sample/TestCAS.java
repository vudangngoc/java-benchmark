package org.sample;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

public class TestCAS {
  
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @Threads(10)
  public void atomic(BenchMarkState state){
    while(state.atomic.get() < 100000)
      while(true){
        int temp = state.atomic.get();
        if(temp >= 100000 || state.atomic.compareAndSet(temp, temp + 1))
          break;
      }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @Threads(10)
  public void integer(BenchMarkState state){
    while(state.integer < 100000){
      synchronized (state.LOCK) {
        if(state.integer < 100000)
          state.integer += 1;
      }
    }
  }
  @State(Scope.Benchmark)
  public static class BenchMarkState {
    @Setup(Level.Trial)
    public void doSetup() {
      atomic.set(0);
      integer = 0;
    }
//    @TearDown(Level.Trial)
//    public void doTearDown() {
//      atomic.set(0);
//      integer = 0;
//    }
    public Object LOCK = new Object();
    public AtomicInteger atomic = new AtomicInteger(0);
    public Integer integer = new Integer(0);
  }
}
