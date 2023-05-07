package org.sample.kotlin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompleteFutureChain {
    public int test(){
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> Jobs.job1())
                .thenApplyAsync(i -> i + Jobs.job2())
                .thenApplyAsync(i -> i + Jobs.job3());
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
