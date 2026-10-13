package com.estudos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "Nome não pode estar vazio") String nome,
        @NotBlank(message = "E-mail não pode estar vazio") @Email(message = "E-mail inválido") String email,
        @NotBlank(message = "Senha não pode estar vazia")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres") String senha) {
}
