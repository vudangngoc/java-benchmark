package org.sample;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

public class TestExceptionPerformance {



	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int withException(BenchMarkState state){
		int result = 0;
		for(int i = 0; i < state.size; i++) {
			try
			{
				result = method1(i);
			}
			catch (Exception ex)
			{
				result = 1;
			}
		}
		return result;
	}
	private int method1(int i) throws Exception  {
		int result = i%2;
		if(result == 1) {
			throw new Exception();
		}
		return 0;
	}
	
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int withoutException(BenchMarkState state){
		int result = 0;
		for(int i = 0; i < state.size; i++) {
			result = method2(i);
		}
		return result;
	}
	private int method2(int i) {
		int result = i%2;
		if(result == 1) {
			return 1;
		}
		return 0;
	}
//	Benchmark                Mode  Cnt          Score         Error  Units
//	TestEP.withException     avgt  200  382963743.753 ± 2495954.533  ns/op
//	TestEP.withoutException  avgt  200          6.504 ±       0.034  ns/op
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		public int size = 1024000;
	}
}
