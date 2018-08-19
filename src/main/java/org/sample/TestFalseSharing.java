package org.sample;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;

public class TestFalseSharing {

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public long unpadding(BenchMarkState state){
	 PaddingObject po = new PaddingObject();
	 Thread t1 = new Thread(new UseUnpad1(po));
	 Thread t2 = new Thread(new UseUnpad2(po));
	 t1.start();
	 t2.start();
	 try {
		t1.join();
		t2.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
  	return po.t1;
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public long padding(BenchMarkState state){
	 PaddingObject po1 = new PaddingObject();
	 PaddingObject po2 = new PaddingObject();
	Thread t1 = new Thread(new UseUnpad1(po1));
	 Thread t2 = new Thread(new UseUnpad1(po2));
	 t1.start();
	 t2.start();
	 try {
		t1.join();
		t2.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
  	return po1.t1 + po2.t1;
  }
//  Benchmark                   Mode  Cnt   Score   Error  Units
//  TestFalseSharing.padding    avgt  200   6.733 ± 0.017  ms/op
//  TestFalseSharing.unpadding  avgt  200  35.475 ± 0.750  ms/op
  @State(Scope.Benchmark)
  public static class BenchMarkState {}
	private static class UseUnpad2 implements Runnable{
		private PaddingObject value;
		public UseUnpad2(PaddingObject input) {
			this.value = input;
		}
		@Override
		public void run() {
			for(int i = 0; i < 1000000; i++)
				this.value.t2++;

		}

	}
	private static class UseUnpad1 implements Runnable{
		private PaddingObject value;
		public UseUnpad1(PaddingObject input) {
			this.value = input;
		}
		@Override
		public void run() {
			for(int i = 0; i < 1000000; i++)
				this.value.t1++;

		}

	}
	private static class PaddingObject {
		public volatile long t0,t1, t2, t3, t4, t5, t6, t7;
		public String toString() {
			return t0 + t1 + t2 + t3 + t4 + t5 + t6 + t7 + "";
		}
	}
}
