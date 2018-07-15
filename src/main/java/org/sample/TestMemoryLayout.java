package org.sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

public class TestMemoryLayout {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public List<Integer> rowWise(BenchMarkState state){
		int size = state.testData.length;
		List<Integer> result = new ArrayList<>(size);		
		for(int i = 0; i<1024; i++) 
			for(int j = 0; j < size; j ++)
				result.add(state.testData[i][j]);
		return result;
	}
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public List<Integer> columnWise(BenchMarkState state){
		int size = state.testData.length;
		List<Integer> result = new ArrayList<>(size);		
		for(int j = 0; j < size; j ++)
			for(int i = 0; i<1024; i++)
				result.add(state.testData[i][j]);

		return result;
	}
//	Benchmark                    Mode  Cnt   Score   Error  Units
//	TestMemoryLayout.rowWise     avgt  200   8.744 ± 0.127  ms/op
//	TestMemoryLayout.columnWise  avgt  200  15.429 ± 0.102  ms/op
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		@Setup(Level.Trial)
		public void doSetup() {
			for(int i = 0; i < 1024; i++)
				for(int j = 0; j < 1024; j++)
					testData[i][j] = 0;

		}

		public int[][] testData = new int[1024][1024];
	}


}

