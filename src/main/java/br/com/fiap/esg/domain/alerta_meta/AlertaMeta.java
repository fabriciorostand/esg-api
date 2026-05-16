package br.com.fiap.esg.domain.alerta_meta;

import br.com.fiap.esg.domain.meta_consumo.MetaConsumo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ESG_ALERTA_META")
public class AlertaMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ESG_ALERTA_META")
    @SequenceGenerator(name = "SEQ_ESG_ALERTA_META", sequenceName = "SEQ_ESG_ALERTA_META", allocationSize = 1)
    @Column(name = "id_alerta_meta")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_meta_consumo", nullable = false)
    private MetaConsumo metaConsumo;

    @Column(name = "valor_alerta_meta", nullable = false)
    private BigDecimal valorAlertaMeta;

    @Column(name = "data_alerta_meta", nullable = false)
    private LocalDate dataAlertaMeta;
}
