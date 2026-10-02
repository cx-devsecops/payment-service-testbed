package com.example.payment.legacy;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LegacyReportRepository {
    private final JdbcTemplate jdbc;

    public LegacyReportRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String, Object>> byMerchant(String merchant) {
        String sql = "SELECT id, merchant_id, amount, currency, status FROM payments WHERE merchant_id = '"
                + merchant + "'";
        return jdbc.queryForList(sql);
    }
}
