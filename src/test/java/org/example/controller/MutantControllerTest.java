package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK para ADN mutante")
    void testCheckMutantReturns200ForMutant() throws Exception {
        // ARRANGE
        String[] mutantDna = {
                "ATGCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest(mutantDna);

        when(mutantService.analyzeDna(any(String[].class)))
                .thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden para ADN humano")
    void testCheckMutantReturns403ForHuman() throws Exception {
        // ARRANGE
        String[] humanDna = {
                "ATGCGA", "CAGTGC", "TTATTT",
                "AGACGG", "GCGTCA", "TCACTG"
        };
        DnaRequest request = new DnaRequest(humanDna);

        when(mutantService.analyzeDna(any(String[].class)))
                .thenReturn(false);

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para ADN nulo")
    void testCheckMutantReturns400ForNullDna() throws Exception {
        // ARRANGE
        DnaRequest request = new DnaRequest(null);

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para ADN vacío")
    void testCheckMutantReturns400ForEmptyDna() throws Exception {
        // ARRANGE
        DnaRequest request = new DnaRequest(new String[]{});

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /stats debe retornar estadísticas correctamente")
    void testGetStatsReturnsCorrectData() throws Exception {
        // ARRANGE
        StatsResponse statsResponse = new StatsResponse(40, 100, 0.4);
        when(statsService.getStats()).thenReturn(statsResponse);

        // ACT & ASSERT
        mockMvc.perform(
                        get("/stats")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK incluso sin datos")
    void testGetStatsReturns200WithNoData() throws Exception {
        // ARRANGE
        StatsResponse statsResponse = new StatsResponse(0, 0, 0.0);
        when(statsService.getStats()).thenReturn(statsResponse);

        // ACT & ASSERT
        mockMvc.perform(
                        get("/stats")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(0))
                .andExpect(jsonPath("$.count_human_dna").value(0))
                .andExpect(jsonPath("$.ratio").value(0.0));
    }

    @Test
    @DisplayName("POST /mutant debe rechazar request sin body")
    void testCheckMutantRejectsEmptyBody() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe aceptar Content-Type application/json")
    void testCheckMutantAcceptsJsonContentType() throws Exception {
        // ARRANGE
        String[] mutantDna = {
                "ATGCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest(mutantDna);

        when(mutantService.analyzeDna(any(String[].class)))
                .thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }
}