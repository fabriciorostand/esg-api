package br.com.fiap.esg.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoEnergeticoRequest(

        @NotNull(message = "O ID do Sensor e obrigatorio")
        Long idSensor,

        @NotNull(message = "O consumo em kWh e obrigatorio")
        BigDecimal kwhConsumido,

        @NotNull(message = "A data de medicao e obrigatoria")
        LocalDate dataMedicao

) {
}