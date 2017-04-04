package org.sample;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
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
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;

public class TestHazelCast {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void linkedList(BenchMarkState state){
		Iterator<String> temp = state.chmRead.iterator();
		while(temp.hasNext())
			temp.next();
			
	}
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void hazelcastList(BenchMarkState state){
		Iterator<String> temp = state.mapRead.iterator();
		while(temp.hasNext())
			temp.next();
	}
//	Benchmark                    Mode  Cnt         Score        Error  Units
//	TestHazelCast.hazelcastList  avgt  200  15148252.440 ± 434565.403  ns/op
//	TestHazelCast.linkedList     avgt  200   1245878.630 ±  45909.236  ns/op
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		@Setup(Level.Trial)
		public void doSetup() {
			mapRead = instance.getList("read");
			
			while(mapRead.size() < 100000){
				chmRead.push(UUID.randomUUID().toString());
				mapRead.add(UUID.randomUUID().toString());
			}

		}
		@TearDown
		public void tearDown(){
			instance.shutdown();
		}

		private final HazelcastInstance instance = Hazelcast.newHazelcastInstance(new XmlConfigBuilder().build());
		public IList<String> mapRead;
		public LinkedList<String> chmRead = new LinkedList<>();
	}
}
