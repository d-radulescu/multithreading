package com.danathome.multithreading.simplestore;

import lombok.Data;

@Data
public class WarehouseWorker implements Runnable {
    private final String id;
    private final OnlineStore store;

    @Override
    public void run() {
        while(true) {
            try {
                PurchaseOrder orderFulfilled = store.getTransactionLedger().take();
                System.out.println("Worker " + this.getId() + " fulfilled order for" + orderFulfilled.getClientId());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
