package br.com.fiap.esg.bdd.steps;

import io.cucumber.java.pt.Então;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RespostaSteps {

    private Response resposta;

    public void guardarResposta(Response resposta) {
        this.resposta = resposta;
    }

    public Response obterResposta() {
        assertThat(resposta)
                .as("O cenário deve enviar uma requisição antes de verificar a resposta")
                .isNotNull();
        return resposta;
    }

    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusCode) {
        obterResposta().then()
                .log().ifValidationFails()
                .statusCode(statusCode);
    }

    @Então("o corpo de resposta de erro da api deve retornar a mensagem {string}")
    public void oCorpoDeRespostaDeErroDaApiDeveRetornarAMensagem(String mensagem) {
        obterResposta().then()
                .log().ifValidationFails()
                .body(equalTo(mensagem));
    }

    @Então("a resposta deve conter um erro no campo {string} com a mensagem {string}")
    public void aRespostaDeveConterUmErroNoCampoComAMensagem(String campo, String mensagem) {
        List<Map<String, Object>> erros = obterResposta().then()
                .contentType(ContentType.JSON)
                .extract().jsonPath().getList("$");

        assertThat(erros).anySatisfy(erro -> {
            assertThat(erro.get("campo")).isEqualTo(campo);
            assertThat(erro.get("mensagem")).isEqualTo(mensagem);
        });
    }
}
