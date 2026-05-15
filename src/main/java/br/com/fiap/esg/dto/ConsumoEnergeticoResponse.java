package br.com.fiap.esg.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoEnergeticoResponse(

        Long id,

        SensorResponse sensor,

        BigDecimal kwhConsumido,

        LocalDate dataMedicao

) {
}
