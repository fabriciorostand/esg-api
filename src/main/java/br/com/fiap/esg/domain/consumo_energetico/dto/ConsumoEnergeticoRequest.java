package br.com.fiap.esg.domain.consumo_energetico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoEnergeticoRequest(

        @NotNull(message = "O ID do Sensor e obrigatorio")
        Long idSensor,

        @NotNull(message = "O consumo em kWh e obrigatorio")
        @Positive(message = "O consumo em kWh deve ser maior que zero")
        BigDecimal kwhConsumido,

        @NotNull(message = "A data de medicao e obrigatoria")
        LocalDate dataMedicao

) {
}
