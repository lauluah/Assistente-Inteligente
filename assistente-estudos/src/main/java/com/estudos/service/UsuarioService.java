package com.estudos.service;

import com.estudos.dto.UsuarioRequest;
import com.estudos.dto.UsuarioResponse;
import com.estudos.entity.Usuario;
import com.estudos.exception.RecursoNaoEncontradoException;
import com.estudos.exception.RegraNegocioException;
import com.estudos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest req) {
        String email = req.email().trim().toLowerCase();
        if (repository.existsByEmail(email)) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }
        Usuario usuario = new Usuario(req.nome().trim(), email, passwordEncoder.encode(req.senha()));
        return UsuarioResponse.de(repository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(Long id) {
        return UsuarioResponse.de(repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado")));
    }
}
