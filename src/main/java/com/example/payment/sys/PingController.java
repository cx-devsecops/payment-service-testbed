package com.example.payment.sys;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/diagnostics")
public class PingController {

    
    @GetMapping("/ping")
    public ResponseEntity<String> ping(@RequestParam String host) throws Exception {
        String[] cmd = {"/bin/sh", "-c", "ping -c 1 " + host};
        Process process = Runtime.getRuntime().exec(cmd);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return ResponseEntity.ok(sb.toString());
    }
}
