package com.danathome.multithreading.simplestore;

import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

/**
 * A producer-consumer use case simulating transactions for an online store. Clients produce transactions for the store
 * which get stored on a BlockingQueue until a WarehouseWorker can fulfill (or consume) them. Clients produce
 * PurchaseOrders faster than WarehouseWorkers can consume them and the blocking queue is of limited capacity. This
 * setup highlights how producers are 'blocked' until the consumers can complete their work when store is at capacity.
 */
@Getter
public class OnlineStore {
    /**
     * The transaction ledger store PurchaseOrders made by clients on a BlockingQueue.
     */
    private final BlockingQueue<PurchaseOrder> transactionLedger;

    public OnlineStore(int storeCapacity) {
        this.transactionLedger = new LinkedBlockingQueue<>(storeCapacity);
    }

    public static void main(String[] args) {
        OnlineStore onlineStore = new OnlineStore(10);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        IntStream.range(0, 8).forEach(i ->
                executorService.submit(new Client("Client" + i, onlineStore))
        );

        IntStream.range(0, 2).forEach(i ->
                executorService.submit(new WarehouseWorker("Worker" + i, onlineStore))
        );

        executorService.shutdown();
    }
}
