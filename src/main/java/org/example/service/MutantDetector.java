package org.example.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    /**
     * Determina si un ADN es mutante.
     * Un mutante tiene MÁS DE UNA secuencia de 4 letras iguales.
     *
     * Optimizaciones implementadas:
     * 1. Early Termination - Para al encontrar >1 secuencias
     * 2. Single Pass - Recorre la matriz una sola vez
     * 3. Boundary Checking - Solo busca donde hay espacio
     * 4. Direct Comparison - Comparaciones sin loops
     * 5. char[][] conversion - Acceso O(1) rápido
     *
     * Complejidad: O(N²) peor caso, ~O(N) promedio con early termination
     */
    public boolean isMutant(String[] dna) {
        if (!isValidDna(dna)) {
            return false;
        }

        final int n = dna.length;
        int sequenceCount = 0;

        // Optimización: Convertir a char[][] para acceso más rápido
        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // Single Pass: recorrer la matriz UNA SOLA VEZ
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Boundary Checking: Solo buscar donde hay espacio suficiente

                // Horizontal (→)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true; // Early Termination
                    }
                }

                // Vertical (↓)
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true; // Early Termination
                    }
                }

                // Diagonal Descendente (↘)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true; // Early Termination
                    }
                }

                // Diagonal Ascendente (↗)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true; // Early Termination
                    }
                }
            }
        }

        return false; // Solo encontró 0 o 1 secuencia
    }

    /**
     * Valida que el ADN sea correcto:
     * - No nulo ni vacío
     * - Matriz NxN (cuadrada)
     * - Solo caracteres A, T, C, G
     */
    private boolean isValidDna(String[] dna) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int n = dna.length;

        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false; // No es cuadrada
            }

            for (char c : row.toCharArray()) {
                if (!VALID_BASES.contains(c)) {
                    return false; // Carácter inválido
                }
            }
        }

        return true;
    }

    /**
     * Verifica secuencia horizontal (→)
     * Optimización: Comparación directa sin loops
     */
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    /**
     * Verifica secuencia vertical (↓)
     */
    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    /**
     * Verifica diagonal descendente (↘)
     */
    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    /**
     * Verifica diagonal ascendente (↗)
     */
    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
                matrix[row - 2][col + 2] == base &&
                matrix[row - 3][col + 3] == base;
    }
}
