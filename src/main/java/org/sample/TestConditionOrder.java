package org.sample;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.*;

public class TestConditionOrder {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void naive(Blackhole bh1, Blackhole bh2){
		for(int i = 0; i < BenchMarkState.size; i++) {
			if( aHeavyComputation() || i%2 == 0) {
				bh1.consume(i);
			}else 
				bh2.consume(i);				
		}			
	}
	
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void optimized(Blackhole bh1, Blackhole bh2){
		for(int i = 0; i < BenchMarkState.size; i++) {
			if(i%2 == 0 || aHeavyComputation()) {
				bh1.consume(i);
			}else 
				bh2.consume(i);				
		}		
	}
	
	private boolean aHeavyComputation()
	{
		Random r = new Random();
		return r.nextInt(100) % 2 == 0;
	}
//	Benchmark                                Mode  Cnt   Score      Error   Units
//	TestConditionOrder.naive         avgt    200  40.761 ± 0.085  ms/op
//	TestConditionOrder.optimized  avgt    200  22.056 ± 0.087  ms/op
	
	
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		public static int size = 1024000;
	}
}
