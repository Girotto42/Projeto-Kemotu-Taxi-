package com.esf.frota.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "veiculo")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veiculo")
    private Long idVeiculo;

    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 7, message = "A placa deve conter exatamente 7 caracteres")
    @Column(nullable = false, unique = true, length = 7)
    private String placa;

    @NotBlank(message = "A marca é obrigatória")
    @Column(nullable = false, length = 50)
    private String marca;

    @NotBlank(message = "O modelo é obrigatório")
    @Column(nullable = false, length = 60)
    private String modelo;

    @NotNull(message = "O ano de fabricação é obrigatório")
    @Min(value = 1950, message = "Ano de fabricação inválido")
    @Column(name = "ano_fabricacao", nullable = false)
    private Integer anoFabricacao;

    @NotNull(message = "A quantidade de portas é obrigatória")
    @Column(name = "quantidade_portas", nullable = false)
    private Byte quantidadePortas = 4;

    @NotNull(message = "A quilometragem é obrigatória")
    @DecimalMin(value = "0.0", message = "A quilometragem não pode ser negativa")
    @Column(nullable = false)
    private BigDecimal quilometragem = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoVeiculo situacao = SituacaoVeiculo.Disponivel;

    public enum SituacaoVeiculo {
        Disponivel, Em_uso, Em_manutencao, Inativo
    }
}