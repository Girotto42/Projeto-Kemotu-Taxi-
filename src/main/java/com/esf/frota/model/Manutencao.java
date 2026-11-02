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
@Table(name = "manutencao")
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_manutencao")
    private Long idManutencao;

    @NotNull(message = "O veículo é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_veiculo", nullable = false)
    private Veiculo veiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_manutencao", nullable = false)
    private TipoManutencao tipoManutencao;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "data_manutencao")
    private LocalDate dataManutencao;

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @DecimalMin(value = "0.0", message = "A quilometragem não pode ser negativa")
    private BigDecimal quilometragem;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo")
    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoManutencao situacao = SituacaoManutencao.Programada;

    public enum TipoManutencao {
        Preventiva, Corretiva
    }

    public enum SituacaoManutencao {
        Programada, Em_andamento, Concluida, Cancelada
    }
}