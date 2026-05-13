package br.com.fiap.esg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ESG_ALERTA_META")
public class AlertaMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALERTA_META")
    @SequenceGenerator(name = "SEQ_ALERTA_META", sequenceName = "SEQ_ALERTA_META", allocationSize = 1)
    @Column(name = "id_alerta_meta")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_meta_consumo", nullable = false)
    private MetaConsumo metaConsumo;

    @Column(name = "valor_alerta_meta", nullable = false)
    private Double valorAlertaMeta;

    @Column(name = "data_alerta_meta", nullable = false)
    private LocalDate dataAlertaMeta;
}