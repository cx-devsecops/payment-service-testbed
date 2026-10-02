CREATE TABLE payments (
    id VARCHAR(36) PRIMARY KEY,
    merchant_id VARCHAR(100) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    currency CHAR(3) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE merchant_notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    merchant_id VARCHAR(100) NOT NULL,
    note VARCHAR(1000) NOT NULL
);
