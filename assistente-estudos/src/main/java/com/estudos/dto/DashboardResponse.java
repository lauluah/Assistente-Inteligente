package com.estudos.dto;

import java.util.List;

public record DashboardResponse(
        long totalTarefas,
        long concluidas,
        long pendentes,
        long atrasadas,
        int progresso,
        String materiaComMaisTarefas,
        List<ProgressoMateria> materias) {
}
