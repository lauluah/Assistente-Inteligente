package com.estudos.dto;

import com.estudos.entity.Prioridade;
import com.estudos.entity.StatusTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record TarefaRequest(
        @NotBlank(message = "Título não pode estar vazio") String titulo,
        String descricao,
        @NotNull(message = "materiaId é obrigatório") Long materiaId,
        @NotNull(message = "Prioridade é obrigatória") Prioridade prioridade,
        @NotNull(message = "Data de entrega é obrigatória") LocalDate dataEntrega,
        StatusTarefa status) {
}
