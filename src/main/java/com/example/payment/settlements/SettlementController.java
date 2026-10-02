package com.example.payment.settlements;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/settlements")
public class SettlementController {

    private final SettlementRepository repository;

    public SettlementController(SettlementRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{bucket}")
    public ResponseEntity<List<Map<String, Object>>> summary(@PathVariable String bucket) {
        SettlementBucket parsed;
        try {
            parsed = SettlementBucket.valueOf(bucket.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repository.summarize(parsed));
    }
}
