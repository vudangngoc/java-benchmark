package org.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.TimeUnit;

public class TestLMAXDisruptor {

	
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public List<Integer> singleThread(BenchMarkState state) {		
		for(int i = 0; i <state.size;i++) {
			state.producer.onData(state.testData.get(i));
		}
		return state.handler.data;
	}
//	Benchmark                       Mode  Cnt  Score   Error  Units
//	TestLMAXDisruptor.singleThread  avgt  200  8.237 ± 0.159  ms/op
//	TestReactor.singleThread        avgt  200  7.402 ± 0.475  ms/op
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		static ExecutorService executor = Executors.newCachedThreadPool();
		int bufferSize = 4096;
		Disruptor<IntegerEvent> disruptor;
		public RingBuffer<IntegerEvent> ringBuffer;
		public IntegerEventHandler handler ;
		public IntegerEventProducer producer;
		@Setup(Level.Trial)
		public void doSetupForTest() {
			for(int i = 0; i < size; i++)
				testData.add(Integer.valueOf(i));
		}
		@Setup(Level.Invocation)
		public void doSetup() {
			handler = new IntegerEventHandler();
			disruptor = new Disruptor(new IntegerEventFactory(), bufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
			disruptor.handleEventsWith(handler).then(new ClearingEventHandler());
			disruptor.start();
			ringBuffer = disruptor.getRingBuffer();
			producer = new IntegerEventProducer(ringBuffer);
		}
		@TearDown(Level.Invocation)
		public void doTearDown() {
			handler.clear();
			try {
				disruptor.shutdown(0, TimeUnit.NANOSECONDS);
				// if shutdown is successful:
				// 1. exception is not thrown (obviously)
				// Disruptor.halt() is called automatically (less obvious)
				
			}
			catch (TimeoutException e) {
				disruptor.halt();
			}
		}
		@TearDown(Level.Trial)
		public void tearAllDown() {
			executor.shutdown();
			try {
				executor.awaitTermination(0, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public int size = 1000000;
		public List<Integer> testData = new ArrayList<>();
	}

	public static class IntegerEvent
	{
		private Integer value;

		public void set(Integer value)
		{
			this.value = value;
		}
		public String toString() {
			return value.toString();
		}
		public void clear() {
			value = null;

		}
	}
	public static class ClearingEventHandler implements EventHandler<IntegerEvent>
	{
		public void onEvent(IntegerEvent event, long sequence, boolean endOfBatch)
		{
			// Failing to call clear here will result in the 
			// object associated with the event to live until
			// it is overwritten once the ring buffer has wrapped
			// around to the beginning.
			event.clear(); 
		}
	}
	public static class IntegerEventFactory implements EventFactory<IntegerEvent>{

		@Override
		public IntegerEvent newInstance() {
			return new IntegerEvent();
		}
	}
	public static class IntegerEventHandler implements EventHandler<IntegerEvent>
	{
		public List<Integer> data = new ArrayList<>(1000000);
		public int count = 0;
		public void onEvent(IntegerEvent event, long sequence, boolean endOfBatch)
		{
			data.add(event.value);
			count++;
		}
		public void clear()
		{
			data.clear();
		}
	}
	public static class IntegerEventProducer
	{
		private final RingBuffer<IntegerEvent> ringBuffer;
		//		IntegerEvent event;
		public IntegerEventProducer(RingBuffer<IntegerEvent> ringBuffer)
		{
			this.ringBuffer = ringBuffer;
		}

		public void onData(Integer data)
		{
			long sequence = ringBuffer.next();  // Grab the next sequence
			try
			{
				IntegerEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
				// for the sequence
				event.set(data);  // Fill with data
			}
			finally
			{
				ringBuffer.publish(sequence);
			}
		}
	}
	
	
}
