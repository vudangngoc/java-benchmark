package org.sample;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.sample.TestObjectArray.BenchMarkState.*;

public class TestObjectArray {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int intArray(BenchMarkState state){
		int result = 0;
		for(int i = 0; i < state.size; i++) {
			result = state.data1[i];
		}
		return result;
	}
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int integerArray(BenchMarkState state){
		int result = 0;
		for(int i = 0; i < state.size; i++) {
			result = state.data2[i].intValue();
		}
		return result;
	}

	
	
//	   size = 10240000
//		Benchmark                         Mode  Cnt        Score       Error  Units
//		TestObjectArray.intArray      avgt  200   213709.132 ±  4884.065  ns/op
//		TestObjectArray.integerArray  avgt  200  2521451.187 ± 14602.431  ns/op
//
//		size = 1024
//		Benchmark                         Mode  Cnt    Score   Error  Units
//		TestObjectArray.intArray      avgt  200   26.076 ± 0.084  ns/op
//		TestArrayAllocation.integerArray  avgt  200  154.461 ± 0.487  ns/op
//
//		size = 32
//		Benchmark                         Mode  Cnt  Score   Error  Units
//		TestObjectArray.intArray      avgt  200  9.291 ± 0.032  ns/op
//		TestObjectArray.integerArray  avgt  200  8.325 ± 0.017  ns/op
//
//		size = 16
//		Benchmark                         Mode  Cnt  Score   Error  Units
//		TestObjectArray.intArray      avgt  200  8.852 ± 0.256  ns/op
//		TestObjectArray.integerArray  avgt  200  5.774 ± 0.019  ns/op
		
	@State(Scope.Benchmark)
	public static class BenchMarkState {

		@Setup(Level.Trial)
		public void doSetup() {
			for(int i = 0; i < size; i++) {
				data2[i] = Integer.valueOf(0);
			}
		}
		public static int size = 16;
		public int[] data1 = new int[size];
		public int[] data3 = new int[10240000];
		public Integer[] data2 = new Integer[size];
	}
}
