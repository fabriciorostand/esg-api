package br.com.fiap.esg.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity(name = "Endereco")
@Table(name = "ESG_ENDERECO")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ESG_ENDERECO")
    @SequenceGenerator(name = "SEQ_ESG_ENDERECO", sequenceName = "SEQ_ESG_ENDERECO", allocationSize = 1)
    @Column(name = "id_endereco")
    private Long id;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "rua", nullable = false)
    private String rua;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "uf", nullable = false)
    private String uf;

}