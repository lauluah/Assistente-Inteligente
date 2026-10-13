package com.estudos.dto;

import com.estudos.entity.Prioridade;
import com.estudos.entity.StatusTarefa;
import com.estudos.entity.Tarefa;

import java.time.LocalDate;

public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        Long materiaId,
        String materia,
        Prioridade prioridade,
        LocalDate dataEntrega,
        StatusTarefa status) {

    public static TarefaResponse de(Tarefa t) {
        return new TarefaResponse(t.getId(), t.getTitulo(), t.getDescricao(),
                t.getMateria().getId(), t.getMateria().getNome(),
                t.getPrioridade(), t.getDataEntrega(), t.getStatus());
    }
}
