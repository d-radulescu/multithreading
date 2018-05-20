package com.danathome.multithreading.systemstarter;

import lombok.Getter;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * A use case for cyclic barrier simulating a system starter class charged with bringing up several systems before
 * continuing on with other work. Each thread can be seen as a subsystem that has to do some work before the main system
 * can proceed.
 */
@Getter
public class SystemStarter implements Runnable {
    private Random random = new Random();
    private int id;
    private CyclicBarrier cyclicBarrier;

    public SystemStarter(int id, CyclicBarrier cyclicBarrier) {
        this.id = id;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println("Subsystem-" + id + " started.");

        try {
            Thread.sleep(random.nextInt(5000));
            System.out.println("Subsystem-" + id + " ready.");

            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        int numberOfSystems = 5;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfSystems,
                () -> System.out.println("All systems started. Continuing with other work."));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfSystems);
        IntStream.range(0, numberOfSystems)
                .forEach(i -> executorService.execute(new SystemStarter(i, cyclicBarrier)));

        executorService.shutdown();
    }
}
