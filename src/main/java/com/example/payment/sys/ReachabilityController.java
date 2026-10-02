package com.example.payment.sys;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/diagnostics")
public class ReachabilityController {

    private static final Set<String> ALLOWED_TARGETS = Set.of(
            "gateway.internal.example.com",
            "ledger.internal.example.com",
            "notifications.internal.example.com");

    @GetMapping("/reachable")
    public ResponseEntity<String> reachable(@RequestParam String target) throws Exception {
        if (!ALLOWED_TARGETS.contains(target)) {
            return ResponseEntity.badRequest().body("target not in allowlist");
        }

        ProcessBuilder builder = new ProcessBuilder(List.of("/sbin/ping", "-c", "1", target));
        builder.redirectErrorStream(true);
        Process process = builder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append('\n');
            }
        }
        return ResponseEntity.ok(output.toString());
    }
}
