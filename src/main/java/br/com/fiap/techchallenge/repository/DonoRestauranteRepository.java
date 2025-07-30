package br.com.fiap.techchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.techchallenge.model.DonoRestaurante;

public interface DonoRestauranteRepository extends JpaRepository<DonoRestaurante, Long>{
  
}
