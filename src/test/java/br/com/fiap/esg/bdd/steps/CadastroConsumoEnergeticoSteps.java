package br.com.fiap.esg.bdd.steps;

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

public class CadastroConsumoEnergeticoSteps {

    @LocalServerPort
    private int porta;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutenticacaoSteps autenticacaoSteps;

    @Autowired
    private CadastroSensorSteps sensorSteps;

    @Autowired
    private RespostaSteps respostaSteps;

    private ConsumoEnergeticoRequest request;

    @Dado("que eu tenha os seguintes dados do consumo energético:")
    public void queEuTenhaOsSeguintesDadosDoConsumoEnergetico(DataTable tabela) {
        Map<String, String> dados = tabela.subTable(1, 0).asMap(String.class, String.class);
        String valor = dados.get("kwhConsumido");
        BigDecimal consumo = valor == null || valor.isBlank() ? null : new BigDecimal(valor);
        request = new ConsumoEnergeticoRequest(
                sensorSteps.obterSensorId(),
                consumo,
                LocalDate.parse(dados.get("dataMedicao"))
        );
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de consumos energéticos")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeConsumosEnergeticos(String endpoint) {
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

    @Então("deve conter o identificador do consumo energético cadastrado")
    public void deveConterOIdentificadorDoConsumoEnergeticoCadastrado() {
        Response resposta = respostaSteps.obterResposta();
        Long consumoId = resposta.then()
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("kwhConsumido", comparesEqualTo(request.kwhConsumido()))
                .body("dataMedicao", equalTo(request.dataMedicao().toString()))
                .extract().jsonPath().getLong("id");

        assertThat(consumoId).isPositive();
        resposta.then()
                .header("Location", equalTo("http://localhost:" + porta + "/api/consumosenergeticos/" + consumoId));
    }

    @Então("o consumo energético deve estar vinculado ao sensor informado")
    public void oConsumoEnergeticoDeveEstarVinculadoAoSensorInformado() {
        Long sensorId = respostaSteps.obterResposta().then()
                .body("sensor.id", notNullValue())
                .extract().jsonPath().getLong("sensor.id");

        assertThat(sensorId).isEqualTo(request.idSensor());
    }
}
