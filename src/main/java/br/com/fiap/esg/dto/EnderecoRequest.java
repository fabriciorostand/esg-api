package br.com.fiap.esg.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRequest(

        @NotBlank
        String bairro,

        @NotBlank
        String rua,

        @NotBlank
        String numero,

        @NotBlank
        String cep,

        @NotBlank
        String cidade,

        @NotBlank
        String uf

) {
}