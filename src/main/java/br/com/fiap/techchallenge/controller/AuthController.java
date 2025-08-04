package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.LoginRequest;
import br.com.fiap.techchallenge.dto.LoginResponse;
import br.com.fiap.techchallenge.service.AuthenticationService;
import br.com.fiap.techchallenge.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    public AuthController(AuthenticationService authenticationService, TokenService tokenService) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest dto) {
        authenticationService.autenticar(dto);

        var expiresIn = 3600L;
        var jwtValue = tokenService.generateToken(dto.username(), expiresIn);

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}