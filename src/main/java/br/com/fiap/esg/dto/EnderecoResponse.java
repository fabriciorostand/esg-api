package br.com.fiap.esg.dto;

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