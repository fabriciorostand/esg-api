package br.com.fiap.esg.bdd.steps;

import br.com.fiap.esg.domain.usuario.dto.LoginRequest;
import br.com.fiap.esg.domain.usuario.dto.RegistroRequest;
import io.cucumber.java.pt.Dado;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class AutenticacaoSteps {

    @LocalServerPort
    private int porta;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Dado("que estou autenticado")
    public void queEstouAutenticado() throws Exception {
        String email = "bdd@fiap.com.br";
        String senha = "Senha123";

        given()
                .baseUri("http://localhost")
                .port(porta)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(new RegistroRequest(email, senha)))
        .when()
                .post("/api/autenticacao/registro")
        .then()
                .statusCode(201);

        token = given()
                .baseUri("http://localhost")
                .port(porta)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(new LoginRequest(email, senha)))
        .when()
                .post("/api/autenticacao/login")
        .then()
                .statusCode(200)
                .body("token", not(emptyOrNullString()))
                .extract().path("token");
    }

    public String obterToken() {
        assertThat(token)
                .as("O cenário deve se autenticar antes de acessar um endpoint protegido")
                .isNotBlank();
        return token;
    }
}
