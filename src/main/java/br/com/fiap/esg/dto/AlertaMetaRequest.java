package br.com.fiap.esg.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AlertaMetaRequest(
        @NotNull(message = "O ID da Meta de Consumo é obrigatório")
        Long idMetaConsumo,

        @NotNull(message = "O valor do alerta é obrigatório")
        BigDecimal valorAlertaMeta,

        @NotNull(message = "A data do alerta é obrigatória")
        LocalDate dataAlertaMeta
) {
}