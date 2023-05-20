package org.sample.kotlin;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

public class TestWithContextFutureChain {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Threads(5)
    public void testWithContext(BenchMarkState state, Blackhole blackhole) {
        for (int i = 0; i < 10; i++) {
            blackhole.consume(state.withContext.test());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Threads(5)
    public void testFutureChain(BenchMarkState state, Blackhole blackhole) {
        for (int i = 0; i < 10; i++) {
            blackhole.consume(state.completeFutureChain.test());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Threads(5)
    public void testCreateThread(BenchMarkState state, Blackhole blackhole) {
        for (int i = 0; i < 10; i++) {
            blackhole.consume(state.createThread.test());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Threads(5)
    public void testThreadPool(BenchMarkState state, Blackhole blackhole) {
        for (int i = 0; i < 10; i++) {
            blackhole.consume(state.threadPool.test());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Threads(5)
    public void testVirtualThread(BenchMarkState state, Blackhole blackhole) {
        for (int i = 0; i < 10; i++) {
            blackhole.consume(state.threadPoolVirtualThread.test());
        }
    }

//    Benchmark                                   Mode  Cnt     Score   Error  Units
//    TestWithContextFutureChain.testFutureChain  avgt   25  1013.514 ▒ 3.984  ms/op
//    TestWithContextFutureChain.testWithContext  avgt   25   784.205 ▒ 2.160  ms/op
//    TestWithContextFutureChain.testCreateThread avgt   25   624.909 ▒ 3.978  ms/op

//    Benchmark                                     Mode  Cnt    Score   Error  Units
//    TestWithContextFutureChain.testThreadPool     avgt   25  623.860 ± 0.474  ms/op
//    TestWithContextFutureChain.testVirtualThread  avgt   25  624.792 ± 0.901  ms/op

    @State(Scope.Benchmark)
    public static class BenchMarkState {
        public CompleteFutureChain completeFutureChain = new CompleteFutureChain();
        public WithContext withContext = new WithContext();
        public CreateThread createThread = new CreateThread();
        public ThreadPool threadPool = new ThreadPool();
        public ThreadPoolVirtualThread threadPoolVirtualThread = new ThreadPoolVirtualThread();

        @TearDown
        public void tearDown(){
            ThreadPoolVirtualThread.executor.shutdown();
            ThreadPool.executor.shutdown();
        }
    }


}
