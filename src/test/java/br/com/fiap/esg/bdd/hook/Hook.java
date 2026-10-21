package br.com.fiap.esg.bdd.hook;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

public class Hook {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void setUpBeforeClass() {
        // Simulação de configuração global antes da execução de todos os testes
        inicializarAmbiente();
    }

    @AfterAll
    public static void tearDownAfterClass() {
        // Simulação de limpeza global após a execução de todos os testes
        limparAmbiente();
    }

    @Before
    public void setUp() {
        // Simulação de configuração antes da execução de cada cenário
        prepararDadosParaTeste();
    }

    @After
    public void tearDown() {
        // Simulação de limpeza após a execução de cada cenário
        limparDadosDepoisDoTeste();
    }

    @Before
    public void limparBancoDeTeste() {
        for (String tabela : new String[]{
                "ESG_ALERTA_META",
                "ESG_META_CONSUMO",
                "ESG_CONSUMO_ENERGETICO",
                "ESG_SENSOR",
                "ESG_DISPOSITIVO",
                "ESG_UNIDADE_CONSUMIDORA",
                "ESG_ENDERECO",
                "ESG_USUARIO"
        }) {
            jdbcTemplate.execute("DELETE FROM " + tabela);
        }
    }

    private static void inicializarAmbiente() {
        // Simulação de inicialização do ambiente
        System.out.println("Ambiente inicializado.");
    }

    private static void limparAmbiente() {
        // Simulação de limpeza do ambiente
        System.out.println("Ambiente limpo.");
    }

    private void prepararDadosParaTeste() {
        // Simulação de preparação de dados para o teste
        System.out.println("Dados preparados para o cenário de teste.");
    }

    private void limparDadosDepoisDoTeste() {
        // Simulação de limpeza de dados após o teste
        System.out.println("Dados limpos após o cenário de teste.");
    }

}
