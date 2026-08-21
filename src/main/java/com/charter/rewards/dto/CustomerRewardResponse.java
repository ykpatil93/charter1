package com.charter.rewards.dto;

import java.util.List;


public record CustomerRewardResponse(
        long customerId,
        String customerName,
        List<MonthlyReward> monthlyRewards,
        long totalPoints) {
}
