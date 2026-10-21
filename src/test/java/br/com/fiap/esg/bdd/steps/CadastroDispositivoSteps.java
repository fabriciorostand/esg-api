package br.com.fiap.esg.bdd.steps;

import br.com.fiap.esg.domain.dispositivo.dto.DispositivoRequest;
import br.com.fiap.esg.domain.unidade_consumidora.Tipo;
import br.com.fiap.esg.domain.unidade_consumidora.dto.EnderecoRequest;
import br.com.fiap.esg.domain.unidade_consumidora.dto.UnidadeConsumidoraRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.DOUBLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CadastroDispositivoSteps {

    @LocalServerPort
    private int porta;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RespostaSteps respostaSteps;

    @Autowired
    private AutenticacaoSteps autenticacaoSteps;

    private Long unidadeId;
    private DispositivoRequest request;
    private Long dispositivoId;

    @Dado("existe uma unidade consumidora cadastrada")
    public void existeUmaUnidadeConsumidoraCadastrada() throws Exception {
        EnderecoRequest endereco = new EnderecoRequest(
                "Centro", "Rua das Flores", "100", "01001000", "Sao Paulo", "SP");
        UnidadeConsumidoraRequest unidade = new UnidadeConsumidoraRequest(
                endereco, "Unidade BDD", Tipo.COMERCIAL, new BigDecimal("250.75"));

        unidadeId = given()
                .baseUri("http://localhost")
                .port(porta)
                .header("Authorization", "Bearer " + autenticacaoSteps.obterToken())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(unidade))
        .when()
                .post("/api/unidadesconsumidoras")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().jsonPath().getLong("id");
    }

    @Dado("existe um dispositivo cadastrado nessa unidade consumidora")
    public void existeUmDispositivoCadastradoNessaUnidadeConsumidora() throws Exception {
        assertThat(unidadeId)
                .as("O cenário deve cadastrar uma unidade consumidora antes do dispositivo")
                .isNotNull();

        DispositivoRequest dispositivo = new DispositivoRequest(
                unidadeId, "Ar condicionado", 1200.0, "LIGADO", 0.5, 30);

        dispositivoId = given()
                .baseUri("http://localhost")
                .port(porta)
                .header("Authorization", "Bearer " + autenticacaoSteps.obterToken())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(dispositivo))
        .when()
                .post("/api/dispositivos")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().jsonPath().getLong("id");
    }

    public Long obterDispositivoId() {
        assertThat(dispositivoId)
                .as("O cenário deve cadastrar um dispositivo antes de utilizá-lo")
                .isNotNull().isPositive();
        return dispositivoId;
    }

    @Dado("que eu tenha os seguintes dados do dispositivo:")
    public void queEuTenhaOsSeguintesDadosDoDispositivo(DataTable tabela) {
        Map<String, String> dados = tabela.subTable(1, 0).asMap(String.class, String.class);

        request = new DispositivoRequest(
                unidadeId,
                dados.get("nome"),
                Double.valueOf(dados.get("potenciaNominal")),
                dados.get("status"),
                Double.valueOf(dados.get("consumoMinimoAtivo")),
                Integer.valueOf(dados.get("tempoOciosidadeLimite"))
        );
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de dispositivos")
    public void euEnviarARequisicaoDeCadastro(String endpoint) throws Exception {
        Response resposta = given()
                .baseUri("http://localhost")
                .port(porta)
                .header("Authorization", "Bearer " + autenticacaoSteps.obterToken())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post(endpoint);

        respostaSteps.guardarResposta(resposta);
    }

    @Então("deve conter o identificador do dispositivo cadastrado")
    public void deveConterOIdentificadorDoDispositivoCadastrado() {
        Response resposta = respostaSteps.obterResposta();
        dispositivoId = resposta.then()
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .extract().jsonPath().getLong("id");

        assertThat(dispositivoId).isPositive();
        resposta.then()
                .header("Location", equalTo("http://localhost:" + porta + "/api/dispositivos/" + dispositivoId));
    }

    @Então("devo conseguir consultar o dispositivo com os dados informados")
    public void devoConseguirConsultarODispositivoComOsDadosInformados() {
        Response consulta = given()
                .baseUri("http://localhost")
                .port(porta)
                .config(config().jsonConfig(jsonConfig().numberReturnType(DOUBLE)))
                .header("Authorization", "Bearer " + autenticacaoSteps.obterToken())
        .when()
                .get("/api/dispositivos/{id}", dispositivoId)
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("nome", equalTo(request.nome()))
                .body("potenciaNominal", equalTo(request.potenciaNominal()))
                .body("status", equalTo(request.status()))
                .body("consumoMinimoAtivo", equalTo(request.consumoMinimoAtivo()))
                .body("tempoOciosidadeLimite", equalTo(request.tempoOciosidadeLimite()))
                .extract().response();

        assertThat(consulta.jsonPath().getLong("id")).isEqualTo(dispositivoId);
        assertThat(consulta.jsonPath().getLong("unidadeConsumidora.id")).isEqualTo(unidadeId);
    }
}
