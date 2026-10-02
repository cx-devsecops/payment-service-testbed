package com.example.payment.files;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/receipts")
public class FileController {

    private static final String BASE_DIR = StorageLayout.RECEIPTS_DIR;

    @GetMapping
    public ResponseEntity<String> download(@RequestParam String name) throws Exception {
        Path path = Paths.get(BASE_DIR + name);
        byte[] content = Files.readAllBytes(path);
        return ResponseEntity.ok(new String(content));
    }
}
