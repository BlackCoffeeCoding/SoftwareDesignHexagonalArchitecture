package com.fastfood.supply.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SupplyOrder {
    private final String id;
    private final String supplierId;
    private final LocalDate dueDate;
    private final Map<String, Integer> items = new HashMap<>();
    private OrderStatus status;

    public SupplyOrder(String supplierId, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.supplierId = supplierId;
        this.dueDate = dueDate;
        this.status = OrderStatus.CREATED;
    }

    public String getId() { return id; }
    public String getSupplierId() { return supplierId; }
    public LocalDate getDueDate() { return dueDate; }
    public OrderStatus getStatus() { return status; }
    public Map<String,Integer> getItems() { return items; }

    public void addItem(String productId, int qty) {
        items.put(productId, items.getOrDefault(productId, 0) + qty);
    }

    public void markSent() { this.status = OrderStatus.SENT; }
    public void markConfirmed() { this.status = OrderStatus.CONFIRMED; }
    public void markDelivered() { this.status = OrderStatus.DELIVERED; }
    public void markRejected() { this.status = OrderStatus.REJECTED; }
}