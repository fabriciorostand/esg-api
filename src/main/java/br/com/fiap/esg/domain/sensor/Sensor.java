package br.com.fiap.esg.domain.sensor;

import br.com.fiap.esg.domain.dispositivo.Dispositivo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ESG_SENSOR")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ESG_SENSOR")
    @SequenceGenerator(name = "SEQ_ESG_SENSOR", sequenceName = "SEQ_ESG_SENSOR", allocationSize = 1)
    @Column(name = "id_sensor")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_dispositivo", nullable = false)
    private Dispositivo dispositivo;

    @Column(name = "ativo", nullable = false, columnDefinition = "CHAR(1)")
    private String ativo;
}
