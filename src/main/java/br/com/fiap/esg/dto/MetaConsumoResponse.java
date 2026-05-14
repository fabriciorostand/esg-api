package br.com.fiap.esg.dto;

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