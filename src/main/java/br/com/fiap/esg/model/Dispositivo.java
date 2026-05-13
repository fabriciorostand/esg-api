package br.com.fiap.esg.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ESG_DISPOSITIVO")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DISPOSITIVO")
    @SequenceGenerator(name = "SEQ_DISPOSITIVO", sequenceName = "SEQ_DISPOSITIVO", allocationSize = 1)
    @Column(name = "id_dispositivo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_unidade_consumidora", nullable = false)
    private UnidadeConsumidora unidadeConsumidora;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "potencia_nominal", nullable = false)
    private Double potenciaNominal;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "consumo_minimo_ativo", nullable = false)
    private Double consumoMinimoAtivo;

    @Column(name = "tempo_ociosidade_limite", nullable = false)
    private Integer tempoOciosidadeLimite;
}