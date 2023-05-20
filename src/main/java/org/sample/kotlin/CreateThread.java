package org.sample.kotlin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CreateThread {
    public int test(){
        CompletableFuture<Integer> future1 = new CompletableFuture<>();
        new Thread(() -> future1.complete(Jobs.job1())).start();
        CompletableFuture<Integer> future2 = new CompletableFuture<>();
        new Thread(() -> future2.complete(Jobs.job2())).start();
        CompletableFuture<Integer> future3 = new CompletableFuture<>();
        new Thread(() -> future3.complete(Jobs.job3())).start();
        try {
            return future3.get() + future2.get() + future1.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
