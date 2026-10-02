package com.example.payment.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Payment(UUID id, String merchantId, BigDecimal amount, String currency,
                      String status, Instant createdAt) { }
