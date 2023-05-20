package org.sample.kotlin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    public static ExecutorService executor = Executors.newFixedThreadPool(16);
    public int test(){
        CompletableFuture<Integer> future1 = new CompletableFuture<>();
        executor.submit(() -> future1.complete(Jobs.job1()));
        CompletableFuture<Integer> future2 = new CompletableFuture<>();
        executor.submit(() -> future2.complete(Jobs.job2()));
        CompletableFuture<Integer> future3 = new CompletableFuture<>();
        executor.submit(() -> future3.complete(Jobs.job3()));
        try {
            return future3.get() + future2.get() + future1.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
