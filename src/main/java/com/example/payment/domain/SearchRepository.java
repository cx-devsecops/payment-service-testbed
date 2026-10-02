package com.example.payment.domain;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SearchRepository {

    private final JdbcTemplate jdbc;

    public SearchRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String, Object>> findByStatusAndMerchant(String status, String merchant) {
        String sql = "SELECT id, merchant_id, amount, currency, status FROM payments "
                + "WHERE status = '" + status + "' AND merchant_id = '" + merchant + "'";
        return jdbc.queryForList(sql);
    }

    public List<Map<String, Object>> findForPortal(String status, String merchant) {
        String sql = "SELECT id, merchant_id, amount, currency, status FROM payments "
                + "WHERE status = ? AND merchant_id = ?";
        return jdbc.queryForList(sql, status, merchant);
    }
}
