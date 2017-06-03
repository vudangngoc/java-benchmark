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
		for(int j = 0; j < size; j ++){
			state.testData.get(j);
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
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void streamMultiThread(BenchMarkState state){
		state.testData.stream().parallel().forEach(item -> {
			// Do something
		});
	}
//	Benchmark                               Mode  Cnt        Score       Error  Units
//	TestLoopPerformance.forCStyle           avgt  200        3.182 ±     0.009  ns/op
//	TestLoopPerformance.forEach             avgt  200  7693143.747 ± 57712.787  ns/op
//	TestLoopPerformance.streamMultiThread   avgt  200  6974017.140 ± 66200.317  ns/op
//	TestLoopPerformance.streamSingleThread  avgt  200  7790435.456 ± 75683.894  ns/op
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
