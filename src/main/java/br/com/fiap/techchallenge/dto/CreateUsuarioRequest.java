package br.com.fiap.techchallenge.dto;

public record CreateUsuarioRequest(String nome, String email, String username, String password, String endereco, String tipo, String nomeDoRestaurante, String numeroFidelidade ) {

}
