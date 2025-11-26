package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');
    private static final Pattern DNA_PATTERN = Pattern.compile("^[ATCG]+$");
    private static final int MIN_SIZE = 4;

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int n = dna.length;

        // Validar tamaño mínimo
        if (n < MIN_SIZE) {
            return false;
        }

        // Validar que sea matriz cuadrada NxN y que solo contenga A, T, C, G
        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false;
            }

            if (!DNA_PATTERN.matcher(row).matches()) {
                return false;
            }
        }

        return true;
    }
}