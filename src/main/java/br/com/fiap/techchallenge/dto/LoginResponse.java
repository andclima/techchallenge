package br.com.fiap.techchallenge.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
