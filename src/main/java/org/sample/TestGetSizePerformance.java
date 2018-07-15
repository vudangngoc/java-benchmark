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

public class TestGetSizePerformance {

//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
//	public void getSize(BenchMarkState state){
//		for(int j = 0; j < state.testData.size(); j ++){
//			state.testData.get(j);
//			// Do something
//		}
//	}
//
//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
//	public void forCStyle(BenchMarkState state){
//		int size = state.testData.size();
//		for(int j = 0; j < size; j ++){
//			state.testData.get(j);
//			// Do something
//		}
//	}
//	Benchmark                         Mode  Cnt  Score   Error  Units
//	TestGetSizePerformance.forCStyle  avgt  200  4.326 ▒ 0.020  ns/op
//	TestGetSizePerformance.getSize    avgt  200  3.869 ▒ 0.012  ns/op

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
