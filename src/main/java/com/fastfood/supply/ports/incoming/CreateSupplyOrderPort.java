package com.fastfood.supply.ports.incoming;

import java.time.LocalDate;
import java.util.Map;

public interface CreateSupplyOrderPort {
    String createOrder(String supplierId, LocalDate dueDate, Map<String,Integer> items);
    void sendOrder(String orderId);
}