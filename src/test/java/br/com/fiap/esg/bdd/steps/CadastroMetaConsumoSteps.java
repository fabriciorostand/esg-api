package br.com.fiap.esg.bdd.steps;

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
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CadastroMetaConsumoSteps {

    @LocalServerPort
    private int porta;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutenticacaoSteps autenticacaoSteps;

    @Autowired
    private CadastroDispositivoSteps dispositivoSteps;

    @Autowired
    private RespostaSteps respostaSteps;

    private Map<String, Object> request;
    private Long metaId;

    @Dado("existe uma meta de consumo cadastrada para esse dispositivo:")
    public void existeUmaMetaDeConsumoCadastradaParaEsseDispositivo(DataTable tabela) {
        Map<String, Object> meta = criarRequest(tabela);
        metaId = cadastrarMeta("/api/metas", meta).then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().jsonPath().getLong("id");
    }

    public Long obterMetaId() {
        assertThat(metaId)
                .as("O cenário deve cadastrar uma meta de consumo antes de utilizá-la")
                .isNotNull().isPositive();
        return metaId;
    }

    @Dado("que eu tenha os seguintes dados da meta de consumo:")
    public void queEuTenhaOsSeguintesDadosDaMetaDeConsumo(DataTable tabela) {
        request = criarRequest(tabela);
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de metas de consumo")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeMetasDeConsumo(String endpoint) {
        respostaSteps.guardarResposta(cadastrarMeta(endpoint, request));
    }

    @Então("deve conter o identificador da meta de consumo cadastrada")
    public void deveConterOIdentificadorDaMetaDeConsumoCadastrada() {
        Response resposta = respostaSteps.obterResposta();
        metaId = resposta.then()
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("tipo", equalTo(request.get("tipo")))
                .body("metaKwh", comparesEqualTo((BigDecimal) request.get("metaKwh")))
                .body("dataInicio", equalTo(request.get("dataInicio").toString()))
                .body("dataFim", equalTo(request.get("dataFim").toString()))
                .extract().jsonPath().getLong("id");

        assertThat(metaId).isPositive();
        resposta.then()
                .header("Location", equalTo("http://localhost:" + porta + "/api/metas/" + metaId));
    }

    @Então("a meta de consumo deve estar vinculada ao dispositivo informado")
    public void aMetaDeConsumoDeveEstarVinculadaAoDispositivoInformado() {
        Long dispositivoId = respostaSteps.obterResposta().then()
                .body("dispositivo.id", notNullValue())
                .extract().jsonPath().getLong("dispositivo.id");

        assertThat(dispositivoId).isEqualTo(request.get("idDispositivo"));
    }

    private Map<String, Object> criarRequest(DataTable tabela) {
        Map<String, String> dados = tabela.subTable(1, 0).asMap(String.class, String.class);
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("idDispositivo", dispositivoSteps.obterDispositivoId());
        corpo.put("tipo", dados.get("tipo"));
        corpo.put("metaKwh", new BigDecimal(dados.get("metaKwh")));
        corpo.put("dataInicio", LocalDate.parse(dados.get("dataInicio")));
        corpo.put("dataFim", LocalDate.parse(dados.get("dataFim")));
        return corpo;
    }

    private Response cadastrarMeta(String endpoint, Map<String, Object> meta) {
        return given()
                .baseUri("http://localhost")
                .port(porta)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .header("Authorization", "Bearer " + autenticacaoSteps.obterToken())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(meta))
        .when()
                .post(endpoint);
    }
}
