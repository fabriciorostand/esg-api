package br.com.fiap.esg.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EsgApiIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Deve subir contexto com Oracle de teste e aplicar migracoes Flyway")
    void deveSubirContextoComOracleEFlyway() {
        assertThat(tabelaExiste("ESG_USUARIO")).isTrue();
        assertThat(tabelaExiste("ESG_CONSUMO_ENERGETICO")).isTrue();
        assertThat(tabelaExiste("FLYWAY_SCHEMA_HISTORY")).isTrue();
    }

    @Test
    @DisplayName("Deve registrar, logar e cadastrar fluxo energetico completo")
    void deveRegistrarLogarECadastrarFluxoEnergeticoCompleto() throws Exception {
        String token = autenticarUsuario();

        Long unidadeId = cadastrarUnidade(token);
        Long dispositivoId = cadastrarDispositivo(token, unidadeId);
        Long sensorId = cadastrarSensor(token, dispositivoId);
        Long consumoId = cadastrarConsumo(token, sensorId);

        mvc.perform(get("/api/consumosenergeticos/{id}", consumoId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(consumoId))
                .andExpect(jsonPath("$.kwhConsumido").value(12.35));

        mvc.perform(get("/api/consumosenergeticos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(consumoId))
                .andExpect(jsonPath("$.content[0].kwhConsumido").value(12.35));

        assertThat(contarRegistros("ESG_USUARIO")).isEqualTo(1);
        assertThat(contarRegistros("ESG_UNIDADE_CONSUMIDORA")).isEqualTo(1);
        assertThat(contarRegistros("ESG_DISPOSITIVO")).isEqualTo(1);
        assertThat(contarRegistros("ESG_SENSOR")).isEqualTo(1);
        assertThat(contarRegistros("ESG_CONSUMO_ENERGETICO")).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve bloquear endpoint protegido sem token")
    void deveBloquearEndpointProtegidoSemToken() throws Exception {
        mvc.perform(get("/api/consumosenergeticos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve aplicar validacao em request integrado")
    void deveAplicarValidacaoEmRequestIntegrado() throws Exception {
        String token = autenticarUsuario();

        mvc.perform(post("/api/unidadesconsumidoras")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "endereco": null,
                                  "nome": "",
                                  "tipo": "COMERCIAL",
                                  "areaTotal": 250.75
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    private String autenticarUsuario() throws Exception {
        mvc.perform(post("/api/autenticacao/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "integracao@fiap.com.br",
                                  "senha": "Senha123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("integracao@fiap.com.br"));

        MvcResult login = mvc.perform(post("/api/autenticacao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "integracao@fiap.com.br",
                                  "senha": "Senha123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        return login.getResponse()
                .getContentAsString()
                .replaceAll("^.*\"token\"\\s*:\\s*\"([^\"]+)\".*$", "$1");
    }

    private Long cadastrarUnidade(String token) throws Exception {
        MvcResult result = mvc.perform(post("/api/unidadesconsumidoras")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "endereco": {
                                    "bairro": "Centro",
                                    "rua": "Rua das Flores",
                                    "numero": "100",
                                    "cep": "01001000",
                                    "cidade": "Sao Paulo",
                                    "uf": "SP"
                                  },
                                  "nome": "Unidade Matriz",
                                  "tipo": "COMERCIAL",
                                  "areaTotal": 250.75
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        return extrairId(result);
    }

    private Long cadastrarDispositivo(String token, Long unidadeId) throws Exception {
        MvcResult result = mvc.perform(post("/api/dispositivos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idUnidadeConsumidora": %d,
                                  "nome": "Ar condicionado",
                                  "potenciaNominal": 1200.0,
                                  "status": "LIGADO",
                                  "consumoMinimoAtivo": 0.5,
                                  "tempoOciosidadeLimite": 30
                                }
                                """.formatted(unidadeId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        return extrairId(result);
    }

    private Long cadastrarSensor(String token, Long dispositivoId) throws Exception {
        MvcResult result = mvc.perform(post("/api/sensores")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idDispositivo": %d,
                                  "ativo": "S"
                                }
                                """.formatted(dispositivoId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        return extrairId(result);
    }

    private Long cadastrarConsumo(String token, Long sensorId) throws Exception {
        MvcResult result = mvc.perform(post("/api/consumosenergeticos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idSensor": %d,
                                  "kwhConsumido": 12.35,
                                  "dataMedicao": "2026-05-14"
                                }
                                """.formatted(sensorId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        return extrairId(result);
    }

    private Long extrairId(MvcResult result) throws Exception {
        return Long.valueOf(result.getResponse()
                .getContentAsString()
                .replaceAll("^.*\"id\"\\s*:\\s*(\\d+).*$", "$1"));
    }
}
