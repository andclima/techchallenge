package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.DonoRestaurante;
import br.com.fiap.techchallenge.model.Restaurante;
import br.com.fiap.techchallenge.model.TipoCozinha;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestauranteRepositoryTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Test
    public void deveBuscarTodosRestaurantes(){
        var restaurante = criarRestauranteTeste();
        List<Restaurante> restaurantes = new ArrayList<>();
        restaurantes.add(restaurante);
        when(restauranteRepository.findAll()).thenReturn(restaurantes);

        var listaRestaurantes = restauranteRepository.findAll();

        Assertions.assertNotNull(listaRestaurantes);
        Assertions.assertEquals(restaurantes.size(), listaRestaurantes.size());
        Assertions.assertEquals(restaurantes, listaRestaurantes);
    }
    @Test
    public void deveBuscarPorId(){
        var restaurante = criarRestauranteTeste();
        when(restauranteRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurante));

        var buscaId = restauranteRepository.findById(1L);

        assertThat(buscaId.get()).isInstanceOf(Restaurante.class).isNotNull().isEqualTo(restaurante);
    }
    @Test
    public void deveBuscarPorNomeECep(){
        var restaurante = criarRestauranteTeste();
        when(restauranteRepository.findByNomeAndCep(any(String.class),any(String.class))).thenReturn(Optional.of(restaurante));

        var buscaNomeCep = restauranteRepository.
                findByNomeAndCep("Casa dos sabores","40203010");
        assertThat(buscaNomeCep.get()).isInstanceOf(Restaurante.class).isNotNull().isEqualTo(restaurante);
    }
    @Test
    public void deveCriarRestaurante(){
        var restaurante = criarRestauranteTeste();
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        var restauranteCriado = restauranteRepository.save(restaurante);

        assertThat(restauranteCriado).isNotNull();
        verify(restauranteRepository,times(1)).save(restaurante);
    }

    @Test
    public void deveDeletarRestauranteId(){
        var restaurante = criarRestauranteTeste();
        doNothing().when(restauranteRepository).deleteById(any(Long.class));

        restauranteRepository.deleteById(1L);

        verify(restauranteRepository,times(1)).deleteById(1L);
    }

    private Restaurante criarRestauranteTeste() {
        DonoRestaurante dono = new DonoRestaurante();
        dono.setId(1L);
        dono.setNome("Carlos Pereira");
        TipoCozinha tipoCozinha = new TipoCozinha();
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Cantina da Praça");
        restaurante.setEndereco("Rua Principal, 10");
        restaurante.setCep("12345-678");
        restaurante.setContato("11999998888");
        restaurante.setHoraFuncionamento(LocalTime.of(12, 0), LocalTime.of(22, 0));
        restaurante.setTipoCozinha(tipoCozinha);
        restaurante.setDono(dono);
        return restaurante;
    }
}
