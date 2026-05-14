package br.com.fiap.esg.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlertaMetaResponse(
        Long id,
        MetaConsumoResponse metaConsumo,
        BigDecimal valorAlertaMeta,
        LocalDate dataAlertaMeta
) {
}