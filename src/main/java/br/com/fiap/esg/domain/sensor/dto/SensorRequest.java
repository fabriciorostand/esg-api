package br.com.fiap.esg.domain.sensor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SensorRequest(
        @NotNull(message = "O ID do Dispositivo é obrigatório")
        Long idDispositivo,

        @NotBlank(message = "O status ativo é obrigatório")
        @Size(max = 1, message = "O status ativo deve ter apenas 1 caractere (ex: 'S' ou 'N')")
        @Pattern(regexp = "S|N", message = "O status ativo deve ser S ou N")
        String ativo
) {
}
