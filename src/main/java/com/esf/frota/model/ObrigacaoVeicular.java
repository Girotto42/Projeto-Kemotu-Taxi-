package com.esf.frota.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "obrigacao_veicular")
public class ObrigacaoVeicular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_obrigacao")
    private Long idObrigacao;

    @NotNull(message = "O veículo é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_veiculo", nullable = false)
    private Veiculo veiculo;

    @NotBlank(message = "O tipo de obrigação é obrigatório")
    @Column(name = "tipo_obrigacao", nullable = false, length = 50)
    private String tipoObrigacao;

    @NotNull(message = "O exercício é obrigatório")
    @Column(nullable = false)
    private Integer exercicio;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo")
    @Column(nullable = false)
    private BigDecimal valor;

    @NotNull(message = "A data de vencimento é obrigatória")
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoObrigacao situacao = SituacaoObrigacao.Pendente;

    public enum SituacaoObrigacao {
        Pendente, Pago, Vencido
    }
}