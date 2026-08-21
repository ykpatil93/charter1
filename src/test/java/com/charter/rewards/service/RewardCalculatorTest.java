package com.charter.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;


class RewardCalculatorTest {

    private final RewardCalculator calculator = new RewardCalculator();

    @Test
    void shouldAwardZeroPointsForFiftyDollarsOrLess() {
        assertEquals(0, calculator.calculate(new BigDecimal("50.00")));
        assertEquals(0, calculator.calculate(new BigDecimal("49.99")));
    }

    @Test
    void shouldAwardOnePointPerDollarBetweenFiftyAndOneHundred() {
        assertEquals(25, calculator.calculate(new BigDecimal("75.00")));
        assertEquals(50, calculator.calculate(new BigDecimal("100.00")));
    }

    @Test
    void shouldAwardTwoPointsPerDollarAboveOneHundred() {
        assertEquals(90, calculator.calculate(new BigDecimal("120.00")));
        assertEquals(250, calculator.calculate(new BigDecimal("200.00")));
    }

    @Test
    void shouldIgnoreFractionalDollarForPointCalculation() {
        assertEquals(50, calculator.calculate(new BigDecimal("100.99")));
        assertEquals(90, calculator.calculate(new BigDecimal("120.99")));
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(new BigDecimal("-1.00")));
    }

    @Test
    void shouldRejectNullAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(null));
    }
}
