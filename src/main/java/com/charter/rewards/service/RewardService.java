package com.charter.rewards.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.charter.rewards.dto.CustomerRewardResponse;
import com.charter.rewards.dto.MonthlyReward;
import com.charter.rewards.model.Transaction;
import com.charter.rewards.repository.TransactionRepository;
import com.charter.rewards.exception.InvalidDateRangeException;
import org.springframework.stereotype.Service;

@Service
public class RewardService {

    private final TransactionRepository transactionRepository;
    private final RewardCalculator rewardCalculator;

   
    public RewardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.rewardCalculator = new RewardCalculator();
    }

   
    public List<CustomerRewardResponse> calculateRewards(LocalDate from, LocalDate to) {
        validateDateRange(from, to);

        List<Transaction> transactions = transactionRepository.findTransactions(from, to);

        Map<Long, CustomerAccumulator> customers = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            CustomerAccumulator accumulator = customers.computeIfAbsent(
                    transaction.customerId(),
                    id -> new CustomerAccumulator(
                            transaction.customerId(),
                            transaction.customerName()));

            long points = rewardCalculator.calculate(transaction.amount());
            accumulator.addPoints(YearMonth.from(transaction.transactionDate()), points);
        }

        return customers.values().stream()
                .map(CustomerAccumulator::toResponse)
                .toList();
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new InvalidDateRangeException("Both from and to dates are required.");
        }

        if (from.isAfter(to)) {
            throw new InvalidDateRangeException("The from date must not be after the to date.");
        }
    }

    private static final class CustomerAccumulator {

        private final long customerId;
        private final String customerName;
        private final Map<YearMonth, Long> monthlyPoints = new TreeMap<>();

        private CustomerAccumulator(long customerId, String customerName) {
            this.customerId = customerId;
            this.customerName = customerName;
        }

        private void addPoints(YearMonth month, long points) {
            monthlyPoints.merge(month, points, Long::sum);
        }

        private CustomerRewardResponse toResponse() {
            List<MonthlyReward> monthlyRewards = new ArrayList<>();
            long total = 0;

            for (Map.Entry<YearMonth, Long> entry : monthlyPoints.entrySet()) {
                monthlyRewards.add(new MonthlyReward(entry.getKey().toString(), entry.getValue()));
                total += entry.getValue();
            }

            return new CustomerRewardResponse(customerId, customerName, monthlyRewards, total);
        }
    }
}
