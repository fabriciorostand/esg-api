package br.com.fiap.esg.domain.meta_consumo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MetaConsumoRequest(
        @NotNull(message = "O ID do Dispositivo é obrigatório")
        Long idDispositivo,

        @NotBlank(message = "O tipo da meta é obrigatório")
        @Size(max = 3, message = "O tipo deve ter no máximo 3 caracteres (ex: MTH, YR)")
        @Pattern(regexp = "DIA|MES|ANO|MTH|YR", message = "O tipo deve ser DIA, MES, ANO, MTH ou YR")
        String tipo,

        @NotNull(message = "O valor da meta (kWh) é obrigatório")
        @Positive(message = "O valor da meta (kWh) deve ser maior que zero")
        BigDecimal metaKwh,

        @NotNull(message = "A data de início é obrigatória")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim é obrigatória")
        LocalDate dataFim
) {
}
