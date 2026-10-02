package com.example.payment.web;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/return")
public class RedirectController {

    @GetMapping
    public ResponseEntity<Void> back(@RequestParam String next) {
        return ResponseEntity.status(302).location(URI.create(next)).build();
    }
}
