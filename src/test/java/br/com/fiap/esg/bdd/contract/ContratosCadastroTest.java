package br.com.fiap.esg.bdd.contract;

import br.com.fiap.esg.bdd.steps.ContratoSteps;
import br.com.fiap.esg.bdd.steps.RespostaSteps;
import io.restassured.builder.ResponseBuilder;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContratosCadastroTest {

    private final RespostaSteps respostaSteps = new RespostaSteps();
    private final ContratoSteps contratoSteps = new ContratoSteps(respostaSteps);

    @ParameterizedTest(name = "{0}: resposta válida")
    @MethodSource("contratos")
    void deveAceitarRespostaValida(String arquivo, String resposta, String caminhoEndereco) throws IOException {
        selecionarContratoEGuardarResposta(arquivo, resposta);

        assertThatCode(contratoSteps::aRespostaDaRequisicaoDeveEstarEmConformidadeComOContratoSelecionado)
                .doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "{0}: campo obrigatório ausente")
    @MethodSource("contratos")
    void deveRejeitarIdAusente(String arquivo, String resposta, String caminhoEndereco) throws IOException {
        JSONObject corpo = new JSONObject(resposta);
        corpo.remove("id");

        verificarRejeicao(arquivo, corpo);
    }

    @ParameterizedTest(name = "{0}: tipo incorreto no endereço aninhado")
    @MethodSource("contratos")
    void deveRejeitarTipoIncorretoNoEndereco(String arquivo, String resposta, String caminhoEndereco) throws IOException {
        JSONObject corpo = new JSONObject(resposta);
        obterEndereco(corpo, caminhoEndereco).put("id", "101");

        verificarRejeicao(arquivo, corpo);
    }

    @ParameterizedTest(name = "{0}: campo extra no endereço aninhado")
    @MethodSource("contratos")
    void deveRejeitarCampoExtraNoEndereco(String arquivo, String resposta, String caminhoEndereco) throws IOException {
        JSONObject corpo = new JSONObject(resposta);
        obterEndereco(corpo, caminhoEndereco).put("campoInesperado", true);

        verificarRejeicao(arquivo, corpo);
    }

    @ParameterizedTest(name = "{0}: valor inválido em {2}")
    @MethodSource("valoresInvalidos")
    void deveRejeitarEnumOuDataInvalida(String arquivo, String resposta, String campo, String valor) throws IOException {
        JSONObject corpo = new JSONObject(resposta).put(campo, valor);

        verificarRejeicao(arquivo, corpo);
    }

    private void verificarRejeicao(String arquivo, JSONObject resposta) throws IOException {
        selecionarContratoEGuardarResposta(arquivo, resposta.toString());

        assertThatThrownBy(contratoSteps::aRespostaDaRequisicaoDeveEstarEmConformidadeComOContratoSelecionado)
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining(arquivo);
    }

    private void selecionarContratoEGuardarResposta(String arquivo, String resposta) throws IOException {
        contratoSteps.queOArquivoDeContratoEsperadoEO(arquivo);
        respostaSteps.guardarResposta(new ResponseBuilder()
                .setStatusCode(201)
                .setContentType(ContentType.JSON)
                .setBody(resposta)
                .build());
    }

    private JSONObject obterEndereco(JSONObject corpo, String caminho) {
        JSONObject endereco = corpo;
        for (String campo : caminho.split("\\.")) {
            endereco = endereco.getJSONObject(campo);
        }
        return endereco;
    }

    private static Stream<Arguments> contratos() {
        return Stream.of(
                Arguments.of(arquivo("unidade-consumidora"), unidade().toString(), "endereco"),
                Arguments.of(arquivo("dispositivo"), dispositivo().toString(), "unidadeConsumidora.endereco"),
                Arguments.of(arquivo("sensor"), sensor().toString(), "dispositivo.unidadeConsumidora.endereco"),
                Arguments.of(arquivo("meta-consumo"), meta().toString(), "dispositivo.unidadeConsumidora.endereco"),
                Arguments.of(arquivo("consumo-energetico"), consumo().toString(), "sensor.dispositivo.unidadeConsumidora.endereco"),
                Arguments.of(arquivo("alerta-meta"), alerta().toString(), "metaConsumo.dispositivo.unidadeConsumidora.endereco")
        );
    }

    private static Stream<Arguments> valoresInvalidos() {
        return Stream.of(
                Arguments.of(arquivo("unidade-consumidora"), unidade().toString(), "tipo", "ABC"),
                Arguments.of(arquivo("dispositivo"), dispositivo().toString(), "status", "ABC"),
                Arguments.of(arquivo("sensor"), sensor().toString(), "ativo", "X"),
                Arguments.of(arquivo("meta-consumo"), meta().toString(), "tipo", "ABC"),
                Arguments.of(arquivo("meta-consumo"), meta().toString(), "dataInicio", "2026-02-30"),
                Arguments.of(arquivo("consumo-energetico"), consumo().toString(), "dataMedicao", "20/10/2026"),
                Arguments.of(arquivo("alerta-meta"), alerta().toString(), "dataAlertaMeta", "invalida")
        );
    }

    private static String arquivo(String dominio) {
        return "cadastro-bem-sucedido-de-" + dominio + ".json";
    }

    private static JSONObject unidade() {
        JSONObject endereco = new JSONObject("""
                {"id":101,"bairro":"Centro","rua":"Rua das Flores","numero":"100",
                 "cep":"01001000","cidade":"Sao Paulo","uf":"SP"}
                """);
        return new JSONObject("""
                {"id":201,"nome":"Unidade Matriz","tipo":"COMERCIAL","areaTotal":250.75}
                """).put("endereco", endereco);
    }

    private static JSONObject dispositivo() {
        return new JSONObject("""
                {"id":301,"nome":"Ar condicionado","potenciaNominal":1200.0,"status":"LIGADO",
                 "consumoMinimoAtivo":0.5,"tempoOciosidadeLimite":30}
                """).put("unidadeConsumidora", unidade());
    }

    private static JSONObject sensor() {
        return new JSONObject().put("id", 401).put("ativo", "S").put("dispositivo", dispositivo());
    }

    private static JSONObject meta() {
        return new JSONObject("""
                {"id":501,"tipo":"MES","metaKwh":100.50,"dataInicio":"2026-10-01","dataFim":"2026-10-31"}
                """).put("dispositivo", dispositivo());
    }

    private static JSONObject consumo() {
        return new JSONObject("""
                {"id":601,"kwhConsumido":15.50,"dataMedicao":"2026-10-20"}
                """).put("sensor", sensor());
    }

    private static JSONObject alerta() {
        return new JSONObject("""
                {"id":701,"valorAlertaMeta":100.00,"dataAlertaMeta":"2026-10-20"}
                """).put("metaConsumo", meta());
    }
}
