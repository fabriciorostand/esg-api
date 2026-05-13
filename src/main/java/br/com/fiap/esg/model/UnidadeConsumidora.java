package br.com.fiap.esg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "UnidadeConsumidora")
@Table(name = "ESG_UNIDADE_CONSUMIDORA")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeConsumidora {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_unidade_consumidora")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco", nullable = false)
    Endereco endereco;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "area_total", nullable = false)
    private BigDecimal areaTotal;

}