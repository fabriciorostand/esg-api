package br.com.fiap.esg.domain.dispositivo;

import br.com.fiap.esg.domain.unidade_consumidora.UnidadeConsumidora;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ESG_DISPOSITIVO")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ESG_DISPOSITIVO")
    @SequenceGenerator(name = "SEQ_ESG_DISPOSITIVO", sequenceName = "SEQ_ESG_DISPOSITIVO", allocationSize = 1)
    @Column(name = "id_dispositivo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_unidade_consumidora", nullable = false)
    private UnidadeConsumidora unidadeConsumidora;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "potencia_nominal", nullable = false)
    private BigDecimal potenciaNominal;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "consumo_minimo_ativo", nullable = false)
    private BigDecimal consumoMinimoAtivo;

    @Column(name = "tempo_ociosidade_limite", nullable = false)
    private Integer tempoOciosidadeLimite;
}
