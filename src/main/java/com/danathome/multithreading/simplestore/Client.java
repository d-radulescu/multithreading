package com.danathome.multithreading.simplestore;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@Getter
@AllArgsConstructor
public class Client implements Runnable {
    private final String id;
    private OnlineStore store;

    @Override
    public void run() {
        while(true) {
            double purchaseAmount = new Random().nextDouble() * 10;
            try {
                PurchaseOrder purchaseOrder = new PurchaseOrder(this.id, purchaseAmount);
                store.getTransactionLedger().put(purchaseOrder);
                System.out.println(this.id + " placed a purchase order.");
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
