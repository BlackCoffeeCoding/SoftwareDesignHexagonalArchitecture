package com.fastfood.supply.ports.incoming;

public interface QualityCheckPort {
    void checkQuality(String orderId, boolean passed);
}