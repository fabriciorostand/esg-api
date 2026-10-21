package br.com.fiap.esg.bdd.steps;

import com.networknt.schema.InputFormat;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.restassured.http.ContentType;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ContratoSteps {

    private final RespostaSteps respostaSteps;
    private JsonSchema contrato;
    private String arquivoContrato;

    public ContratoSteps(RespostaSteps respostaSteps) {
        this.respostaSteps = respostaSteps;
    }

    @Dado("que o arquivo de contrato esperado é o {string}")
    public void queOArquivoDeContratoEsperadoEO(String arquivo) throws IOException {
        ClassPathResource recurso = new ClassPathResource("schemas/" + arquivo);
        assertThat(recurso.exists())
                .as("O arquivo de contrato schemas/%s deve existir no classpath", arquivo)
                .isTrue();

        SchemaValidatorsConfig configuracao = SchemaValidatorsConfig.builder()
                .formatAssertionsEnabled(true)
                .build();

        try (InputStream conteudo = recurso.getInputStream()) {
            contrato = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                    .getSchema(conteudo, configuracao);
        }
        arquivoContrato = arquivo;
    }

    @Então("a resposta da requisição deve estar em conformidade com o contrato selecionado")
    public void aRespostaDaRequisicaoDeveEstarEmConformidadeComOContratoSelecionado() {
        assertThat(contrato)
                .as("O cenário deve selecionar um contrato antes de validar a resposta")
                .isNotNull();

        String resposta = respostaSteps.obterResposta().then()
                .contentType(ContentType.JSON)
                .extract().asString();

        Set<ValidationMessage> erros = contrato.validate(resposta, InputFormat.JSON);
        assertThat(erros)
                .as("A resposta deve estar em conformidade com o contrato %s", arquivoContrato)
                .isEmpty();
    }
}
