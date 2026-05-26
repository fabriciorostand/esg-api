package br.com.fiap.esg.domain.unidade_consumidora.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UnidadeConsumidoraRequest(

        @NotNull
        @Valid
        EnderecoRequest endereco,

        @NotBlank
        String nome,

        @NotBlank
        String tipo,

        @NotNull
        @Positive(message = "A área total deve ser maior que zero")
        BigDecimal areaTotal

) {
}
