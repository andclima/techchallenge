package br.com.fiap.techchallenge.model;

import br.com.fiap.techchallenge.dto.CreateRestauranteRequest;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "restaurante")
@Getter
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String endereco;
    private String cep;

    @ManyToOne
    @JoinColumn(name = "tipo_cozinha_id", referencedColumnName = "id")
    private TipoCozinha tipoCozinha;
    private String horaFuncionamento;
    @ManyToOne
    @JoinColumn(name = "dono_id", referencedColumnName = "idUsuario")//
    private DonoRestaurante dono;
    private String contato;

    public Restaurante() {
    }

    public Restaurante(CreateRestauranteRequest request, TipoCozinha cozinha, DonoRestaurante dono) {
        this.nome = request.nomeRestaurante();
        this.endereco = request.endereco();
        this.cep = request.cep();
        this.tipoCozinha = cozinha;
        this.horaFuncionamento = "Das "+ request.horaAbertura() +" até " + request.horaFechamento();
        this.dono = dono;
        this.contato = request.contato();
    }
}
