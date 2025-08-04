package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.LoginRequest;
import br.com.fiap.techchallenge.dto.LoginResponse;
import br.com.fiap.techchallenge.exception.CredenciaisInvalidasException;
import br.com.fiap.techchallenge.repository.UsuarioRepository;
import br.com.fiap.techchallenge.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(UsuarioRepository userRepository,
                          PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest dto) {

        userRepository.findByUsername(dto.username())
                .filter(user -> passwordEncoder.matches(dto.password(), user.getPassword()))
                .orElseThrow(() -> new CredenciaisInvalidasException("Usuário ou senha inválidos."));

        var expiresIn = 300L;
        var jwtValue = tokenService.generateToken(dto.username(), expiresIn);

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}