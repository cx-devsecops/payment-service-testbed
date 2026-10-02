package com.example.payment.files;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Provisiona los directorios de almacenamiento del servicio durante el arranque
 * y deja un documento de muestra para las pruebas de humo.
 */
@Component
public class StorageLayout {

    public static final String RECEIPTS_DIR =
            System.getProperty("java.io.tmpdir") + "/payments/receipts/";

    public static final String EVIDENCE_DIR =
            System.getProperty("java.io.tmpdir") + "/payments/chargebacks/evidence/";

    @Value("${payments.storage.seed:true}")
    private boolean seed;

    @PostConstruct
    public void provision() throws Exception {
        Path receipts = Paths.get(RECEIPTS_DIR);
        Path evidence = Paths.get(EVIDENCE_DIR);
        Files.createDirectories(receipts);
        Files.createDirectories(evidence);

        if (seed) {
            Path sampleReceipt = receipts.resolve("receipt-001.json");
            if (Files.notExists(sampleReceipt)) {
                Files.write(sampleReceipt,
                        "{\"receipt\":\"001\",\"merchantId\":\"merchant-demo\",\"amount\":12.50}"
                                .getBytes(StandardCharsets.UTF_8));
            }
            Path sampleEvidence = evidence.resolve("CB-1001.pdf");
            if (Files.notExists(sampleEvidence)) {
                Files.write(sampleEvidence,
                        "evidencia de contracargo CB-1001".getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
