package com.fastfood.supply.ports.incoming;

public interface AcceptancePort {
    void acceptDelivery(String orderId);
    void rejectDelivery(String orderId);
}