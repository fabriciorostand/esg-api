package br.com.fiap.esg.bdd.steps;

import br.com.fiap.esg.domain.sensor.dto.SensorRequest;
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
import static org.hamcrest.Matchers.notNullValue;

public class CadastroSensorSteps {

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

    private SensorRequest request;
    private Long sensorId;

    @Dado("existe um sensor ativo cadastrado nesse dispositivo")
    public void existeUmSensorAtivoCadastradoNesseDispositivo() {
        SensorRequest sensor = new SensorRequest(dispositivoSteps.obterDispositivoId(), "S");
        sensorId = cadastrarSensor("/api/sensores", sensor).then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().jsonPath().getLong("id");
    }

    public Long obterSensorId() {
        assertThat(sensorId)
                .as("O cenário deve cadastrar um sensor antes de utilizá-lo")
                .isNotNull().isPositive();
        return sensorId;
    }

    @Dado("que eu tenha os seguintes dados do sensor:")
    public void queEuTenhaOsSeguintesDadosDoSensor(DataTable tabela) {
        Map<String, String> dados = tabela.subTable(1, 0).asMap(String.class, String.class);
        request = new SensorRequest(dispositivoSteps.obterDispositivoId(), dados.get("ativo"));
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de sensores")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeSensores(String endpoint) {
        respostaSteps.guardarResposta(cadastrarSensor(endpoint, request));
    }

    @Então("deve conter o identificador do sensor cadastrado")
    public void deveConterOIdentificadorDoSensorCadastrado() {
        Response resposta = respostaSteps.obterResposta();
        sensorId = resposta.then()
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("ativo", equalTo(request.ativo()))
                .extract().jsonPath().getLong("id");

        assertThat(sensorId).isPositive();
        resposta.then()
                .header("Location", equalTo("http://localhost:" + porta + "/api/sensores/" + sensorId));
    }

    @Então("o sensor deve estar vinculado ao dispositivo informado")
    public void oSensorDeveEstarVinculadoAoDispositivoInformado() {
        Long dispositivoId = respostaSteps.obterResposta().then()
                .body("dispositivo.id", notNullValue())
                .extract().jsonPath().getLong("dispositivo.id");

        assertThat(dispositivoId).isEqualTo(request.idDispositivo());
    }

    private Response cadastrarSensor(String endpoint, SensorRequest sensor) {
        return given()
                .baseUri("http://localhost")
                .port(porta)
                .header("Authorization", "Bearer " + autenticacaoSteps.obterToken())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(sensor))
        .when()
                .post(endpoint);
    }
}
