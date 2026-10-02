package com.example.payment.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository {
    private final JdbcTemplate jdbc;

    public PaymentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(Payment payment) {
        jdbc.update("INSERT INTO payments (id, merchant_id, amount, currency, status, created_at) VALUES (?, ?, ?, ?, ?, ?)",
                payment.id().toString(), payment.merchantId(), payment.amount(), payment.currency(),
                payment.status(), java.sql.Timestamp.from(payment.createdAt()));
    }

    public Optional<Payment> findById(UUID id) {
        return jdbc.query("SELECT id, merchant_id, amount, currency, status, created_at FROM payments WHERE id = ?",
                this::map, id.toString()).stream().findFirst();
    }

    private Payment map(ResultSet rs, int rowNum) throws SQLException {
        return new Payment(UUID.fromString(rs.getString("id")), rs.getString("merchant_id"),
                rs.getBigDecimal("amount"), rs.getString("currency"), rs.getString("status"),
                rs.getTimestamp("created_at").toInstant());
    }
}
