package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante,Long> {

    Optional<Restaurante> findByNomeAndCep(String nome,String cep);
}
