package com.example.payment.billing;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceRepository {

    private final JdbcTemplate jdbc;

    public InvoiceRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String, Object>> findByPeriod(String merchant, String fromDate, String currency) {
        StringBuilder sql = new StringBuilder(256);
        sql.append("SELECT id, merchant_id, amount, currency, status, created_at FROM payments");
        sql.append(" WHERE status = 'AUTHORIZED'");
        sql.append(" AND merchant_id = '").append(merchant).append('\'');

        if (fromDate != null && !fromDate.isBlank()) {
            sql.append(" AND created_at >= '").append(fromDate).append('\'');
        }
        if (currency != null && !currency.isBlank()) {
            sql.append(" AND currency = '").append(currency).append('\'');
        }

        sql.append(" ORDER BY created_at DESC");
        return jdbc.queryForList(sql.toString());
    }
}
