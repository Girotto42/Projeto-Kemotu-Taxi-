package com.esf.frota.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Long idFuncionario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, length = 50)
    private String cargo;

    private String telefone;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String senha; // Necessário para a autenticação JWT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoFuncionario situacao = SituacaoFuncionario.Ativo;

    public enum SituacaoFuncionario {
        Ativo, Inativo
    }
}