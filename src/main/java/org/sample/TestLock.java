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

public class TestLock {
  
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @Threads(10)
  public void lock(BenchMarkState state){
    while(state.intLock < 100000){
        state.lock.lock();
        state.intLock++;
        state.lock.unlock();
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @Threads(10)
  public void synchonized(BenchMarkState state){
    while(state.intSync < 100000){
      synchronized (state.LOCK) {
        if(state.intSync < 100000)
          state.intSync += 1;
      }
    }
  }
  @State(Scope.Benchmark)
  public static class BenchMarkState {
    @Setup(Level.Trial)
    public void doSetup() {
    	intSync = 0;
    	intLock = 0;
    }
//    @TearDown(Level.Trial)
//    public void doTearDown() {
//      atomic.set(0);
//      integer = 0;
//    }
    public ReentrantLock lock = new ReentrantLock();
    public Object LOCK = new Object();
    public Integer intSync = new Integer(0);
    public Integer intLock = new Integer(0);
  }
}
