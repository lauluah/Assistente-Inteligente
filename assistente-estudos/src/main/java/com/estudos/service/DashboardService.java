package com.estudos.service;

import com.estudos.dto.DashboardResponse;
import com.estudos.dto.ProgressoMateria;
import com.estudos.entity.StatusTarefa;
import com.estudos.entity.Tarefa;
import com.estudos.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TarefaRepository tarefaRepository;
    private final TarefaService tarefaService;

    public DashboardService(TarefaRepository tarefaRepository, TarefaService tarefaService) {
        this.tarefaRepository = tarefaRepository;
        this.tarefaService = tarefaService;
    }

    @Transactional
    public DashboardResponse gerar(Long usuarioId) {
        tarefaService.atualizarAtrasadas();
        List<Tarefa> tarefas = tarefaRepository.findByMateriaUsuarioId(usuarioId);

        long total = tarefas.size();
        long concluidas = contar(tarefas, StatusTarefa.CONCLUIDA);
        long atrasadas = contar(tarefas, StatusTarefa.ATRASADA);
        long pendentes = total - concluidas - atrasadas; // PENDENTE + EM_ANDAMENTO


        Map<Long, List<Tarefa>> porMateria = tarefas.stream()
                .collect(Collectors.groupingBy(t -> t.getMateria().getId(), LinkedHashMap::new, Collectors.toList()));

        List<ProgressoMateria> materias = porMateria.values().stream()
                .map(lista -> {
                    long conc = contar(lista, StatusTarefa.CONCLUIDA);
                    return new ProgressoMateria(lista.get(0).getMateria().getId(),
                            lista.get(0).getMateria().getNome(), lista.size(), conc,
                            calcularPercentual(conc, lista.size()));
                })
                .sorted(Comparator.comparingInt(ProgressoMateria::progresso).reversed())
                .toList();

        String maisTarefas = materias.stream()
                .max(Comparator.comparingLong(ProgressoMateria::total))
                .map(ProgressoMateria::materia)
                .orElse(null);

        return new DashboardResponse(total, concluidas, pendentes, atrasadas,
                calcularPercentual(concluidas, total), maisTarefas, materias);
    }

    public static int calcularPercentual(long concluidas, long total) {
        return total == 0 ? 0 : (int) (concluidas * 100 / total);
    }

    private long contar(List<Tarefa> tarefas, StatusTarefa status) {
        return tarefas.stream().filter(t -> t.getStatus() == status).count();
    }
}
