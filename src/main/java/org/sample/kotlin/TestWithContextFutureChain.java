package org.sample.kotlin;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import org.sample.kotlin.WithContext;

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

//    Benchmark                                   Mode  Cnt     Score   Error  Units
//    TestWithContextFutureChain.testFutureChain  avgt   25  1013.514 ▒ 3.984  ms/op
//    TestWithContextFutureChain.testWithContext  avgt   25   784.205 ▒ 2.160  ms/op

    @State(Scope.Benchmark)
    public static class BenchMarkState {
        public CompleteFutureChain completeFutureChain = new CompleteFutureChain();
        public WithContext withContext = new WithContext();
    }
}
