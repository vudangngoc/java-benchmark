package org.sample;

import org.openjdk.jmh.annotations.*;
import net.openhft.chronicle.map.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestChronicleMap {

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void putHashMap(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
    	state.hashMap.put(value, value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void getHashMap(BenchMarkState state,Blackhole blackhole){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
        blackhole.consume(state.hashMap.get(value));
    }
  }
//    Benchmark                         Mode  Cnt    Score   Error  Units
//    TestChronicleMap.getChronicleMap  avgt  200  231.499 ± 2.295  ms/op
//    TestChronicleMap.getHashMap       avgt  200  101.433 ± 1.353  ms/op
//    TestChronicleMap.putChronicleMap  avgt  200  404.672 ± 4.860  ms/op
//    TestChronicleMap.putHashMap       avgt  200  170.291 ± 6.682  ms/op
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void putChronicleMap(BenchMarkState state){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
    	state.chronicleMap.put(value, value);
    }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void getChronicleMap(BenchMarkState state,Blackhole blackhole){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
        blackhole.consume(state.chronicleMap.get(value));
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
    	chronicleMap.close();
    }
    private void resetData(){
        hashMap = new HashMap<>(BIG_NUMBER);
        chronicleMap = ChronicleMapBuilder
                .of(Integer.class, Integer.class)
                .name("test-integer-map")
                .entries(BIG_NUMBER)
                .create();

    	for(int i = 0; i < BIG_NUMBER; i++){
    		final Integer value = Integer.valueOf(i);
            hashMap.put(value, value);
            chronicleMap.put(value, value);
    	}
    	
    }
    public final int BIG_NUMBER = 1000000;
    public final Random rand = new Random();
    public Map<Integer, Integer> hashMap = new HashMap<>(BIG_NUMBER);

    ChronicleMap<Integer, Integer> chronicleMap = ChronicleMapBuilder
            .of(Integer.class, Integer.class)
            .name("test-integer-map")
            .entries(BIG_NUMBER)
            .create();
  }
}
