package br.com.fiap.esg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DispositivoRequest(
        @NotNull(message = "O ID da Unidade Consumidora é obrigatório")
        Long idUnidadeConsumidora,

        @NotBlank(message = "O nome do dispositivo é obrigatório")
        String nome,

        @NotNull(message = "A potência nominal é obrigatória")
        Double potenciaNominal,

        @NotBlank(message = "O status é obrigatório")
        String status,

        @NotNull(message = "O consumo mínimo ativo é obrigatório")
        Double consumoMinimoAtivo,

        @NotNull(message = "O tempo de ociosidade limite é obrigatório")
        Integer tempoOciosidadeLimite
) {
}
