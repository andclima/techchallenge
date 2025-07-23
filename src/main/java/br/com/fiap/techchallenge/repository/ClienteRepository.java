package br.com.fiap.techchallenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.techchallenge.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  Optional<Cliente> findByNumeroFidelidade(String numeroFidelidade);
}
