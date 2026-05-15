package br.com.fiap.esg.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity(name = "UnidadeConsumidora")
@Table(name = "ESG_UNIDADE_CONSUMIDORA")
public class UnidadeConsumidora {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ESG_UNIDADE_CONSUMIDORA")
    @SequenceGenerator(name = "SEQ_ESG_UNIDADE_CONSUMIDORA", sequenceName = "SEQ_ESG_UNIDADE_CONSUMIDORA", allocationSize = 1)
    @Column(name = "id_unidade_consumidora")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco", nullable = false)
    private Endereco endereco;

    @Column(name = "nome", nullable = false, length = 35)
    private String nome;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "area_total", nullable = false)
    private BigDecimal areaTotal;

}