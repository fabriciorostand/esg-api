package br.com.fiap.esg.dto;

public record SensorResponse(
        Long id,
        DispositivoResponse dispositivo,
        String ativo
) {
}
