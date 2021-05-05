package com.example.core.concurrency.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Main main = new Main();
        try {
            //thenCompose example
            String result = main.calculateAsync()
                    .thenCompose(main::calculateAsyncWithOutExecutor)
                    .get();

            System.out.println(result);

            //thenCombine example
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                    .thenCombine(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> s1 + s2);
            System.out.println(completableFuture.get());

            //thenAcceptBoth example
            /*A simpler case is when we want to do something with two Futures‘ results, but don't need to pass any resulting value down a
            Future chain, then thenAcceptBoth method can be used
            */
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> "Hello")
                        .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World!"),
                                (s1, s2) -> System.out.println(s1 + s2));
            future.get();

            main.runMultipleFuturesInParallel();

            main.errorHandling(null);

            main.asyncMethods();
        } finally {
            main.executorService.shutdownNow();
        }
    }

    private void runMultipleFuturesInParallel() {
        CompletableFuture<String> future1
                = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() -> "Beautiful");
        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() -> "World");

        /*CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);
        combinedFuture.get();
        System.out.println("Main.runMultipleFuturesInParallel " + future1.isDone());
        System.out.println("Main.runMultipleFuturesInParallel " + future1.get());
        System.out.println("Main.runMultipleFuturesInParallel " + future2.isDone());
        System.out.println("Main.runMultipleFuturesInParallel " + future2.get());
        System.out.println("Main.runMultipleFuturesInParallel " + future3.isDone());
        System.out.println("Main.runMultipleFuturesInParallel " + future3.get());*/

        //The CompletableFuture.join() method is similar to the get method, but it throws an unchecked exception in case the Future does not complete normally.
        // This makes it possible to use it as a method reference in the Stream.map() method.
        String combined = Stream.of(future1, future2, future3)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));
        System.out.println("Main.runMultipleFuturesInParallel " + combined);
    }

    private void errorHandling(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture =  CompletableFuture.supplyAsync(() -> {
            if (name == null) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        }).handle((s, t) -> s != null ? s : /*"Hello, Stranger!"*/ t.getMessage());

        System.out.println("Main.errorHandling " + completableFuture.get());

        /*
         * instead if we want to throw exception instead of handling, we can use completeExceptionally.
         */

        try {
            CompletableFuture<String> completableFutureWithException = new CompletableFuture<>();

            completableFutureWithException.completeExceptionally(new RuntimeException("Calculation failed!"));
            completableFutureWithException.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Main.errorHandling " + e.getMessage());
        }
    }

    private void asyncMethods() throws ExecutionException, InterruptedException {
        /*
         * The methods without the Async postfix run the next execution stage using a calling thread. In contrast, the Async method without the
         * Executor argument runs a step using the common fork/join pool implementation of Executor that is accessed with the ForkJoinPool.commonPool()
         * method. Finally, the Async method with an Executor argument runs a step using the passed Executor.
         * under the hood the application of a function is wrapped into a ForkJoinTask instance. This allows us to parallelize our computation even more and use system resources more efficiently:
         */
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future = completableFuture .thenApplyAsync(s -> s + " World");

        System.out.println("Main.asyncMethods " + future.get());
    }

    private CompletableFuture<String> calculateAsync() {
        /*
         * If we already know the result of a computation, we can use the static completedFuture method with an argument
         * that represents a result of this computation.
         * Consequently, the get method of the Future will never block,
         */
        //Future<String> completableFuture = CompletableFuture.completedFuture("Hello");

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        executorService.submit(() -> {
            //LockSupport.parkNanos(500_000_000);
            Thread.sleep(500);

            completableFuture.complete("Hello");

            return null;
        });

        /*
         * The most generic way to process the result of a computation is to feed it to a function. The thenApply method does exactly that;
         * it accepts a Function instance, uses it to process the result, and returns a Future that holds a value returned by a function
         */
        //CompletableFuture<String> thenApply = completableFuture.thenApply(s -> s + " world");

        /*
         * If we don't need to return a value down the Future chain, we can use an instance of the Consumer functional interface.
         * Its single method takes a parameter and returns void.
         */
        //CompletableFuture<Void> thenAccept = completableFuture.thenAccept(s -> System.out.println("Result from completable future " + s));

        /*
         * if we neither need the value of the computation, nor want to return some value at the end of the chain, then we can pass a
         * Runnable lambda to the thenRun method. In the following example, we simply print a line in the console after calling the future.get()
         */
        //CompletableFuture<Void> thenRun = completableFuture.thenRun(() -> System.out.println("Computation finished"));

        return completableFuture;
    }

    private CompletableFuture<String> calculateAsyncWithOutExecutor(String s) {
        return CompletableFuture.supplyAsync(() -> "I got -> " + s);
        //CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> System.out.println("Hello"));
    }
}
