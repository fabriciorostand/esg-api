package br.com.fiap.esg.domain.unidade_consumidora.dto;

import br.com.fiap.esg.domain.unidade_consumidora.Tipo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UnidadeConsumidoraRequest(

        @NotNull
        @Valid
        EnderecoRequest endereco,

        @NotBlank(message = "O nome da unidade consumidora é obrigatório")
        String nome,

        @NotNull(message = "O tipo da unidade consumidora é obrigatório")
        Tipo tipo,

        @NotNull
        @Positive(message = "A área total deve ser maior que zero")
        BigDecimal areaTotal

) {
}
