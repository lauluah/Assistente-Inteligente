package com.estudos.dto;

import com.estudos.entity.Materia;

public record MateriaResponse(Long id, String nome, String descricao, Long usuarioId) {

    public static MateriaResponse de(Materia m) {
        return new MateriaResponse(m.getId(), m.getNome(), m.getDescricao(), m.getUsuario().getId());
    }
}
