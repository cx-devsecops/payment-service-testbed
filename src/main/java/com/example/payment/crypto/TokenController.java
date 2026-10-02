package com.example.payment.crypto;

import java.util.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/tokens")
public class TokenController {

    private final HashUtil hashUtil;

    public TokenController(HashUtil hashUtil) {
        this.hashUtil = hashUtil;
    }

    /**
     * Genera la huella del numero de tarjeta para deduplicar transacciones.
     *
     * <p>El valor recibido se normaliza y se sala antes de aplicar el digest, por lo
     * que la huella resultante no permite recuperar el PAN original.
     */
    @PostMapping("/fingerprint")
    public ResponseEntity<String> fingerprint(@RequestParam String pan) throws Exception {
        return ResponseEntity.ok(hashUtil.digest(pan));
    }

    @PostMapping("/protect")
    public ResponseEntity<String> protect(@RequestParam String value) throws Exception {
        byte[] encrypted = hashUtil.encrypt(value);
        return ResponseEntity.ok(Base64.getEncoder().encodeToString(encrypted));
    }
}
