DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE transactions (
    id BIGINT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    amount DECIMAL(12, 2) NOT NULL CHECK (amount >= 0),
    transaction_date DATE NOT NULL,
    CONSTRAINT fk_transactions_customer
        FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE INDEX idx_transactions_customer_date
    ON transactions(customer_id, transaction_date);
