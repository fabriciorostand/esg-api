package br.com.fiap.esg.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "{email.obrigatorio}")
        @Email(message = "{email.login.invalido}")
        String email,

        @NotBlank(message = "{senha.obrigatoria}")
        String senha

) {
}