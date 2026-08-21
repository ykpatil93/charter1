package com.charter.rewards.repository;

import java.time.LocalDate;
import java.util.List;

import com.charter.rewards.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Transaction> findTransactions(LocalDate from, LocalDate to) {
        String sql = """
                SELECT t.id, t.customer_id, c.name, t.amount, t.transaction_date
                FROM transactions t
                JOIN customers c ON c.id = t.customer_id
                WHERE t.transaction_date BETWEEN ? AND ?
                ORDER BY t.customer_id, t.transaction_date, t.id
                """;

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNumber) -> new Transaction(
                        resultSet.getLong("id"),
                        resultSet.getLong("customer_id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getDate("transaction_date").toLocalDate()),
                from,
                to);
    }
}
