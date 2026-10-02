package com.example.payment.vendor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/notifications")
public class TemplateController {

    @PostMapping("/preview")
    public ResponseEntity<String> preview(@RequestBody String template) {
        return ResponseEntity.ok(StringInterpolator.interpolate(template));
    }
}
