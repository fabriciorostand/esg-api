package br.com.fiap.esg.domain.unidade_consumidora.dto;

public record EnderecoResponse(

        Long id,

        String bairro,

        String rua,

        String numero,

        String cep,

        String cidade,

        String uf

) {
}