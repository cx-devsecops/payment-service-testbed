package com.example.payment.gateway;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class GatewayClient {

    private static final String KEY_ENV = "live";
    private static final String KEY_PART_A = "sk_" + KEY_ENV + "_";
    private static final String KEY_PART_B = "7QbW2nR9xL4kM1pZ";
    private static final String KEY_PART_C = "8vT3yU6iO0aS5dF2";

    private static final String LEGACY_BASIC_AUTH = "cGF5bWVudHM6UzNjcjN0MFAtTGVnYWN5LTIwMjQh";

    public String authorizationHeader() {
        return "Bearer " + KEY_PART_A + KEY_PART_B + KEY_PART_C;
    }

    public String legacyAuthorizationHeader() {
        byte[] decoded = Base64.getDecoder().decode(LEGACY_BASIC_AUTH);
        return "Basic " + new String(decoded, StandardCharsets.UTF_8);
    }
}
