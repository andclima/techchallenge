package br.com.fiap.techchallenge.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GerarSenhaTeste implements CommandLineRunner {

    private final PasswordEncoder encoder;

    public GerarSenhaTeste(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String senhaLimpa = "admin123";
        System.out.println("Senha limpa: " + senhaLimpa + " -> " + "Senha criptografada: " + encoder.encode(senhaLimpa));
    }
}
