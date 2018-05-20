package com.danathome.multithreading.simplestore;

import lombok.Data;

@Data
public class PurchaseOrder {
    private final String clientId;
    private final double amount;
}
