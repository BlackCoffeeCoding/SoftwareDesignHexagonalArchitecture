package com.fastfood.supply.ports.incoming;

public interface ConfirmSupplyOrderPort {
    void receiveSupplierConfirmation(String orderId);
}
