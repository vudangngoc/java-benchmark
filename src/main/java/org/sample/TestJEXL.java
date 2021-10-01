package org.sample;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

public class TestJEXL {
	
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void normalCode(BenchMarkState state,Blackhole blackhole){
	  for(int i = 0; i < 100000; i++) {
		    blackhole.consume((state.random.nextInt(1000) + state.random.nextInt(1000))*state.random.nextInt(1000));
	  }
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void jexl(BenchMarkState state,Blackhole blackhole){
	  for(int i = 0; i < 100000; i++) {
		  JexlContext context = new MapContext();
		    context.set("a", state.random.nextInt(1000));
		    context.set("b", state.random.nextInt(1000));
		    context.set("c", state.random.nextInt(1000));
		    blackhole.consume(state.expression.evaluate(context));
	  }
	  
  }
  //Benchmark            Mode  Cnt   Score   Error  Units
  //TestJEXL.jexl        avgt  200  29.746 ± 0.545  ms/op
  //TestJEXL.normalCode  avgt  200   2.951 ± 0.024  ms/op
  @State(Scope.Benchmark)
  public static class BenchMarkState {
	  private Random random = new Random();
	  private JexlEngine jexl = new JexlBuilder().cache(512).strict(true).silent(false).create();
	  private JexlExpression expression;
    @Setup(Level.Trial)
    public void doSetup() {
    	 expression = jexl.createExpression( "(a + b)*c" );
    }
    
  }
}
