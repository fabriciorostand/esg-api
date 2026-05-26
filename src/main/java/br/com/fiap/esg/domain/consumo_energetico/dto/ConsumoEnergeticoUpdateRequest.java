package br.com.fiap.esg.domain.consumo_energetico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoEnergeticoUpdateRequest(

    @NotNull
    @Positive(message = "O consumo em kWh deve ser maior que zero")
    BigDecimal kwhConsumido,

    @NotNull
    LocalDate dataMedicao

) {
}
