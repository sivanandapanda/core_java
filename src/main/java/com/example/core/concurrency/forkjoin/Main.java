package com.example.core.concurrency.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        FactorialSquareCalculator calculator = new FactorialSquareCalculator(10);

        forkJoinPool.execute(calculator);

        forkJoinPool.shutdownNow();
    }

}

class FactorialSquareCalculator extends RecursiveTask<Integer> {

    private final Integer n;

    public FactorialSquareCalculator(Integer n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }

        FactorialSquareCalculator calculator = new FactorialSquareCalculator(n - 1);

        calculator.fork();

        return n * n + calculator.join();
    }
}