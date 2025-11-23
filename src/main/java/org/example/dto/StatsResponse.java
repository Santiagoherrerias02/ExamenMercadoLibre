package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estadísticas de verificaciones de ADN")
public class StatsResponse {

    @Schema(description = "Cantidad de ADN mutante verificado", example = "40")
    @JsonProperty("count_mutant_dna")
    private long countMutantDna;

    @Schema(description = "Cantidad de ADN humano verificado", example = "100")
    @JsonProperty("count_human_dna")
    private long countHumanDna;

    @Schema(description = "Ratio de mutantes sobre humanos", example = "0.4")
    @JsonProperty("ratio")
    private double ratio;
}
