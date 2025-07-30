package br.com.fiap.techchallenge.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.techchallenge.dto.ClienteResponse;
import br.com.fiap.techchallenge.dto.DonoRestauranteResponse;
import br.com.fiap.techchallenge.model.Cliente;
import br.com.fiap.techchallenge.model.DonoRestaurante;
import br.com.fiap.techchallenge.model.TipoUsuario;

@Configuration
public class ModelMapperConfig {
  
  @Bean
  ModelMapper modelMapper() {
      var modelMapper = new ModelMapper();

      modelMapper.typeMap(DonoRestaurante.class, DonoRestauranteResponse.class)
      .setProvider(req -> {
          DonoRestaurante d = (DonoRestaurante) req.getSource();
          return new DonoRestauranteResponse(
              d.getId(), d.getNome(), d.getEmail(), d.getUsername(), 
              d.getEndereco(), d.getNomeDoRestaurante(), TipoUsuario.DONO.name()
          );
      });

    modelMapper.typeMap(Cliente.class, ClienteResponse.class)
        .setProvider(req -> {
            Cliente c = (Cliente) req.getSource();
            return new ClienteResponse(
                c.getId(), c.getNome(), c.getEmail(), c.getUsername(), 
                c.getEndereco(), c.getNumeroFidelidade(), TipoUsuario.CLIENTE.name()
            );
        });

    return modelMapper;
  }
}
