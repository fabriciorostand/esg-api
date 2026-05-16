package br.com.fiap.esg.domain.consumo_energetico;

import br.com.fiap.esg.domain.sensor.Sensor;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity(name = "ConsumoEnergetico")
@Table(name = "ESG_CONSUMO_ENERGETICO")
public class ConsumoEnergetico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ESG_CONSUMO_ENERGETICO")
    @SequenceGenerator(name = "SEQ_ESG_CONSUMO_ENERGETICO", sequenceName = "SEQ_ESG_CONSUMO_ENERGETICO", allocationSize = 1)
    @Column(name = "id_consumo_energetico")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    @Column(name = "kwh_consumido", nullable = false)
    private BigDecimal kwhConsumido;

    @Column(name = "data_medicao", nullable = false)
    private LocalDate dataMedicao;

}
