package com.estudos.controller;

import com.estudos.dto.StatusRequest;
import com.estudos.dto.TarefaRequest;
import com.estudos.dto.TarefaResponse;
import com.estudos.entity.Prioridade;
import com.estudos.entity.StatusTarefa;
import com.estudos.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criar(@Valid @RequestBody TarefaRequest req) {
        return service.criar(req);
    }

    /** Filtros opcionais: status, prioridade, matéria, data de entrega (yyyy-MM-dd) e usuário. */
    @GetMapping
    public List<TarefaResponse> listar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) StatusTarefa status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) Long materiaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntrega) {
        return service.listar(usuarioId, status, prioridade, materiaId, dataEntrega);
    }

    @GetMapping("/proximas-do-vencimento")
    public List<TarefaResponse> proximas(@RequestParam Long usuarioId,
                                         @RequestParam(defaultValue = "3") int dias) {
        return service.proximasDoVencimento(usuarioId, dias);
    }

    @GetMapping("/atrasadas")
    public List<TarefaResponse> atrasadas(@RequestParam Long usuarioId) {
        return service.atrasadas(usuarioId);
    }

    @GetMapping("/{id}")
    public TarefaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public TarefaResponse atualizar(@PathVariable Long id, @Valid @RequestBody TarefaRequest req) {
        return service.atualizar(id, req);
    }

    @PatchMapping("/{id}/status")
    public TarefaResponse alterarStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest req) {
        return service.alterarStatus(id, req.status());
    }

    @PatchMapping("/{id}/concluir")
    public TarefaResponse concluir(@PathVariable Long id) {
        return service.concluir(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
