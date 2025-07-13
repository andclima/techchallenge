package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.LoginRequest;
import br.com.fiap.techchallenge.dto.LoginResponse;
import br.com.fiap.techchallenge.repository.UsuarioRepository;
import br.com.fiap.techchallenge.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest dto) {
        var user = userRepository.findByUsername(dto.username());
        if (user.isEmpty() || !passwordEncoder.matches(dto.password(), user.get().getPassword())) {
            throw new BadCredentialsException("user or password is invalid!");
        }
        var expiresIn = 300L; // 5 minutes
        var jwtValue = tokenService.generateToken(dto.username(), expiresIn);
        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
