package com.charter.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.charter.rewards.exception.InvalidDateRangeException;
import com.charter.rewards.model.Transaction;
import com.charter.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class RewardServiceTest {

    private final TransactionRepository repository = Mockito.mock(TransactionRepository.class);
    private final RewardService service = new RewardService(repository);

    @Test
    void shouldAggregateMultipleCustomersAndMonths() {
        LocalDate from = LocalDate.of(2026, 5, 1);
        LocalDate to = LocalDate.of(2026, 7, 31);

        when(repository.findTransactions(from, to)).thenReturn(List.of(
                new Transaction(1, 10, "Alice", new BigDecimal("120"), LocalDate.of(2026, 5, 5)),
                new Transaction(2, 10, "Alice", new BigDecimal("75"), LocalDate.of(2026, 5, 6)),
                new Transaction(3, 10, "Alice", new BigDecimal("210"), LocalDate.of(2026, 6, 5)),
                new Transaction(4, 20, "Bob", new BigDecimal("100"), LocalDate.of(2026, 5, 7)),
                new Transaction(5, 20, "Bob", new BigDecimal("60"), LocalDate.of(2026, 7, 8))));

        var result = service.calculateRewards(from, to);

        assertEquals(2, result.size());
        assertEquals(385, result.get(0).totalPoints());
        assertEquals(60, result.get(1).totalPoints());
        assertEquals(2, result.get(0).monthlyRewards().size());
    }

    @Test
    void shouldRejectReversedDateRange() {
        LocalDate from = LocalDate.of(2026, 7, 31);
        LocalDate to = LocalDate.of(2026, 5, 1);

        assertThrows(
                InvalidDateRangeException.class,
                () -> service.calculateRewards(from, to));
    }
}
