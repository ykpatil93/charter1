package com.charter.rewards.model;

import java.math.BigDecimal;
import java.time.LocalDate;


public record Transaction(
        long id,
        long customerId,
        String customerName,
        BigDecimal amount,
        LocalDate transactionDate) {
}
