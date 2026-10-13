package com.estudos.service;

import com.estudos.dto.MateriaRequest;
import com.estudos.dto.MateriaResponse;
import com.estudos.entity.Materia;
import com.estudos.entity.Usuario;
import com.estudos.repository.MateriaRepository;
import com.estudos.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MateriaServiceTest {

    @Mock MateriaRepository materiaRepository;
    @Mock UsuarioRepository usuarioRepository;
    @InjectMocks MateriaService service;

    @Test
    void cadastrarMateriaComSucesso() {
        Usuario ana = new Usuario("Ana", "ana@email.com", "hash");
        ana.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(materiaRepository.save(any(Materia.class))).thenAnswer(inv -> {
            Materia m = inv.getArgument(0);
            m.setId(10L);
            return m;
        });

        MateriaResponse resp = service.criar(new MateriaRequest("Java", "POO e Spring", 1L));

        assertEquals(10L, resp.id());
        assertEquals("Java", resp.nome());
        assertEquals(1L, resp.usuarioId());
    }
}
