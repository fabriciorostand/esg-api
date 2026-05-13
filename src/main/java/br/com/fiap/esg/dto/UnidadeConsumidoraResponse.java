package br.com.fiap.esg.dto;

import java.math.BigDecimal;

public record UnidadeConsumidoraResponse(

        Long id,

        EnderecoResponse endereco,

        String nome,

        String tipo,

        BigDecimal areaTotal

) {
}
