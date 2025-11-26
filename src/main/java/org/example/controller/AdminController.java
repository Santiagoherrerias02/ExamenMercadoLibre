package org.example.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador administrativo para gestión de datos de demostración
 * Protegido con token secreto
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Hidden // Oculto en Swagger
public class AdminController {

    private final DnaRecordRepository dnaRecordRepository;

    @Value("${admin.secret.token:default-secret-change-me}")
    private String adminToken;

    /**
     * POST /admin/seed?token=SECRET
     *
     * Pobla la base de datos con datos de demostración:
     * - 40 mutantes
     * - 100 humanos
     * - Ratio: 0.4
     *
     * IMPORTANTE: Solo ejecutar en bases de datos vacías o de prueba
     */
    @PostMapping("/seed")
    public ResponseEntity<Map<String, Object>> seedDatabase(
            @RequestParam(required = false) String token) {

        Map<String, Object> response = new HashMap<>();

        // Validar token
        if (token == null || !adminToken.equals(token)) {
            response.put("error", "Forbidden: Invalid or missing token");
            response.put("message", "Use: POST /admin/seed?token=YOUR_SECRET");
            return ResponseEntity.status(403).body(response);
        }

        // Verificar si ya hay muchos datos
        long existingCount = dnaRecordRepository.count();
        if (existingCount >= 140) {
            response.put("error", "Database already populated");
            response.put("current_count", existingCount);
            response.put("message", "Use /admin/clear first if you want to reset");
            return ResponseEntity.badRequest().body(response);
        }

        List<DnaRecord> records = new ArrayList<>();

        // Insertar 40 mutantes
        for (int i = 1; i <= 40; i++) {
            String hash = String.format("demo_mutant_%03d_%d", i, System.currentTimeMillis());
            records.add(new DnaRecord(hash, true));
        }

        // Insertar 100 humanos
        for (int i = 1; i <= 100; i++) {
            String hash = String.format("demo_human_%03d_%d", i, System.currentTimeMillis());
            records.add(new DnaRecord(hash, false));
        }

        // Guardar todos
        dnaRecordRepository.saveAll(records);

        // Respuesta
        response.put("success", true);
        response.put("message", "Database seeded successfully");
        response.put("mutants_added", 40);
        response.put("humans_added", 100);
        response.put("total_records", dnaRecordRepository.count());
        response.put("expected_ratio", 0.4);

        return ResponseEntity.ok(response);
    }

    /**
     * POST /admin/clear?token=SECRET
     *
     * Limpia TODA la base de datos
     *
     * ⚠️ PELIGROSO: Elimina todos los registros
     */
    @PostMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearDatabase(
            @RequestParam(required = false) String token) {

        Map<String, Object> response = new HashMap<>();

        // Validar token
        if (token == null || !adminToken.equals(token)) {
            response.put("error", "Forbidden: Invalid or missing token");
            return ResponseEntity.status(403).body(response);
        }

        long count = dnaRecordRepository.count();
        dnaRecordRepository.deleteAll();

        response.put("success", true);
        response.put("message", "Database cleared");
        response.put("records_deleted", count);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /admin/status?token=SECRET
     *
     * Muestra el estado actual de la base de datos
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDatabaseStatus(
            @RequestParam(required = false) String token) {

        Map<String, Object> response = new HashMap<>();

        // Validar token
        if (token == null || !adminToken.equals(token)) {
            response.put("error", "Forbidden: Invalid or missing token");
            return ResponseEntity.status(403).body(response);
        }

        long mutantCount = dnaRecordRepository.countByIsMutant(true);
        long humanCount = dnaRecordRepository.countByIsMutant(false);
        long totalCount = dnaRecordRepository.count();
        double ratio = humanCount > 0 ? (double) mutantCount / humanCount : (mutantCount > 0 ? mutantCount : 0.0);

        response.put("total_records", totalCount);
        response.put("mutant_count", mutantCount);
        response.put("human_count", humanCount);
        response.put("ratio", ratio);
        response.put("target_ratio", 0.4);
        response.put("is_target_ratio", Math.abs(ratio - 0.4) < 0.01);

        return ResponseEntity.ok(response);
    }
}