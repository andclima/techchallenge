package br.com.fiap.techchallenge.service;

import br.com.fiap.techchallenge.dto.LoginRequest;
import br.com.fiap.techchallenge.exception.CredenciaisInvalidasException;
import br.com.fiap.techchallenge.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void autenticar(LoginRequest dto) {
        usuarioRepository.findByUsername(dto.username())
                .filter(user -> passwordEncoder.matches(dto.password(), user.getPassword()))
                .orElseThrow(() -> new CredenciaisInvalidasException("Usuário ou senha inválidos."));
    }
}
