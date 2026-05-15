package br.com.fiap.esg.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoEnergeticoUpdateRequest(

    @NotNull
    BigDecimal kwhConsumido,

    @NotNull
    LocalDate dataMedicao

) {
}