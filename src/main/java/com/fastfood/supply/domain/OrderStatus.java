package com.fastfood.supply.domain;

public enum OrderStatus {
    CREATED,
    SENT,
    CONFIRMED,
    DELIVERED,
    QUALITY_PASSED,
    QUALITY_FAILED,
    ACCEPTED,
    REJECTED
}