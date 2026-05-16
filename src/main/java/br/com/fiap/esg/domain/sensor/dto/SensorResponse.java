package br.com.fiap.esg.domain.sensor.dto;

import br.com.fiap.esg.domain.dispositivo.dto.DispositivoResponse;

public record SensorResponse(
        Long id,
        DispositivoResponse dispositivo,
        String ativo
) {
}
