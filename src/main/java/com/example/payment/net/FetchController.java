package com.example.payment.net;

import com.example.payment.gateway.GatewayClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/callback")
public class FetchController {

    private final GatewayClient gatewayClient;

    public FetchController(GatewayClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }

    // Verifica que el endpoint de callback del comercio responda antes de registrarlo.
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", gatewayClient.authorizationHeader());
        conn.setRequestProperty("X-Legacy-Authorization", gatewayClient.legacyAuthorizationHeader());
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        }
        return ResponseEntity.ok(sb.toString());
    }
}
