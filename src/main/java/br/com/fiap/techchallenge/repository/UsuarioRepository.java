package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findAllByNomeContainingIgnoreCase(String nome);
}
