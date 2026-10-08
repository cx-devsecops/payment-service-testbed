package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
    
    private static final String SAMPLE_JWT_2 = "eyJzdWciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
}

//SQLi
@PostMapping("/retry")
   public ResponseEntity<?> retryPayment(@RequestParam String query) {
       String dbQuery = "SELECT * FROM transactions WHERE id = " + query;  // SQL Injection
       // ... execute query
       return ResponseEntity.ok("Payment retried");
   }
