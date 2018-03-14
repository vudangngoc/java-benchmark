package org.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

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

import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class TestPubSub {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void java9(BenchMarkState state) {
		for(int i = 0; i <state.size;i++) {
			state.publisher.submit(state.testData.get(i));
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void lmaxDisruptor(BenchMarkState state) {		
		for(int i = 0; i <state.size;i++) {
			state.producer.onData(state.testData.get(i));
		}
	}



	@State(Scope.Benchmark)
	public static class BenchMarkState {
		public SubmissionPublisher<Integer> publisher;
		ExecutorService executor = Executors.newCachedThreadPool();
		int bufferSize = 1024;
		Disruptor<IntegerEvent> disruptor = new Disruptor(new IntegerEventFactory(), bufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
		public RingBuffer<IntegerEvent> ringBuffer;
		IntegerEventProducer producer;
		@Setup(Level.Trial)
		public void doSetup() {
			IntegerEventHandler handler = new IntegerEventHandler();
			disruptor.handleEventsWith(handler).then(new ClearingEventHandler());
			disruptor.start();
			ringBuffer = disruptor.getRingBuffer();
			producer = new IntegerEventProducer(ringBuffer);
			
			publisher = new SubmissionPublisher<>();
			EndSubscriber<Integer> subscriber = new EndSubscriber<>();
			publisher.subscribe(subscriber);
			
			for(int i = 0; i < size; i++)
				testData.add(Integer.valueOf(i));
		}
		@TearDown(Level.Trial)
		public void doTearDown() {
			try {
				disruptor.shutdown(0, TimeUnit.NANOSECONDS);
				// if shutdown is successful:
				// 1. exception is not thrown (obviously)
				// Disruptor.halt() is called automatically (less obvious)
			}
			catch (TimeoutException e) {
				disruptor.halt();
			}
			executor.shutdown();
			try {
				executor.awaitTermination(0, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			publisher.close();
		}

		public int size = 50000000;
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
		public void onEvent(IntegerEvent event, long sequence, boolean endOfBatch)
		{
			// do nothing
		}
	}
	public static class IntegerEventProducer
	{
		private final RingBuffer<IntegerEvent> ringBuffer;
		
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
	public static class EndSubscriber<T> implements Subscriber<T> {
		private Subscription subscription;
		public boolean isDone = false;;
		@Override
		public void onSubscribe(Subscription subscription) {
			this.subscription = subscription;
			subscription.request(1);
		}

		@Override
		public void onComplete() {
			isDone = true;
		}

		@Override
		public void onError(Throwable arg0) {
			arg0.printStackTrace();
		}

		@Override
		public void onNext(T arg0) {
			subscription.request(1);
			// do nothing
		}
	}
}
