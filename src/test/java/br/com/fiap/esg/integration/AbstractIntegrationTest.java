package br.com.fiap.esg.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
abstract class AbstractIntegrationTest {

    @Container
    static final OracleContainer ORACLE = new OracleContainer("gvenzl/oracle-free:23-slim-faststart");

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void configurarBancoDeTeste(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ORACLE::getJdbcUrl);
        registry.add("spring.datasource.username", ORACLE::getUsername);
        registry.add("spring.datasource.password", ORACLE::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "oracle.jdbc.OracleDriver");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("api.security.token.secret", () -> "12345678");
    }

    @BeforeEach
    void limparBanco() {
        jdbcTemplate.execute("DELETE FROM ESG_ALERTA_META");
        jdbcTemplate.execute("DELETE FROM ESG_META_CONSUMO");
        jdbcTemplate.execute("DELETE FROM ESG_CONSUMO_ENERGETICO");
        jdbcTemplate.execute("DELETE FROM ESG_SENSOR");
        jdbcTemplate.execute("DELETE FROM ESG_DISPOSITIVO");
        jdbcTemplate.execute("DELETE FROM ESG_UNIDADE_CONSUMIDORA");
        jdbcTemplate.execute("DELETE FROM ESG_ENDERECO");
        jdbcTemplate.execute("DELETE FROM ESG_USUARIO");
    }

    protected Integer contarRegistros(String tabela) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tabela, Integer.class);
    }

    protected Boolean tabelaExiste(String tabela) {
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM USER_TABLES WHERE UPPER(TABLE_NAME) = UPPER(?)",
                Integer.class,
                tabela
        );

        return total != null && total > 0;
    }
}
