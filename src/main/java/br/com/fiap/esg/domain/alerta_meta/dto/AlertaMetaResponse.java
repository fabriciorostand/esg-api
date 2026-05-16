package br.com.fiap.esg.domain.alerta_meta.dto;

import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlertaMetaResponse(
        Long id,
        MetaConsumoResponse metaConsumo,
        BigDecimal valorAlertaMeta,
        LocalDate dataAlertaMeta
) {
}