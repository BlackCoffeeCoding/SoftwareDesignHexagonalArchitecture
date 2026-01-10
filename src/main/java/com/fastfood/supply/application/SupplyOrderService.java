package com.fastfood.supply.application;

import com.fastfood.supply.domain.OrderStatus;
import com.fastfood.supply.domain.SupplyOrder;
import com.fastfood.supply.ports.incoming.*;
import com.fastfood.supply.ports.outgoing.SupplyOrderRepository;
import com.fastfood.supply.ports.outgoing.SupplierNotifier;

import java.time.LocalDate;
import java.util.Map;

public class SupplyOrderService implements CreateSupplyOrderPort, ConfirmSupplyOrderPort, TrackSupplyOrderPort, DeliveryPort, QualityCheckPort, AcceptancePort, DefectiveReturnPort {
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
    public void receiveSupplierConfirmation(String orderId) {
        SupplyOrder o = repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("заказ не найден"));
        o.markConfirmed();
        repository.save(o);
    }

    @Override
    public OrderStatus getStatus(String orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("заказ не найден"))
                .getStatus();
    }

    @Override
    public void markDelivered(String orderId) {
        SupplyOrder order = repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("заказ не найден"));
        order.markDelivered();
        repository.save(order);
    }

    @Override
    public void checkQuality(String orderId, boolean passed) {
        SupplyOrder o = repository.findById(orderId).orElseThrow();
        o.checkQuality(passed);
        repository.save(o);
    }

    @Override
    public void acceptDelivery(String orderId) {
        SupplyOrder o = repository.findById(orderId).orElseThrow();
        o.accept();
        repository.save(o);
    }

    @Override
    public void rejectDelivery(String orderId) {
        SupplyOrder o = repository.findById(orderId).orElseThrow();
        o.reject();
        repository.save(o);
        notifier.notifySupplier(o.getSupplierId(), "заказ " + o.getId() + " не прошел проверку качества и будет вам возвращен.");
    }

    @Override
    public void returnDefectiveDelivery(String orderId) {
        SupplyOrder o = repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("заказ не найден"));

        o.returnDefectiveDelivery();
        repository.save(o);
        notifier.notifySupplier(o.getSupplierId(), "заказ " + o.getId() + " отправлен обратно по причине брака.");
    }

}