package br.com.fiap.techchallenge.dto;

public record UpdateUsuarioRequest(Long id, String nome, String email, String endereco, String nomeDoRestaurante, String numeroFidelidade) {

}
