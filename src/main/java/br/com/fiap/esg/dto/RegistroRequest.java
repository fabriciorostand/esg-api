package br.com.fiap.esg.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistroRequest(

        @NotBlank(message = "{email.obrigatorio}")
        @Email(message = "{email.login.invalido}")
        String email,

        @NotBlank(message = "{senha.obrigatoria}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=\\-{}|:;\"'<>,.?/]{8,}$", message = "{senha.invalida}")
        String senha

) {
}