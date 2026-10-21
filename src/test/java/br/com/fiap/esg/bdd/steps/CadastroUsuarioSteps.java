package br.com.fiap.esg.bdd.steps;

import br.com.fiap.esg.domain.usuario.dto.RegistroRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class CadastroUsuarioSteps {

    @LocalServerPort
    private int porta;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RespostaSteps respostaSteps;

    private RegistroRequest request;

    @Dado("que eu tenha os seguintes dados do usuário:")
    public void queEuTenhaOsSeguintesDadosDoUsuario(DataTable tabela) {
        Map<String, String> dados = tabela.subTable(1, 0).asMap(String.class, String.class);
        request = new RegistroRequest(dados.get("email"), dados.get("senha"));
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de usuarios")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeUsuarios(String endpoint) throws Exception {
        Response resposta = given()
                .baseUri("http://localhost")
                .port(porta)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post(endpoint);

        respostaSteps.guardarResposta(resposta);
    }

    @Então("deve conter o identificador do usuário cadastrado")
    public void deveConterOIdentificadorDoUsuarioCadastrado() {
        Long usuarioId = respostaSteps.obterResposta().then()
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("email", equalTo(request.email()))
                .body("$", not(hasKey("senha")))
                .extract().jsonPath().getLong("id");

        assertThat(usuarioId).isPositive();
    }

}
