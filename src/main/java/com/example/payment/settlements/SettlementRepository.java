package com.example.payment.settlements;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SettlementRepository {

    private final JdbcTemplate jdbc;

    public SettlementRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String, Object>> summarize(SettlementBucket bucket) {
        String sql = "SELECT merchant_id, currency, COUNT(*) AS total, SUM(amount) AS volume "
                + "FROM payments WHERE status = '" + bucket.name() + "' "
                + "GROUP BY merchant_id, currency ORDER BY merchant_id";
        return jdbc.queryForList(sql);
    }
}
