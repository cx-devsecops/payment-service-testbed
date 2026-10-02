package com.example.payment.merchants;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MerchantNoteRepository {

    private final JdbcTemplate jdbc;

    public MerchantNoteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void store(String merchant, String encodedNote) {
        jdbc.update("INSERT INTO merchant_notes (merchant_id, note) VALUES (?, ?)", merchant, encodedNote);
    }

    public List<Map<String, Object>> findMatching(String fragment) {
        String sql = "SELECT merchant_id, note FROM merchant_notes WHERE note LIKE '%" + fragment + "%'";
        return jdbc.queryForList(sql);
    }
}
