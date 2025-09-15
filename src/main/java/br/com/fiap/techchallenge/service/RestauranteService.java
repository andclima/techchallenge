package br.com.fiap.techchallenge.service;

import br.com.fiap.techchallenge.dto.CreateRestauranteRequest;
import br.com.fiap.techchallenge.dto.RestauranteResponse;
import br.com.fiap.techchallenge.exception.CredenciaisInvalidasException;
import br.com.fiap.techchallenge.exception.DadoDuplicadoException;
import br.com.fiap.techchallenge.exception.RecursoNaoEncontradoException;
import br.com.fiap.techchallenge.model.DonoRestaurante;
import br.com.fiap.techchallenge.model.Restaurante;
import br.com.fiap.techchallenge.model.TipoCozinha;
import br.com.fiap.techchallenge.repository.DonoRestauranteRepository;
import br.com.fiap.techchallenge.repository.RestauranteRepository;
import br.com.fiap.techchallenge.repository.TipoCozinhaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteService {
    private final RestauranteRepository restauranteRepository;
    private final TipoCozinhaRepository tipoCozinhaRepository;
    private final DonoRestauranteRepository donoRestauranteRepository;
    public RestauranteService(RestauranteRepository restauranteRepository, TipoCozinhaRepository tipoCozinhaRepository, DonoRestauranteRepository donoRestauranteRepository) {
        this.restauranteRepository = restauranteRepository;
        this.tipoCozinhaRepository = tipoCozinhaRepository;
        this.donoRestauranteRepository = donoRestauranteRepository;
    }
    public List<RestauranteResponse> buscarRestaurante(){
        var restauranteList = restauranteRepository.findAll();
        var restaurantes = restauranteResponseMethod(restauranteList);
        return restaurantes;
    }
    public Restaurante criarRestaurante(CreateRestauranteRequest request){
        if (restauranteRepository.findByNomeAndCep(request.nomeRestaurante(), request.cep()).isPresent()){
            throw new DadoDuplicadoException("O restaurante já existe");
        }
        DonoRestaurante dono = donoRestauranteRepository.findById(request.dono()).
                orElseThrow(() -> new CredenciaisInvalidasException("Dono de restaurante não encontrado"));

        TipoCozinha cozinha = tipoCozinhaRepository.findById(request.tipoCozinha())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de cozinha não encontrado"));
        Restaurante restaurante = new Restaurante(request,cozinha,dono);
        return restauranteRepository.save(restaurante);
    }
    private List<RestauranteResponse> restauranteResponseMethod(List<Restaurante> restaurantes){
        List<RestauranteResponse> responseList = restaurantes.stream().map(restaurante -> new RestauranteResponse(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getEndereco(),
                restaurante.getCep(),
                restaurante.getTipoCozinha().getTipo(),
                restaurante.getHoraFuncionamento(),
                restaurante.getContato(),
                restaurante.getDono().getNome(),
                restaurante.getDono().getEmail()
        )).toList();
        return responseList;
    }
}
