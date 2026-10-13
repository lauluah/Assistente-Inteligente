package com.estudos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MateriaRequest(
        @NotBlank(message = "Nome da matéria não pode estar vazio") String nome,
        String descricao,
        @NotNull(message = "usuarioId é obrigatório") Long usuarioId) {
}
