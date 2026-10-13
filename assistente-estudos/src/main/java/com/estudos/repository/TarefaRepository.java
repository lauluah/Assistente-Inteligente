package com.estudos.repository;

import com.estudos.entity.StatusTarefa;
import com.estudos.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long>, JpaSpecificationExecutor<Tarefa> {

    List<Tarefa> findByMateriaUsuarioId(Long usuarioId);

    List<Tarefa> findByMateriaUsuarioIdAndStatus(Long usuarioId, StatusTarefa status);

    List<Tarefa> findByMateriaUsuarioIdAndStatusInAndDataEntregaBetweenOrderByDataEntregaAsc(
            Long usuarioId, Collection<StatusTarefa> status, LocalDate inicio, LocalDate fim);


    @Modifying
    @Query("""
            UPDATE Tarefa t SET t.status = com.estudos.entity.StatusTarefa.ATRASADA
            WHERE t.dataEntrega < :hoje
              AND t.status IN (com.estudos.entity.StatusTarefa.PENDENTE,
                               com.estudos.entity.StatusTarefa.EM_ANDAMENTO)
            """)
    int marcarAtrasadas(@Param("hoje") LocalDate hoje);
}
