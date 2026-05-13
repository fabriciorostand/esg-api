package br.com.fiap.esg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ESG_META_CONSUMO")
public class MetaConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_META_CONSUMO")
    @SequenceGenerator(name = "SEQ_META_CONSUMO", sequenceName = "SEQ_META_CONSUMO", allocationSize = 1)
    @Column(name = "id_meta_consumo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_dispositivo", nullable = false)
    private Dispositivo dispositivo;

    @Column(name = "tipo", nullable = false, length = 3)
    private String tipo;

    @Column(name = "meta_kwh", nullable = false)
    private Double metaKwh;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;
}