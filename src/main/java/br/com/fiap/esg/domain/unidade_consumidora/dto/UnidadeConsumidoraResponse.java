package br.com.fiap.esg.domain.unidade_consumidora.dto;

import java.math.BigDecimal;

public record UnidadeConsumidoraResponse(

        Long id,

        EnderecoResponse endereco,

        String nome,

        String tipo,

        BigDecimal areaTotal

) {
}
