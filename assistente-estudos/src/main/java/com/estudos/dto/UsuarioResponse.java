package com.estudos.dto;

import com.estudos.entity.Usuario;


public record UsuarioResponse(Long id, String nome, String email) {

    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail());
    }
}
