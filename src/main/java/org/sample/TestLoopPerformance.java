package org.sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

public class TestLoopPerformance {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public List<Integer> forEach(BenchMarkState state){
		List<Integer> result = new ArrayList<>(state.testData.size());
		for(Integer item : state.testData){
			result.add(item);
		}
		return result;
	}
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void forCStyle(BenchMarkState state){
		int size = state.testData.size();
		List<Integer> result = new ArrayList<>(size);
		for(int j = 0; j < size; j ++){
			result.add(state.testData.get(j));
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public List<Integer> streamSingleThread(BenchMarkState state){
		List<Integer> result = new ArrayList<>(state.testData.size());
		state.testData.stream().forEach(item -> {
			result.add(item);
		});
		return result;
	}
	
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public List<Integer> streamMultiThread(BenchMarkState state){
		List<Integer> result = new ArrayList<>(state.testData.size());
		state.testData.stream().parallel().forEach(item -> {
			result.add(item);
		});
		return result;
	}
//	Benchmark                               Mode  Cnt   Score   Error  Units
//	TestLoopPerformance.forCStyle           avgt  200  15.608 ± 0.079  ms/op
//	TestLoopPerformance.forEach             avgt  200  30.566 ± 0.165  ms/op
//	TestLoopPerformance.streamMultiThread   avgt  200  79.433 ± 0.747  ms/op
//	TestLoopPerformance.streamSingleThread  avgt  200  37.779 ± 0.485  ms/op
	
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		@Setup(Level.Trial)
		public void doSetup() {
			for(int i = 0; i < 5000000; i++)
				testData.add(Integer.valueOf(i));
		}
		@TearDown(Level.Trial)
		public void doTearDown() {
			testData = new ArrayList<>(5000000);
		}
		public List<Integer> testData = new ArrayList<>();
	}

}

