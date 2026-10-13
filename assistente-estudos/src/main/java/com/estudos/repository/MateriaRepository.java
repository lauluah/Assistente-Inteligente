package com.estudos.repository;

import com.estudos.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    List<Materia> findByUsuarioId(Long usuarioId);
}
