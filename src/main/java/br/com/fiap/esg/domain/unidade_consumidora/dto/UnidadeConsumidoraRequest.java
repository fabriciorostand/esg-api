package br.com.fiap.esg.domain.unidade_consumidora.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UnidadeConsumidoraRequest(

        @NotNull
        EnderecoRequest endereco,

        @NotBlank
        String nome,

        @NotBlank
        String tipo,

        @NotNull
        BigDecimal areaTotal

) {
}