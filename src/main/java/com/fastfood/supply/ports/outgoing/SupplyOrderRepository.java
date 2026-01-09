package com.fastfood.supply.ports.outgoing;

import com.fastfood.supply.domain.SupplyOrder;
import java.util.Optional;

public interface SupplyOrderRepository {
    void save(SupplyOrder order);
    Optional<SupplyOrder> findById(String id);
}