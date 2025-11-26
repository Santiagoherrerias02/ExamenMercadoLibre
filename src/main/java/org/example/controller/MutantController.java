package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.DnaRequest;
import org.example.dto.ErrorResponse;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para detectar mutantes mediante análisis de ADN")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    /**
     * POST /mutant
     *
     * Verifica si una secuencia de ADN es mutante.
     *
     * Respuestas:
     * - 200 OK: Es mutante
     * - 403 Forbidden: No es mutante (humano)
     * - 400 Bad Request: ADN inválido
     */
    @PostMapping("/mutant")
    @Operation(
            summary = "Verificar si un ADN es mutante",
            description = "Analiza una secuencia de ADN y determina si corresponde a un mutante. " +
                    "Un mutante tiene más de una secuencia de 4 letras iguales en cualquier dirección."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Es mutante"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No es mutante (humano)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ADN inválido (matriz no cuadrada, caracteres inválidos, etc.)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.analyzeDna(request.getDna());

        return isMutant
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * GET /stats
     *
     * Obtiene estadísticas de las verificaciones de ADN.
     *
     * Retorna:
     * - count_mutant_dna: Cantidad de mutantes detectados
     * - count_human_dna: Cantidad de humanos detectados
     * - ratio: Proporción mutantes/humanos
     */
    @GetMapping("/stats")
    @Operation(
            summary = "Obtener estadísticas",
            description = "Retorna estadísticas sobre las verificaciones de ADN realizadas"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estadísticas obtenidas correctamente",
            content = @Content(schema = @Schema(implementation = StatsResponse.class))
    )
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}