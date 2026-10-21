package br.com.fiap.esg.bdd.steps;

import br.com.fiap.esg.domain.alerta_meta.dto.AlertaMetaRequest;
import br.com.fiap.esg.domain.consumo_energetico.dto.ConsumoEnergeticoRequest;
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
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CadastroAlertaMetaSteps {

    @LocalServerPort
    private int porta;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutenticacaoSteps autenticacaoSteps;

    @Autowired
    private CadastroSensorSteps sensorSteps;

    @Autowired
    private CadastroMetaConsumoSteps metaSteps;

    @Autowired
    private RespostaSteps respostaSteps;

    private AlertaMetaRequest request;

    @Dado("que eu tenha os seguintes dados do alerta de meta:")
    public void queEuTenhaOsSeguintesDadosDoAlertaDeMeta(DataTable tabela) {
        Map<String, String> dados = tabela.subTable(1, 0).asMap(String.class, String.class);
        String valor = dados.get("valorAlertaMeta");
        BigDecimal valorAlerta = valor == null || valor.isBlank() ? null : new BigDecimal(valor);
        request = new AlertaMetaRequest(
                metaSteps.obterMetaId(),
                valorAlerta,
                LocalDate.parse(dados.get("dataAlertaMeta"))
        );
    }

    @Dado("que existem os seguintes consumos registrados para esse sensor:")
    public void queExistemOsSeguintesConsumosRegistradosParaEsseSensor(DataTable tabela) {
        Long sensorId = sensorSteps.obterSensorId();
        for (Map<String, String> dados : tabela.asMaps(String.class, String.class)) {
            ConsumoEnergeticoRequest consumo = new ConsumoEnergeticoRequest(
                    sensorId,
                    new BigDecimal(dados.get("kwhConsumido")),
                    LocalDate.parse(dados.get("dataMedicao"))
            );

            Long consumoId = given()
                    .baseUri("http://localhost")
                    .port(porta)
                    .header("Authorization", "Bearer " + autenticacaoSteps.obterToken())
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(consumo))
            .when()
                    .post("/api/consumosenergeticos")
            .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .extract().jsonPath().getLong("id");

            assertThat(consumoId).isPositive();
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de alertas de meta")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeAlertasDeMeta(String endpoint) {
        Response resposta = given()
                .baseUri("http://localhost")
                .port(porta)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .header("Authorization", "Bearer " + autenticacaoSteps.obterToken())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post(endpoint);

        respostaSteps.guardarResposta(resposta);
    }

    @Então("deve conter o identificador do alerta de meta cadastrado")
    public void deveConterOIdentificadorDoAlertaDeMetaCadastrado() {
        Response resposta = respostaSteps.obterResposta();
        Long alertaId = resposta.then()
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("valorAlertaMeta", comparesEqualTo(request.valorAlertaMeta()))
                .body("dataAlertaMeta", equalTo(request.dataAlertaMeta().toString()))
                .extract().jsonPath().getLong("id");

        assertThat(alertaId).isPositive();
        resposta.then()
                .header("Location", equalTo("http://localhost:" + porta + "/api/alertas/" + alertaId));
    }

    @Então("o alerta deve estar vinculado à meta de consumo informada")
    public void oAlertaDeveEstarVinculadoAMetaDeConsumoInformada() {
        Long metaId = respostaSteps.obterResposta().then()
                .body("metaConsumo.id", notNullValue())
                .extract().jsonPath().getLong("metaConsumo.id");

        assertThat(metaId).isEqualTo(request.idMetaConsumo());
    }
}
