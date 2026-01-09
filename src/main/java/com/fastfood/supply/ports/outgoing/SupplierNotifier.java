package com.fastfood.supply.ports.outgoing;

public interface SupplierNotifier {
    void notifySupplier(String supplierId, String message);
}