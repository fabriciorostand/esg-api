package br.com.fiap.esg.domain.meta_consumo.dto;

import br.com.fiap.esg.domain.dispositivo.dto.DispositivoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetaConsumoResponse(
        Long id,
        DispositivoResponse dispositivo,
        String tipo,
        BigDecimal metaKwh,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}