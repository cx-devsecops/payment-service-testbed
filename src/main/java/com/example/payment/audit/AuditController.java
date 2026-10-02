package com.example.payment.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/audit")
public class AuditController {

    private static final Logger log = LoggerFactory.getLogger(AuditController.class);

    @PostMapping("/event")
    public ResponseEntity<String> record(@RequestParam String actor,
                                         @RequestParam String action,
                                         @RequestParam(required = false) String detail) {
        log.info("audit event actor=" + actor + " action=" + action + " detail=" + detail);
        return ResponseEntity.ok("recorded");
    }
}
