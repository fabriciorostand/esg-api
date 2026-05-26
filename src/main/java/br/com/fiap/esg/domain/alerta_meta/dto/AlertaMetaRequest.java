package br.com.fiap.esg.domain.alerta_meta.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AlertaMetaRequest(
        @NotNull(message = "O ID da Meta de Consumo é obrigatório")
        Long idMetaConsumo,

        @NotNull(message = "O valor do alerta é obrigatório")
        @Positive(message = "O valor do alerta deve ser maior que zero")
        BigDecimal valorAlertaMeta,

        @NotNull(message = "A data do alerta é obrigatória")
        LocalDate dataAlertaMeta
) {
}
