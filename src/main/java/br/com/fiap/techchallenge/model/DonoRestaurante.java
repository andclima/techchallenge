package br.com.fiap.techchallenge.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="donorestaurante")
public class DonoRestaurante extends Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nomeDoRestaurante;
  
}
