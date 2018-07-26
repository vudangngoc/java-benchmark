/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package org.sample;

import java.util.*;
import java.util.concurrent.*;
import org.openjdk.jmh.annotations.*;
import reactor.core.publisher.*;

/**
 */
public class TestReactor
{
	
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public List<Integer> singleThread(BenchMarkState state) {
		List<Integer> result = new ArrayList<>(state.size);
		state.stream.subscribe(result::add); // nothing happen until subscribe
		return result;
	}
//	Benchmark                       Mode  Cnt  Score   Error  Units
//	TestLMAXDisruptor.singleThread  avgt  200  8.237 ± 0.159  ms/op
//	TestReactor.singleThread        avgt  200  7.402 ± 0.475  ms/op
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		public Flux<Integer> stream;
		@Setup(Level.Trial)
		public void doSetup() {
			for(int i = 0; i < size; i ++)
				data.add(Integer.valueOf(i));
		}
		@Setup(Level.Invocation)
		public void setupEachIteration() {
			stream = Flux
					.fromIterable(data);
		}
		public int size = 1000000;
		public List< Integer> data = new ArrayList<Integer>(size);
	}

}
