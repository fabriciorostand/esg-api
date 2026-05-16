package br.com.fiap.esg.domain.consumo_energetico.dto;

import br.com.fiap.esg.domain.sensor.dto.SensorResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoEnergeticoResponse(

        Long id,

        SensorResponse sensor,

        BigDecimal kwhConsumido,

        LocalDate dataMedicao

) {
}
