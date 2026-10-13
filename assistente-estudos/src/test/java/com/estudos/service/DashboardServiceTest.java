package com.estudos.service;

import com.estudos.dto.DashboardResponse;
import com.estudos.entity.*;
import com.estudos.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock TarefaRepository tarefaRepository;
    @Mock TarefaService tarefaService;
    @InjectMocks DashboardService service;

    private Tarefa tarefa(Materia m, StatusTarefa status) {
        Tarefa t = new Tarefa();
        t.setTitulo("t");
        t.setMateria(m);
        t.setPrioridade(Prioridade.MEDIA);
        t.setDataEntrega(LocalDate.now());
        t.setStatus(status);
        return t;
    }

    @Test
    void calcularPercentualDeProgresso() {
        assertEquals(75, DashboardService.calcularPercentual(15, 20));
        assertEquals(0, DashboardService.calcularPercentual(0, 0));
    }

    @Test
    void gerarDashboard() {
        Materia java = new Materia("Java", null, null);
        java.setId(1L);
        Materia sql = new Materia("SQL", null, null);
        sql.setId(2L);

        List<Tarefa> tarefas = new ArrayList<>();
        tarefas.add(tarefa(java, StatusTarefa.CONCLUIDA));
        tarefas.add(tarefa(java, StatusTarefa.CONCLUIDA));
        tarefas.add(tarefa(java, StatusTarefa.PENDENTE));
        tarefas.add(tarefa(sql, StatusTarefa.ATRASADA));
        tarefas.add(tarefa(sql, StatusTarefa.EM_ANDAMENTO));
        when(tarefaRepository.findByMateriaUsuarioId(1L)).thenReturn(tarefas);

        DashboardResponse d = service.gerar(1L);

        assertEquals(5, d.totalTarefas());
        assertEquals(2, d.concluidas());
        assertEquals(2, d.pendentes());
        assertEquals(1, d.atrasadas());
        assertEquals(40, d.progresso());
        assertEquals("Java", d.materiaComMaisTarefas());
    }
}
