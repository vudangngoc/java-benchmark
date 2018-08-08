package org.sample;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

public class TestMemoryLocalization {
  
  
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public int localData(BenchMarkState state){
	    int result = 0;
	    final int[] local1 = state.localData[0];
	    final int[] local2 = state.localData[1];
	    for(int i = 0; i < state.size; i++) {
	    	result = local1[i] - local2[i];	    	
	    }
	    return result;
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public int fragmentData(BenchMarkState state){
    int result = 0;
    for(int i = 0; i < state.size; i++) {
    	result = state.fragmentData.data1[i] - state.fragmentData.data2[i];
    }
    return result;
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public int fragmentDataWithPadding(BenchMarkState state){
    int result = 0;
    for(int i = 0; i < state.size; i++) {
    	result = state.fragmentDataWithPadding.data1[i] - state.fragmentDataWithPadding.data2[i];
    }
    return result;
  }
//  Benchmark                            Mode  Cnt  Score   Error  Units
//  TestMemoryLocalization.fragmentData  avgt  200  2.411 ± 0.035  ns/op
//  TestMemoryLocalization.localData     avgt  200  2.554 ± 0.043  ns/op
  @State(Scope.Benchmark)
  public static class BenchMarkState {
   
//    @Setup(Level.Trial)
//    public void doSetup() {
//    	 for(int i = 0; i < size; i++) {
//    		 localData[0][i] = i;
//    		 localData[1][i] = i;
//    		 fragmentData1[i] = i;
//    		 fragmentData2[i] = i;
////    		 align[i] = i;
//    	 }
//    }
    public static int size = 1024;
    public int[][] localData = new int[2][size];
    public FragmentData fragmentData = new FragmentData();
    public FragmentDataWithPadding fragmentDataWithPadding = new FragmentDataWithPadding();
    
    public static class FragmentData{
    	public int[] data1 = new int[size];
      public int[] data2 = new int[size];
    }
    public static class FragmentDataWithPadding{
    	public int[] data1 = new int[size];
    	private int[] align = new int[size*100];
      public int[] data2 = new int[size];
      public String toString() {
    	  return "" + align.length;
      }
    }
  }
}
