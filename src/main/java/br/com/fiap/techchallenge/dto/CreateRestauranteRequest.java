package br.com.fiap.techchallenge.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalTime;

public record CreateRestauranteRequest (
        @NotBlank(message = "O campo ao lado não pode estar vazio")
        String nomeRestaurante,
        @NotBlank(message = "O campo ao lado não pode estar vazio")
        String endereco,
        @Pattern(regexp = "\\d{8}", message = "O CEP é inválido")
        @NotBlank(message = "O campo CEP não pode ser nulo")
        String cep,
        @NotNull(message = "O campo ao lado não pode estar vazio")
        Long tipoCozinha,
        @NotNull(message = "O campo ao lado não pode estar vazio")
        LocalTime horaAbertura,
        @NotNull(message = "O campo ao lado não pode estar vazio")
        LocalTime horaFechamento,
        @NotNull(message = "O campo ao lado não pode estar vazio")
        Long dono,
        @NotBlank(message = "O campo ao lado não pode estar vazio")
        String contato
){
}
