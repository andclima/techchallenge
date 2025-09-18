package br.com.fiap.techchallenge.model;

import br.com.fiap.techchallenge.dto.CreateRestauranteRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;

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
    @ManyToOne(cascade = CascadeType.ALL)
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setTipoCozinha(TipoCozinha tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public void setHoraFuncionamento(LocalTime horaAbertura, LocalTime horaFechamento) {
        this.horaFuncionamento = "Das "+ horaAbertura +" até " + horaFechamento;
    }

    public void setDono(DonoRestaurante dono) {
        this.dono = dono;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}
