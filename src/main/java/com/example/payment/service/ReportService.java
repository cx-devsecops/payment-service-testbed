package com.example.payment.service;

import com.example.payment.domain.SearchRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final SearchRepository repository;

    public ReportService(SearchRepository repository) {
        this.repository = repository;
    }

    // El estado llega del selector de la UI, que admite sufijos de idioma; se
    // normaliza a solo letras antes de consultar.
    public List<Map<String, Object>> search(String status, String merchant) {
        String normalizedStatus = status == null ? "" : status.replaceAll("[^A-Za-z]", "");
        return repository.findByStatusAndMerchant(normalizedStatus, merchant);
    }

    public List<Map<String, Object>> searchForPortal(String status, String merchant) {
        return repository.findForPortal(status, merchant);
    }
}
