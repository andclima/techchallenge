package br.com.fiap.techchallenge.service;

import br.com.fiap.techchallenge.dto.CreateRestauranteRequest;
import br.com.fiap.techchallenge.dto.RestauranteResponse;
import br.com.fiap.techchallenge.dto.UpdateRestauranteRequest;
import br.com.fiap.techchallenge.exception.CredenciaisInvalidasException;
import br.com.fiap.techchallenge.exception.DadoDuplicadoException;
import br.com.fiap.techchallenge.exception.RecursoNaoEncontradoException;
import br.com.fiap.techchallenge.model.DonoRestaurante;
import br.com.fiap.techchallenge.model.Restaurante;
import br.com.fiap.techchallenge.model.TipoCozinha;
import br.com.fiap.techchallenge.repository.DonoRestauranteRepository;
import br.com.fiap.techchallenge.repository.RestauranteRepository;
import br.com.fiap.techchallenge.repository.TipoCozinhaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceTest {
    @InjectMocks
    private RestauranteService restauranteService;
    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private TipoCozinhaRepository tipoCozinhaRepository;
    @Mock
    private DonoRestauranteRepository donoRestauranteRepository;

    private Restaurante restaurante;
    @Test
    @DisplayName("Retornar lista com apenas 1 usuário")
    public void deveRetornarListaRestaurantes(){
        var restaurante = criarRestauranteTeste();
        when(restauranteRepository.findAll()).thenReturn(Collections.singletonList(restaurante));

        List<RestauranteResponse> restaurantes = restauranteService.buscarRestaurante();
        Assertions.assertEquals(1,restaurantes.size());
    }
    @Test
    public void deveRetornarRestaurantePorId(){
        var restaurante = criarRestauranteTeste();
        when(restauranteRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(restaurante));

        var restauranteEncontrado = restauranteService.
                buscarRestauranteId(1L);

        assertThat(restauranteEncontrado)
                .isNotNull();
        assertThat(restauranteEncontrado.nome()).isEqualTo(restaurante.getNome());
    }
    @Test
    public void lancarExcecaoQuandoRestauranteJaExistir(){
        var restauranteRequest = criarRestauranteTesteRequest();
        var restauranteExistente = criarRestauranteTeste();
        when(restauranteRepository.
                findByNomeAndCep(any(String.class),any(String.class))).thenReturn(Optional.of(restauranteExistente));

        Assertions.assertThrows(DadoDuplicadoException.class,
                ()-> restauranteService.criarRestaurante(restauranteRequest));
    }
    @Test
    public void lancarExcecaoQuandoDonoNaoForEncontrado(){
        CreateRestauranteRequest create = criarRestauranteTesteRequest();

        Assertions.assertThrows(CredenciaisInvalidasException.class, ()-> restauranteService.criarRestaurante(create));
    }
    @Test
    public void lancarExcecaoQuandoTipoCozinhaNaoEncontrado(){
        TipoCozinha tipoCozinha = new TipoCozinha();
        CreateRestauranteRequest create = criarRestauranteTesteRequest();
        DonoRestaurante dono = new DonoRestaurante();
        when(donoRestauranteRepository.findById(1L)).thenReturn(Optional.of(dono));
        when(tipoCozinhaRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class,
                ()-> restauranteService.criarRestaurante(create));
    }
    @Test
    public void deveCriarRestaurante(){
        var restauranteRequest = criarRestauranteTesteRequest();
        DonoRestaurante dono = new DonoRestaurante();
        TipoCozinha tipoCozinha = new TipoCozinha();
        when(restauranteRepository.
                findByNomeAndCep(restauranteRequest.nomeRestaurante(),
                        restauranteRequest.cep())).thenReturn(Optional.empty());

        when(donoRestauranteRepository.
                findById(any(Long.class))).thenReturn(Optional.of(dono));

        when(tipoCozinhaRepository.
                findById(any(Long.class))).thenReturn(Optional.of(tipoCozinha));

        when(restauranteRepository.
                save(any(Restaurante.class))).thenReturn(restaurante);

        var criarRestaurante = restauranteService.criarRestaurante(restauranteRequest);

        assertThat(criarRestaurante).isNotNull();
        verify(restauranteRepository,times(1)).save(any(Restaurante.class));
    }
    @Test
    public void deveEditarRestaurante(){
        var restaurante = criarRestauranteTeste();
        var restauranteEditado = editarUsuarioTesteRequest();
        DonoRestaurante dono = new DonoRestaurante();
        TipoCozinha tipoCozinha = new TipoCozinha();
        when(restauranteRepository.
                findById(any(Long.class))).thenReturn(Optional.of(restaurante));

        when(donoRestauranteRepository.
                findById(any(Long.class))).thenReturn(Optional.of(dono));

        when(tipoCozinhaRepository.
                findById(any(Long.class))).thenReturn(Optional.of(tipoCozinha));

        when(restauranteRepository.
                save(any(Restaurante.class))).thenReturn(any(Restaurante.class));

        var editarUsuario = restauranteService.editarRestaurante(restauranteEditado);

        assertThat(editarUsuario).isNotNull();
        assertThat(editarUsuario.nome()).isEqualTo(restauranteEditado.nomeRestaurante());
        assertThat(editarUsuario.nome()).isEqualTo(restaurante.getNome());
    }
    @Test
    public void deveExcluirRestaurante(){
        var restaurante = criarRestauranteTeste();
        when(restauranteRepository.
                findById(any(Long.class))).thenReturn(Optional.of(restaurante));
        doNothing().when(restauranteRepository).deleteById(1L);

        restauranteService.excluirRestaurante(1L);

        verify(restauranteRepository,times(1)).deleteById(1L);
    }
    private Restaurante criarRestauranteTeste(){
        DonoRestaurante dono = new DonoRestaurante();
        TipoCozinha tipoCozinha = new TipoCozinha();
        CreateRestauranteRequest create = new CreateRestauranteRequest(
                "Casa da fonte",
                "Rua Prateada",
                "12345678",
                1L,
                LocalTime.of(16,45),
                LocalTime.of(23,00),
                1L,
                "71888885555"
        );
        Restaurante restaurante = new Restaurante(create,tipoCozinha,dono);
        return new Restaurante(create,tipoCozinha,dono);
    }
    private CreateRestauranteRequest criarRestauranteTesteRequest(){
        return new CreateRestauranteRequest(
                "Casa da fonte",
                "Rua Prateada",
                "12345678",
                1L,
                LocalTime.of(16,45),
                LocalTime.of(23,00),
                1L,
                "71888885555"
        );
    }
    private UpdateRestauranteRequest editarUsuarioTesteRequest(){
        return new UpdateRestauranteRequest(
                1L, "Nome Atualizado", "Endereço Novo", "98765-432",
                2L, LocalTime.of(18, 0), LocalTime.of(23, 0),
                2L, "11912345678"
        );
    }
}