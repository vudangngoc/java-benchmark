package org.sample;

import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class TestHazelcastVsIgnite {

    public static void main(String...strings){
        BenchMarkState test = new BenchMarkState();
        test.doSetup();
        while (true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

  @Benchmark
  @Threads(10)
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void getLockHazelcast(BenchMarkState state,Blackhole blackhole){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(100));
        Lock lock = state.instance.getCPSubsystem().getLock(value.toString());
        try {
            if(lock.tryLock(1000,TimeUnit.MILLISECONDS)){
                try {
                    blackhole.consume(value);
                }finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void getIgniteCache(BenchMarkState state,Blackhole blackhole){
	  Integer value;
    for(int i = 0; i < state.BIG_NUMBER;i++) {
    	value = Integer.valueOf(state.rand.nextInt(state.BIG_NUMBER - 1));
        blackhole.consume(state.igniteCache.get(value));
        Lock lock = state.igniteCache.lock(value);
        try {
            if(lock.tryLock(1000,TimeUnit.MILLISECONDS)){
                try {
                    blackhole.consume(value);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
  }

//    Benchmark                               Mode  Cnt    Score    Error  Units
//    TestHazelcastVsIgnite.getIgniteCache    avgt  200  296.758 ± 23.598  ms/op
//    TestHazelcastVsIgnite.getLockHazelcast  avgt  200  416.350 ± 27.804  ms/op
  @State(Scope.Benchmark)
  public static class BenchMarkState {
    @Setup(Level.Trial)
    public void doSetup() {
        CacheConfiguration cacheCfg = new CacheConfiguration();
        cacheCfg.setName("test-integer-map");
        cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setCacheConfiguration(cacheCfg);
        ignite = Ignition.start(cfg);
        igniteCache = ignite.getOrCreateCache("test-integer-map");

        instance = Hazelcast.newHazelcastInstance(new XmlConfigBuilder().build());
        instance.getList("read").add(1);
        System.out.println("Hazelcast is start");
    }
    @TearDown(Level.Trial)
    public void doTearDown() {
        ignite.close();
        instance.shutdown();
    }
    public final int BIG_NUMBER = 1000;
    public final Random rand = new Random();
    private HazelcastInstance instance;
    private Ignite ignite;
    private IgniteCache igniteCache;
  }
}
