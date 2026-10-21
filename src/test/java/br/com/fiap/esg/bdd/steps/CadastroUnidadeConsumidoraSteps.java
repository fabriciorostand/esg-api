package br.com.fiap.esg.bdd.steps;

import br.com.fiap.esg.domain.unidade_consumidora.dto.EnderecoRequest;
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

public class CadastroUnidadeConsumidoraSteps {

    @LocalServerPort
    private int porta;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutenticacaoSteps autenticacaoSteps;

    @Autowired
    private RespostaSteps respostaSteps;

    private String nome;
    private String tipo;
    private BigDecimal areaTotal;
    private EnderecoRequest endereco;

    @Dado("que eu tenha os seguintes dados da unidade consumidora:")
    public void queEuTenhaOsSeguintesDadosDaUnidadeConsumidora(DataTable tabela) {
        Map<String, String> dados = tabela.subTable(1, 0).asMap(String.class, String.class);
        nome = dados.get("nome");
        tipo = dados.get("tipo");
        areaTotal = new BigDecimal(dados.get("areaTotal"));
    }

    @Dado("que eu tenha os seguintes dados de endereço:")
    public void queEuTenhaOsSeguintesDadosDeEndereco(DataTable tabela) {
        Map<String, String> dados = tabela.subTable(1, 0).asMap(String.class, String.class);
        endereco = new EnderecoRequest(
                dados.get("bairro"),
                dados.get("rua"),
                dados.get("numero"),
                dados.get("cep"),
                dados.get("cidade"),
                dados.get("uf")
        );
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de unidades consumidoras")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeUnidadeConsumidoras(String endpoint) throws Exception {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("endereco", endereco);
        request.put("nome", nome);
        request.put("tipo", tipo);
        request.put("areaTotal", areaTotal);

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

    @Então("deve conter o identificador da unidade consumidora")
    public void deveConterOIdentificadorDaUnidadeConsumidora() {
        Response resposta = respostaSteps.obterResposta();
        Long unidadeId = resposta.then()
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("nome", equalTo(nome))
                .body("tipo", equalTo(tipo))
                .body("areaTotal", comparesEqualTo(areaTotal))
                .extract().jsonPath().getLong("id");

        assertThat(unidadeId).isPositive();
        resposta.then()
                .header("Location", equalTo("http://localhost:" + porta + "/api/unidadesconsumidoras/" + unidadeId));
    }

    @Então("o endereço retornado deve corresponder aos dados informados")
    public void oEnderecoRetornadoDeveCorresponderAosDadosInformados() {
        Response resposta = respostaSteps.obterResposta();
        Long enderecoId = resposta.then()
                .body("endereco.id", notNullValue())
                .body("endereco.bairro", equalTo(endereco.bairro()))
                .body("endereco.rua", equalTo(endereco.rua()))
                .body("endereco.numero", equalTo(endereco.numero()))
                .body("endereco.cep", equalTo(endereco.cep()))
                .body("endereco.cidade", equalTo(endereco.cidade()))
                .body("endereco.uf", equalTo(endereco.uf()))
                .extract().jsonPath().getLong("endereco.id");

        assertThat(enderecoId).isPositive();
    }
}
