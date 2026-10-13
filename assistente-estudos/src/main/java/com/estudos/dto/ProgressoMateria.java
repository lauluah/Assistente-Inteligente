package com.estudos.dto;

public record ProgressoMateria(Long materiaId, String materia, long total, long concluidas, int progresso) {
}
