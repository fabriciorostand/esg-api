package br.com.fiap.esg.bdd.infra;

import br.com.fiap.esg.EsgApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.oracle.OracleContainer;

import java.time.Duration;

@CucumberContextConfiguration
@SpringBootTest(classes = EsgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {

    private static final OracleContainer ORACLE =
            new OracleContainer("gvenzl/oracle-free:23-slim-faststart")
                    .withStartupTimeout(Duration.ofMinutes(3));

    @DynamicPropertySource
    static void configurarBancoDeTeste(DynamicPropertyRegistry registry) {
        ORACLE.start();
        registry.add("spring.datasource.url", ORACLE::getJdbcUrl);
        registry.add("spring.datasource.username", ORACLE::getUsername);
        registry.add("spring.datasource.password", ORACLE::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "oracle.jdbc.OracleDriver");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("api.security.token.secret", () -> "12345678");
    }
}
