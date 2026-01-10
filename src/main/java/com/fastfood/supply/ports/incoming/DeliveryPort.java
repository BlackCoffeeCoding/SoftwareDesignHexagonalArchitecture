package com.fastfood.supply.ports.incoming;

public interface DeliveryPort {
    void markDelivered(String orderId);
}