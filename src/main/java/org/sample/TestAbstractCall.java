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
import org.openjdk.jmh.annotations.TearDown;

public class TestAbstractCall {

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public long abstractList(BenchMarkState state){
	  Integer temp = Integer.valueOf(0);
	 for(int i = 0; i < state.SIZE; i++)
		 state.abstractList.add(temp);
	  return state.abstractList.size();
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public long concreteArrayList(BenchMarkState state){
	  Integer temp = Integer.valueOf(0);
	  for(int i = 0; i < state.SIZE; i++)
			 state.concreteArrayList.add(temp);
	  return state.concreteArrayList.size();
  }
//  Benchmark                           Mode  Cnt  Score   Error  Units
//  TestAbstractCall.abstractList       avgt  200  3.157 ± 0.021  ms/op
//  TestAbstractCall.concreteArrayList  avgt  200  2.888 ± 0.013  ms/op
  @State(Scope.Benchmark)
  public static class BenchMarkState {
	  @Setup(Level.Invocation)
		    public void doSetup() {
		  abstractList = new ArrayList<Integer>(SIZE);
		  concreteArrayList = new ArrayList<Integer>(SIZE);
	  }
	  @TearDown(Level.Invocation)
	  public void tearDown() {
		  abstractList.clear();
		  concreteArrayList.clear();
	  }
	  public  int SIZE = 1000000;
	  public  List<Integer> abstractList;
	  public  ArrayList<Integer> concreteArrayList;
  }
	
}
