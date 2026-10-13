package com.estudos.service;

import com.estudos.dto.MateriaRequest;
import com.estudos.dto.MateriaResponse;
import com.estudos.entity.Materia;
import com.estudos.entity.Usuario;
import com.estudos.exception.RecursoNaoEncontradoException;
import com.estudos.repository.MateriaRepository;
import com.estudos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;

    public MateriaService(MateriaRepository materiaRepository, UsuarioRepository usuarioRepository) {
        this.materiaRepository = materiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MateriaResponse criar(MateriaRequest req) {
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        Materia materia = new Materia(req.nome().trim(), req.descricao(), usuario);
        return MateriaResponse.de(materiaRepository.save(materia));
    }

    @Transactional(readOnly = true)
    public List<MateriaResponse> listar(Long usuarioId) {
        List<Materia> materias = usuarioId == null
                ? materiaRepository.findAll()
                : materiaRepository.findByUsuarioId(usuarioId);
        return materias.stream().map(MateriaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public MateriaResponse buscar(Long id) {
        return MateriaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public MateriaResponse atualizar(Long id, MateriaRequest req) {
        Materia materia = buscarEntidade(id);
        materia.setNome(req.nome().trim());
        materia.setDescricao(req.descricao());
        return MateriaResponse.de(materiaRepository.save(materia));
    }

    @Transactional
    public void excluir(Long id) {
        materiaRepository.delete(buscarEntidade(id));
    }

    private Materia buscarEntidade(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria não encontrada"));
    }
}
