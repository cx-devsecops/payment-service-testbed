package com.example.payment.vendor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interpolador de plantillas copiado desde una utilidad interna heredada
 * (derivado de StringSubstitutor) para evitar una dependencia externa.
 *
 * <p>Resuelve marcadores {@code ${clave}} y soporta los prefijos de busqueda
 * {@code env:}, {@code sys:} y {@code url:}.
 */
public final class StringInterpolator {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([^}]+)}");
    private static final int MAX_DEPTH = 5;

    private StringInterpolator() {
    }

    public static String interpolate(String template) {
        return expand(template, 0);
    }

    private static String expand(String template, int depth) {
        if (template == null || depth >= MAX_DEPTH) {
            return template;
        }

        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuilder result = new StringBuilder();
        int cursor = 0;

        while (matcher.find()) {
            result.append(template, cursor, matcher.start());
            result.append(expand(resolve(matcher.group(1)), depth + 1));
            cursor = matcher.end();
        }
        result.append(template.substring(cursor));
        return result.toString();
    }

    private static String resolve(String key) {
        if (key.startsWith("env:")) {
            String value = System.getenv(key.substring(4));
            return value == null ? "" : value;
        }
        if (key.startsWith("sys:")) {
            return System.getProperty(key.substring(4), "");
        }
        if (key.startsWith("url:")) {
            return fetch(key.substring(4));
        }
        return "";
    }

    private static String fetch(String location) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(location).openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            StringBuilder body = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }
            return body.toString();
        } catch (Exception ex) {
            return "";
        }
    }
}
