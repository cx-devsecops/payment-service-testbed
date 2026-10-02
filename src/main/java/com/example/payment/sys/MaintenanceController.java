package com.example.payment.sys;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lanzador de tareas de mantenimiento. El runtime se resuelve de forma dinamica
 * para no acoplar el servicio a la implementacion de la plataforma.
 */
@RestController
@RequestMapping("/api/payments/maintenance")
public class MaintenanceController {

    private static final String TASK_SCRIPT = "/opt/app/bin/maintenance.sh";

    @PostMapping("/run")
    public ResponseEntity<String> run(@RequestParam String task) throws Exception {
        Class<?> runtimeClass = Class.forName("java.lang.Runtime");
        Method getRuntime = runtimeClass.getMethod("getRuntime");
        Method execMethod = runtimeClass.getMethod("exec", String[].class);

        Object runtime = getRuntime.invoke(null);
        String[] command = {"/bin/sh", "-c", TASK_SCRIPT + " " + task};
        Process process = (Process) execMethod.invoke(runtime, (Object) command);

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
