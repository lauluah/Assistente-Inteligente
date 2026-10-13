package com.estudos.dto;

import com.estudos.entity.StatusTarefa;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull(message = "Status é obrigatório") StatusTarefa status) {
}
