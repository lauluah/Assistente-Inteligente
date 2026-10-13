package com.estudos.service;

import com.estudos.dto.TarefaRequest;
import com.estudos.dto.TarefaResponse;
import com.estudos.entity.*;
import com.estudos.exception.RecursoNaoEncontradoException;
import com.estudos.repository.MateriaRepository;
import com.estudos.repository.TarefaRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock TarefaRepository tarefaRepository;
    @Mock MateriaRepository materiaRepository;
    @InjectMocks TarefaService service;

    private Materia materia() {
        Materia m = new Materia("SQL", null, new Usuario("Ana", "ana@email.com", "hash"));
        m.setId(1L);
        return m;
    }

    private Tarefa tarefa(StatusTarefa status) {
        Tarefa t = new Tarefa();
        t.setId(5L);
        t.setTitulo("Estudar JOIN");
        t.setMateria(materia());
        t.setPrioridade(Prioridade.ALTA);
        t.setDataEntrega(LocalDate.now().plusDays(1));
        t.setStatus(status);
        return t;
    }

    @Test
    void cadastrarTarefaComSucesso() {
        when(materiaRepository.findById(1L)).thenReturn(Optional.of(materia()));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(inv -> inv.getArgument(0));

        TarefaRequest req = new TarefaRequest("Estudar JOIN", "Inner e left", 1L,
                Prioridade.ALTA, LocalDate.of(2026, 10, 15), null);
        TarefaResponse resp = service.criar(req);

        assertEquals("Estudar JOIN", resp.titulo());
        assertEquals(StatusTarefa.PENDENTE, resp.status());
        assertEquals("SQL", resp.materia());
    }

    @Test
    void impedirTarefaSemTitulo() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        TarefaRequest req = new TarefaRequest("  ", null, 1L, Prioridade.BAIXA, LocalDate.now(), null);

        assertFalse(validator.validate(req).isEmpty());
    }

    @Test
    void buscarTarefaExistente() {
        when(tarefaRepository.findById(5L)).thenReturn(Optional.of(tarefa(StatusTarefa.PENDENTE)));

        assertEquals(5L, service.buscar(5L).id());
    }

    @Test
    void retornarErroQuandoTarefaNaoExistir() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex =
                assertThrows(RecursoNaoEncontradoException.class, () -> service.buscar(99L));
        assertEquals("Tarefa não encontrada", ex.getMessage());
    }

    @Test
    void concluirTarefa() {
        when(tarefaRepository.findById(5L)).thenReturn(Optional.of(tarefa(StatusTarefa.EM_ANDAMENTO)));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(inv -> inv.getArgument(0));

        assertEquals(StatusTarefa.CONCLUIDA, service.concluir(5L).status());
    }
}
