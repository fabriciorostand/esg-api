package br.com.fiap.esg.bdd.contract;

import io.restassured.builder.ResponseBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import br.com.fiap.esg.bdd.steps.ContratoSteps;
import br.com.fiap.esg.bdd.steps.RespostaSteps;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContratoStepsTest {

    private final RespostaSteps respostaSteps = new RespostaSteps();
    private final ContratoSteps contratoSteps = new ContratoSteps(respostaSteps);

    @BeforeEach
    void selecionarContrato() throws IOException {
        contratoSteps.queOArquivoDeContratoEsperadoEO("cadastro-bem-sucedido-de-usuario.json");
    }

    @Test
    void deveAceitarRespostaValidaSemFixarIdOuEmail() {
        guardarResposta("""
                {"id": 42, "email": "outro@example.com"}
                """);

        assertThatCode(contratoSteps::aRespostaDaRequisicaoDeveEstarEmConformidadeComOContratoSelecionado)
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"email\":\"usuario@example.com\"}",
            "{\"id\":1}",
            "{\"id\":0,\"email\":\"usuario@example.com\"}",
            "{\"id\":\"1\",\"email\":\"usuario@example.com\"}",
            "{\"id\":1,\"email\":\"invalido\"}",
            "{\"id\":1,\"email\":\"usuario@example.com\",\"senha\":null}"
    })
    void deveRejeitarRespostaForaDoContrato(String resposta) {
        guardarResposta(resposta);

        assertThatThrownBy(contratoSteps::aRespostaDaRequisicaoDeveEstarEmConformidadeComOContratoSelecionado)
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("cadastro-bem-sucedido-de-usuario.json");
    }

    private void guardarResposta(String corpo) {
        respostaSteps.guardarResposta(new ResponseBuilder()
                .setStatusCode(201)
                .setContentType(ContentType.JSON)
                .setBody(corpo)
                .build());
    }
}
