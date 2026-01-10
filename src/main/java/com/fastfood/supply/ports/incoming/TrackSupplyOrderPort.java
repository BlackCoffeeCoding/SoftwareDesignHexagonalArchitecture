package com.fastfood.supply.ports.incoming;

import com.fastfood.supply.domain.OrderStatus;

public interface TrackSupplyOrderPort {
    OrderStatus getStatus(String orderId);
}