package com.example.payment.files;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/archive")
public class ArchiveController {

    private static final String ARCHIVE_DIR = System.getProperty("java.io.tmpdir") + "/app-archive/";

    @PostMapping
    public ResponseEntity<String> archive(@RequestBody String payload) throws Exception {
        String generatedName = UUID.randomUUID() + ".json";
        Path target = Paths.get(ARCHIVE_DIR + generatedName);
        Files.createDirectories(target.getParent());
        Files.write(target, payload.getBytes(StandardCharsets.UTF_8));
        return ResponseEntity.ok(generatedName);
    }
}
