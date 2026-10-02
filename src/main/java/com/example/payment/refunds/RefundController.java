package com.example.payment.refunds;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/refunds")
public class RefundController {

    private final RefundRepository repository;

    public RefundController(RefundRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Map<String, Object>> search(@RequestParam String merchant,
                                            @RequestParam(defaultValue = "0") String minAmount) {
        return repository.byMerchantAndAmount(merchant, minAmount);
    }
}
