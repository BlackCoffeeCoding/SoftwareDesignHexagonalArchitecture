package com.fastfood.supply.adapters.outgoing;

import com.fastfood.supply.ports.outgoing.SupplierNotifier;

public class MockSupplierNotifier implements SupplierNotifier {
    @Override
    public void notifySupplier(String supplierId, String message) {
        System.out.println("[MockNotifier] поставщику " + supplierId + ": " + message);
    }
}