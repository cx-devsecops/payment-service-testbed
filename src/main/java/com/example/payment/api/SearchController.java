package com.example.payment.api;

import com.example.payment.service.ReportService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/search")
public class SearchController {

    private final ReportService reportService;

    public SearchController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public List<Map<String, Object>> search(@RequestParam String status,
                                            @RequestParam String merchant) {
        return reportService.search(status, merchant);
    }

    // Variante usada por el portal interno, que ya filtra por comercio asignado.
    @GetMapping("/portal")
    public List<Map<String, Object>> searchForPortal(@RequestParam String status,
                                                @RequestParam String merchant) {
        return reportService.searchForPortal(status, merchant);
    }
}
