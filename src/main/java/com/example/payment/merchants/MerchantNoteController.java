package com.example.payment.merchants;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Notas operativas por comercio. El texto se guarda codificado en URL para
 * conservar acentos y saltos de linea del canal de origen.
 */
@RestController
@RequestMapping("/api/payments/merchants/notes")
public class MerchantNoteController {

    private final MerchantNoteRepository repository;

    public MerchantNoteController(MerchantNoteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestParam String merchant, @RequestParam String note) {
        String encoded = URLEncoder.encode(note, StandardCharsets.UTF_8);
        repository.store(merchant, encoded);
        return ResponseEntity.ok("stored");
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String fragment) {
        String decoded = URLDecoder.decode(fragment, StandardCharsets.UTF_8);
        return repository.findMatching(decoded);
    }
}
