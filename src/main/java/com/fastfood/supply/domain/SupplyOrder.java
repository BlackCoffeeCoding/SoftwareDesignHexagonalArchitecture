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

    public void markSent() {
        if (status != OrderStatus.CREATED)
            throw new IllegalStateException("Поставка не может быть отправлена");
        status = OrderStatus.SENT;
    }

    public void markConfirmed() {
        if (status != OrderStatus.SENT)
            throw new IllegalStateException("Поставка не может быть подтверждена");
        status = OrderStatus.CONFIRMED;
    }

    public void markDelivered() {
        if (status != OrderStatus.CONFIRMED)
            throw new IllegalStateException("Поставка не может быть получена");
        status = OrderStatus.DELIVERED;
    }

    public void checkQuality(boolean passed) {
        if (status != OrderStatus.DELIVERED)
            throw new IllegalStateException("Нельзя проверить качество до приемки");
        status = passed ? OrderStatus.QUALITY_PASSED : OrderStatus.QUALITY_FAILED;
    }

    public void accept() {
        if (status != OrderStatus.QUALITY_PASSED)
            throw new IllegalStateException("Нельзя принять без успешной проверки");
        status = OrderStatus.ACCEPTED;
    }

    public void reject() {
        if (status != OrderStatus.QUALITY_FAILED)
            throw new IllegalStateException("Нельзя отклонить без проваленной проверки");
        status = OrderStatus.REJECTED;
    }

    public void returnDefectiveDelivery() {
        if (status != OrderStatus.REJECTED) {
            throw new IllegalStateException(
                    "Возврат брака возможен только для отклонённой поставки"
            );
        }
        status = OrderStatus.SENT;
    }
}