package com.fastfood.supply.ports.incoming;

public interface DefectiveReturnPort {
    void returnDefectiveDelivery(String orderId);
}