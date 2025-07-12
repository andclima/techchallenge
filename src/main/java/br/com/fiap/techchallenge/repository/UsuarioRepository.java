package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public Optional<Usuario> findById(Long id);
    public Optional<Usuario> findByEmail(String email);
    public Optional<Usuario> findByUsername(String username);
    public List<Usuario> findAllByNomeContainingIgnoreCase(String nome);
}
