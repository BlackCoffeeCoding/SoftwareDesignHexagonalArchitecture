package com.fastfood.supply.application;

import com.fastfood.supply.domain.SupplyOrder;
import com.fastfood.supply.ports.incoming.CreateSupplyOrderPort;
import com.fastfood.supply.ports.outgoing.SupplyOrderRepository;
import com.fastfood.supply.ports.outgoing.SupplierNotifier;

import java.time.LocalDate;
import java.util.Map;

public class SupplyOrderService implements CreateSupplyOrderPort {
    private final SupplyOrderRepository repository;
    private final SupplierNotifier notifier;

    public SupplyOrderService(SupplyOrderRepository repository, SupplierNotifier notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    @Override
    public String createOrder(String supplierId, LocalDate dueDate, Map<String, Integer> items) {
        SupplyOrder order = new SupplyOrder(supplierId, dueDate);
        items.forEach(order::addItem);
        repository.save(order);
        return order.getId();
    }

    @Override
    public void sendOrder(String orderId) {
        SupplyOrder o = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("заказ не найден"));
        o.markSent();
        repository.save(o);
        notifier.notifySupplier(o.getSupplierId(), "заказ " + o.getId() + " был отправлен.");
    }

    @Override
    public void confirmOrder(String orderId) {
        SupplyOrder o = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("заказ не найден"));
        o.markConfirmed();
        repository.save(o);
        notifier.notifySupplier(o.getSupplierId(), "заказ " + o.getId() + " подтвержден.");
    }
}