package com.charter.rewards.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RewardCalculator {

    private static final BigDecimal FIFTY = BigDecimal.valueOf(50);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    
    public long calculate(BigDecimal amount) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Transaction amount must be zero or greater.");
        }

        BigDecimal wholeDollars = amount.setScale(0, RoundingMode.FLOOR);

        if (wholeDollars.compareTo(FIFTY) <= 0) {
            return 0;
        }

        if (wholeDollars.compareTo(ONE_HUNDRED) <= 0) {
            return wholeDollars.subtract(FIFTY).longValue();
        }

        long pointsForFirstBand = 50;
        long pointsForSecondBand = wholeDollars
                .subtract(ONE_HUNDRED)
                .longValue() * 2;

        return pointsForFirstBand + pointsForSecondBand;
    }
}
