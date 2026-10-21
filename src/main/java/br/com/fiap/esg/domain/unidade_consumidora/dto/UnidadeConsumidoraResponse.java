package br.com.fiap.esg.domain.unidade_consumidora.dto;

import br.com.fiap.esg.domain.unidade_consumidora.Tipo;

import java.math.BigDecimal;

public record UnidadeConsumidoraResponse(

        Long id,

        EnderecoResponse endereco,

        String nome,

        Tipo tipo,

        BigDecimal areaTotal

) {
}
