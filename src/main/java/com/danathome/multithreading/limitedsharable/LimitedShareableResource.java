package com.danathome.multithreading.limitedsharable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public enum LimitedShareableResource {
    INSTANCE;

    private Semaphore semaphore = new Semaphore(4, true);

    public void useResource() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " acquired lock.");
            System.out.println(semaphore.availablePermits() + " permits left.");

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " released lock.");
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        IntStream.range(0, 8).forEach(i -> executorService.execute(() -> INSTANCE.useResource()));

        executorService.shutdown();
    }
}
