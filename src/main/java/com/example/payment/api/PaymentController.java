package com.example.payment.api;

import com.example.payment.domain.Payment;
import com.example.payment.domain.PaymentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentRepository repository;

    public PaymentController(PaymentRepository repository) {
        this.repository = repository;
    }

    public record NewPayment(@NotBlank String merchantId,
                             @NotNull @DecimalMin("0.01") BigDecimal amount,
                             @NotNull @Pattern(regexp = "[A-Z]{3}") String currency) { }

    @PostMapping
    public ResponseEntity<Payment> create(@Valid @RequestBody NewPayment request) {
        Payment payment = new Payment(UUID.randomUUID(), request.merchantId(), request.amount(),
                request.currency(), "AUTHORIZED", Instant.now());
        repository.save(payment);
        return ResponseEntity.created(URI.create("/api/payments/" + payment.id())).body(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> get(@PathVariable UUID id) {
        return repository.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
