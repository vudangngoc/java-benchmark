package org.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class TestInlineFunction {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public int smallFunction() {
		int result = 0;
		for(int i = 0; i < 10000000; i++) {
			result = function(i);
		}
		return result;
	}
	private int function(int i) {
		int temp = i/2;
		return temp+ temp;
	}
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	
	public int inlineFunction() {
		int result = 0;
		int temp;
		for(int i = 0; i < 10000000; i++) {
			temp = i/2;
			result = temp + temp;
		}
		return result;
	}
//	Benchmark                    Mode  Cnt   Score   Error  Units
//	TestMemoryLayout.columnWise  avgt  200  15.179 ± 0.087  ms/op
//	TestMemoryLayout.rowWise     avgt  200   8.395 ± 0.104  ms/op

}
