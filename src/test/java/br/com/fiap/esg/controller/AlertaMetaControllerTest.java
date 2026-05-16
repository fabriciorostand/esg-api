package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.AlertaMetaRequest;
import br.com.fiap.esg.dto.AlertaMetaResponse;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.repository.UsuarioRepository;
import br.com.fiap.esg.service.AlertaMetaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AlertaMetaController.class)
@AutoConfigureJsonTesters
class AlertaMetaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<AlertaMetaRequest> alertaMetaRequestJson;

    @Autowired
    private JacksonTester<AlertaMetaResponse> alertaMetaResponseJson;

    @MockitoBean
    private AlertaMetaService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/alertas").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        AlertaMetaRequest request = criarRequest();
        AlertaMetaResponse responseSimulada = criarResponse();

        when(service.cadastrar(any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                post("/api/alertas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alertaMetaRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = alertaMetaResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).endsWith("/api/alertas/1");
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando campo obrigatorio nao for informado")
    @WithMockUser
    void cadastrarCenario3() throws Exception {
        AlertaMetaRequest request = new AlertaMetaRequest(null, BigDecimal.valueOf(450), LocalDate.of(2026, 1, 20));

        MockHttpServletResponse response = mvc.perform(
                post("/api/alertas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(alertaMetaRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao buscar por id")
    @WithMockUser
    void buscarPorIdCenario1() throws Exception {
        AlertaMetaResponse responseSimulada = criarResponse();

        when(service.buscarPorId(1L)).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(get("/api/alertas/1"))
                .andReturn().getResponse();

        String jsonEsperado = alertaMetaResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao buscar alerta inexistente")
    @WithMockUser
    void buscarPorIdCenario2() throws Exception {
        when(service.buscarPorId(1L)).thenThrow(EntityNotFoundException.class);

        MockHttpServletResponse response = mvc.perform(get("/api/alertas/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private AlertaMetaRequest criarRequest() {
        return new AlertaMetaRequest(1L, BigDecimal.valueOf(450), LocalDate.of(2026, 1, 20));
    }

    private AlertaMetaResponse criarResponse() {
        return new AlertaMetaResponse(1L, null, BigDecimal.valueOf(450), LocalDate.of(2026, 1, 20));
    }
}
