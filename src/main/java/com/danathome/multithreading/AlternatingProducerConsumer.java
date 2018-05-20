package com.danathome.multithreading;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple take on the classic producer-consumer problem where the producer keeps adding
 * elements to a container until the container is full. Once full, the producer releases
 * the object monitor.
 * When the consumer takes over, it keeps removing elements from the container until it
 * is empty. The consumer then releases its hold on the monitor and the producer takes
 * over once more.
 * The producer and consumer thus alternate roles until the main thread is stopped.
 */
public class AlternatingProducerConsumer {
    /**
     * A lock object for the producer and consumer methods.
     */
    private Object lock;

    /**
     * A common container for the producer and consumer threads to operate on.
     */
    private List<Integer> container;

    /**
     * The container's maximum allowed size.
     */
    private final int MAX_SIZE;

    /**
     * The value to add to the container.
     */
    private volatile int value;

    public AlternatingProducerConsumer(int maxSize) {
        this.lock = new Object();
        this.container = new LinkedList<>();
        this.MAX_SIZE = maxSize;
        this.value = 0;
    }

    public void produce() throws InterruptedException {
        synchronized (lock) {

            while (true) {
                // Keep adding elements to the list until the max size is reached.
                while (container.size() < MAX_SIZE) {
                    // Note the added values also match the index of their position in the container list.
                    container.add(value);
                    System.out.println("Added " + value);
                    value++;

                    // Note that notify does not break out of the while loop.
                    // The producer continues until the while condition is invalid.
                    lock.notify();

                    Thread.sleep(500);
                }

                System.out.println("Cannot add more elements, container is full. Waiting.");
                lock.wait();
            }
        }
    }

    public void consume() throws InterruptedException{
        synchronized (lock) {
            while (true) {
                // Keep removing elements from the container until the min size is reached.
                while (container.size() > 0) {
                    Integer removedEl = container.remove(--value);
                    System.out.println("Removed " + removedEl);

                    lock.notify();

                    Thread.sleep(500);
                }

                System.out.println("Cannot remove element, container is empty. Waiting.");
                lock.wait();
            }
        }
    }

    public static void main(String[] args) {
        AlternatingProducerConsumer pc = new AlternatingProducerConsumer(10);
        Thread producer1 = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumer1 = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer1.start();
        consumer1.start();
    }
}
