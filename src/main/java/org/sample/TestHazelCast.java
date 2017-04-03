package org.sample;

import java.util.UUID;
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

import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class TestHazelCast {
  
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @Threads(10)
  public void concurrentHashMap(BenchMarkState state){
	  if(state.chmWrite.size() < 100000)
		  state.chmWrite.put(UUID.randomUUID().toString(), UUID.randomUUID());
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @Threads(10)
  public void hazelcast(BenchMarkState state){
	  if(state.mapWrite.size() < 100000)
		  state.mapWrite.put(UUID.randomUUID().toString(), UUID.randomUUID());
  }
  @State(Scope.Benchmark)
  public static class BenchMarkState {
    @Setup(Level.Trial)
    public void doSetup() {
    	mapWrite = instance.getMap("write");
    	mapRead = instance.getMap("read");
    }
    @TearDown
    public void tearDown(){
    	instance.shutdown();
    }

    private final HazelcastInstance instance = Hazelcast.newHazelcastInstance(new XmlConfigBuilder().build());
    public IMap<String, Object> mapWrite;
    public IMap<String, Object> mapRead;
    public ConcurrentHashMap<String, Object> chmRead = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> chmWrite = new ConcurrentHashMap<>();
  }
}
