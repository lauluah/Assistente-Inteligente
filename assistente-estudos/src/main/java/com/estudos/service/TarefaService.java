package com.estudos.service;

import com.estudos.dto.TarefaRequest;
import com.estudos.dto.TarefaResponse;
import com.estudos.entity.Materia;
import com.estudos.entity.Prioridade;
import com.estudos.entity.StatusTarefa;
import com.estudos.entity.Tarefa;
import com.estudos.exception.RecursoNaoEncontradoException;
import com.estudos.repository.MateriaRepository;
import com.estudos.repository.TarefaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final MateriaRepository materiaRepository;

    public TarefaService(TarefaRepository tarefaRepository, MateriaRepository materiaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.materiaRepository = materiaRepository;
    }

    @Transactional
    public TarefaResponse criar(TarefaRequest req) {
        Materia materia = buscarMateria(req.materiaId());
        Tarefa tarefa = new Tarefa();
        preencher(tarefa, req, materia);
        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    @Transactional
    public List<TarefaResponse> listar(Long usuarioId, StatusTarefa status, Prioridade prioridade,
                                       Long materiaId, LocalDate dataEntrega) {
        atualizarAtrasadas();

        Specification<Tarefa> spec = (root, query, cb) -> cb.conjunction();
        if (usuarioId != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("materia").get("usuario").get("id"), usuarioId));
        }
        if (status != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), status));
        }
        if (prioridade != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("prioridade"), prioridade));
        }
        if (materiaId != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("materia").get("id"), materiaId));
        }
        if (dataEntrega != null) {
            spec = spec.and((r, q, cb) -> cb.equal(r.get("dataEntrega"), dataEntrega));
        }
        return tarefaRepository.findAll(spec).stream().map(TarefaResponse::de).toList();
    }

    @Transactional
    public TarefaResponse buscar(Long id) {
        atualizarAtrasadas();
        return TarefaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public TarefaResponse atualizar(Long id, TarefaRequest req) {
        Tarefa tarefa = buscarEntidade(id);
        Materia materia = buscarMateria(req.materiaId());
        preencher(tarefa, req, materia);
        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaResponse alterarStatus(Long id, StatusTarefa status) {
        Tarefa tarefa = buscarEntidade(id);
        tarefa.setStatus(status);
        return TarefaResponse.de(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaResponse concluir(Long id) {
        return alterarStatus(id, StatusTarefa.CONCLUIDA);
    }

    @Transactional
    public void excluir(Long id) {
        tarefaRepository.delete(buscarEntidade(id));
    }

    @Transactional
    public List<TarefaResponse> proximasDoVencimento(Long usuarioId, int dias) {
        atualizarAtrasadas();
        LocalDate hoje = LocalDate.now();
        return tarefaRepository
                .findByMateriaUsuarioIdAndStatusInAndDataEntregaBetweenOrderByDataEntregaAsc(
                        usuarioId, List.of(StatusTarefa.PENDENTE, StatusTarefa.EM_ANDAMENTO),
                        hoje, hoje.plusDays(dias))
                .stream().map(TarefaResponse::de).toList();
    }

    @Transactional
    public List<TarefaResponse> atrasadas(Long usuarioId) {
        atualizarAtrasadas();
        return tarefaRepository.findByMateriaUsuarioIdAndStatus(usuarioId, StatusTarefa.ATRASADA)
                .stream().map(TarefaResponse::de).toList();
    }

    @Transactional
    public void atualizarAtrasadas() {
        tarefaRepository.marcarAtrasadas(LocalDate.now());
    }

    private void preencher(Tarefa tarefa, TarefaRequest req, Materia materia) {
        tarefa.setTitulo(req.titulo().trim());
        tarefa.setDescricao(req.descricao());
        tarefa.setMateria(materia);
        tarefa.setPrioridade(req.prioridade());
        tarefa.setDataEntrega(req.dataEntrega());
        if (req.status() != null) {
            tarefa.setStatus(req.status());
        }

        if (tarefa.getStatus() == StatusTarefa.ATRASADA && !req.dataEntrega().isBefore(LocalDate.now())) {
            tarefa.setStatus(StatusTarefa.PENDENTE);
        }
    }

    private Tarefa buscarEntidade(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));
    }

    private Materia buscarMateria(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria não encontrada"));
    }
}
