package org.sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
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

public class TestSetPerformance {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void forEach(BenchMarkState state){
		for(Integer temp : state.testData){
			// Do something
		}
	}
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void forCStyle(BenchMarkState state){
		int size = state.testData.size();
		List<Integer> temp = new ArrayList<>(state.testData);
		for(int j = 0; j < size; j ++){
			temp.get(j);
			// Do something
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void streamSingleThread(BenchMarkState state){
		state.testData.stream().forEach(item -> {
			// Do something
		});

	}
//	Benchmark                              Mode  Cnt         Score        Error  Units
//	TestSetPerformance.forCStyle           avgt  200  30932686.838 ± 324392.325  ns/op
//	TestSetPerformance.forEach             avgt  200  28612117.566 ± 282661.493  ns/op
//	TestSetPerformance.streamSingleThread  avgt  200  27928042.863 ± 269328.059  ns/op
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		@Setup(Level.Trial)
		public void doSetup() {
			for(int i = 0; i < 5000000; i++)
				testData.add(Integer.valueOf(i));
		}
		@TearDown(Level.Trial)
		public void doTearDown() {
			testData = new HashSet<>(5000000);
		}
		public Set<Integer> testData = new HashSet<>();
	}

}
