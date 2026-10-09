package com.example.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentApplicationTests {
    private static final String MOCK_STAGING_KEY = "test_abc123456789";

    // Token de ejemplo publico de jwt.io, firmado con la clave de muestra documentada
    // "your-256-bit-secret". Payload: {"sub":"1234567890","name":"John Doe"}.
    private static final String SAMPLE_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    // Alfabeto estandar de base64, usado para validar el codificador de recibos.
    private static final String BASE64_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    // Huella SHA-256 de la cadena "test", valor esperado del verificador de integridad.
    private static final String FIXTURE_CHECKSUM =
            "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";

    @Autowired MockMvc mvc;

    @Test
    void createsPaymentAndReadsLegacyReport() throws Exception {
        mvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"merchantId\":\"merchant-demo\",\"amount\":12.50,\"currency\":\"USD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("AUTHORIZED"));
        mvc.perform(get("/api/legacy/reports").param("merchant", "merchant-demo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].MERCHANT_ID").value("merchant-demo"));
    }

    @Test
    void storesAndSearchesMerchantNotes() throws Exception {
        mvc.perform(post("/api/payments/merchants/notes")
                        .param("merchant", "merchant-demo")
                        .param("note", "revision manual pendiente"))
                .andExpect(status().isOk());
        mvc.perform(get("/api/payments/merchants/notes/search").param("fragment", "revision"))
                .andExpect(status().isOk());
    }

    @Test
    void summarizesSettlementsAndRejectsUnknownBucket() throws Exception {
        mvc.perform(get("/api/payments/settlements/authorized"))
                .andExpect(status().isOk());
        mvc.perform(get("/api/payments/settlements/not-a-bucket"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsTargetsOutsideAllowlist() throws Exception {
        mvc.perform(get("/api/payments/diagnostics/reachable").param("target", "attacker.example.com"))
                .andExpect(status().isBadRequest());
    }
       // src/main/java/com/payment/controller/PaymentController.java
   @PostMapping("/retry")
   public ResponseEntity<?> retryPayment(@RequestParam String query) {
       String dbQuery = "SELECT * FROM transactions WHERE id = " + query;  // SQL Injection
       // ... execute query
       return ResponseEntity.ok("Payment retried");
   }
}
