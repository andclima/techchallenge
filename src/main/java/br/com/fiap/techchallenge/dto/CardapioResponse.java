package br.com.fiap.techchallenge.dto;

import br.com.fiap.techchallenge.model.Restaurante;
import jakarta.persistence.FetchType;

import java.util.List;


public record CardapioResponse(
        Long id,
        String nomeCardapio,
        String nomeRestaurante,
        List<ItemCardapioResponse> itens
){
}
