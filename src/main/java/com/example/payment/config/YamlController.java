package com.example.payment.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

@RestController
@RequestMapping("/api/payments/config")
public class YamlController {


    @PostMapping(consumes = "application/x-yaml")
    public ResponseEntity<String> load(@RequestBody String body) {
        Yaml yaml = new Yaml();
        Object parsed = yaml.load(body);
        return ResponseEntity.ok("loaded:" + (parsed == null ? "null" : parsed.getClass().getName()));
    }
}
