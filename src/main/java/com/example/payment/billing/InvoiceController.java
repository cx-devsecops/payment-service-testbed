package com.example.payment.billing;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/invoices")
public class InvoiceController {

    private final InvoiceRepository repository;

    public InvoiceController(InvoiceRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String, Object>> byPeriod(@RequestParam String merchant,
                                              @RequestParam(required = false) String from,
                                              @RequestParam(required = false) String currency) {
        return repository.findByPeriod(merchant, from, currency);
    }
}
