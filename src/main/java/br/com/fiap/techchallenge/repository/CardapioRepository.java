package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardapioRepository extends JpaRepository<Cardapio,Long> {

    Optional<Cardapio> findByRestauranteId(Long idRestaurante);
}
