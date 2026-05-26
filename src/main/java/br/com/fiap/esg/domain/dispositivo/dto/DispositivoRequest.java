package br.com.fiap.esg.domain.dispositivo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record DispositivoRequest(
        @NotNull(message = "O ID da Unidade Consumidora é obrigatório")
        Long idUnidadeConsumidora,

        @NotBlank(message = "O nome do dispositivo é obrigatório")
        String nome,

        @NotNull(message = "A potência nominal é obrigatória")
        @Positive(message = "A potência nominal deve ser maior que zero")
        Double potenciaNominal,

        @NotBlank(message = "O status é obrigatório")
        @Pattern(regexp = "ATIVO|INATIVO|LIGADO|DESLIGADO", message = "O status deve ser ATIVO, INATIVO, LIGADO ou DESLIGADO")
        String status,

        @NotNull(message = "O consumo mínimo ativo é obrigatório")
        @Positive(message = "O consumo mínimo ativo deve ser maior que zero")
        Double consumoMinimoAtivo,

        @NotNull(message = "O tempo de ociosidade limite é obrigatório")
        @Positive(message = "O tempo de ociosidade limite deve ser maior que zero")
        Integer tempoOciosidadeLimite
) {
}
