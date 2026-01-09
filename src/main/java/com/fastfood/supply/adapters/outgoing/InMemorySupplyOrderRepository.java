package com.fastfood.supply.adapters.outgoing;

import com.fastfood.supply.domain.SupplyOrder;
import com.fastfood.supply.ports.outgoing.SupplyOrderRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySupplyOrderRepository implements SupplyOrderRepository {
    private final Map<String, SupplyOrder> store = new ConcurrentHashMap<>();

    @Override
    public void save(SupplyOrder order) {
        store.put(order.getId(), order);
    }

    @Override
    public Optional<SupplyOrder> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}