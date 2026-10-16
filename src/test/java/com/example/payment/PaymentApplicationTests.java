package com.example.payment;

import static org.assertj.core.api.Assertions.assertThat;
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

    /**
     * Verifies that the YAML endpoint correctly parses safe YAML content.
     * Regression test for CVE-2022-1471: SnakeYaml upgraded to 2.0 where
     * the default constructor uses SafeConstructor, preventing arbitrary type
     * instantiation during deserialization.
     */
    @Test
    void yamlEndpointParsesSafeContent() throws Exception {
        mvc.perform(post("/api/payments/config")
                        .contentType("application/x-yaml")
                        .content("key: value\ncount: 42"))
                .andExpect(status().isOk());
    }

    /**
     * Verifies that the YAML endpoint rejects global type tags (the CVE-2022-1471 attack vector).
     * In SnakeYaml >= 2.0, the default Yaml constructor disables global tags, so payloads
     * containing arbitrary class instantiation (e.g. !!javax.script.ScriptEngineManager) must
     * be rejected rather than executed. The response must NOT be 200 OK.
     */
    @Test
    void yamlEndpointRejectsGlobalTypeTags() throws Exception {
        // This payload exploits CVE-2022-1471 in SnakeYaml < 2.0 by instantiating
        // an arbitrary Java class via a global tag. SnakeYaml 2.0+ must reject it.
        String maliciousPayload = "!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[]]]";
        int status = mvc.perform(post("/api/payments/config")
                        .contentType("application/x-yaml")
                        .content(maliciousPayload))
                .andReturn()
                .getResponse()
                .getStatus();
        // SnakeYaml 2.0 throws an exception for global tags, resulting in a non-2xx response.
        assertThat(status).isNotEqualTo(200);
    }
}
