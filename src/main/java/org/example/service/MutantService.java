package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.exception.DnaHashCalculationException;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    /**
     * Analiza un ADN y determina si es mutante.
     * Implementa caché con hash SHA-256 para evitar reprocesar.
     *
     * Flujo:
     * 1. Calcular hash del DNA
     * 2. Buscar en BD (caché)
     * 3. Si existe → retornar resultado cacheado
     * 4. Si no existe → analizar, guardar y retornar
     */
    public boolean analyzeDna(String[] dna) {
        // Calcular hash único del DNA
        String dnaHash = calculateDnaHash(dna);

        // Buscar en caché (BD)
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);

        if (existingRecord.isPresent()) {
            // Retornar resultado cacheado (no reprocesar)
            return existingRecord.get().isMutant();
        }

        // Analizar DNA (primera vez)
        boolean isMutant = mutantDetector.isMutant(dna);

        // Guardar resultado en BD (caché)
        DnaRecord record = new DnaRecord(dnaHash, isMutant);
        dnaRecordRepository.save(record);

        return isMutant;
    }

    /**
     * Calcula hash SHA-256 del DNA.
     * Esto permite:
     * - Detectar DNAs duplicados
     * - Búsqueda O(1) con índice en BD
     * - Ahorrar espacio (64 chars vs matriz completa)
     */
    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Concatenar todas las filas
            String dnaString = String.join("", dna);

            // Calcular hash
            byte[] hashBytes = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));

            // Convertir a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error al calcular hash SHA-256", e);
        }
    }
}