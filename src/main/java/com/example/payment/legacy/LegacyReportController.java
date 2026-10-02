package com.example.payment.legacy;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/legacy/reports")
public class LegacyReportController {
    private final LegacyReportRepository repository;

    public LegacyReportController(LegacyReportRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String, Object>> byMerchant(@RequestParam String merchant) {
        return repository.byMerchant(merchant);
    }
}
