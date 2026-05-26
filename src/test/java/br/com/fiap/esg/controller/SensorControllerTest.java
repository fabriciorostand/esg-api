package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.sensor.SensorService;
import br.com.fiap.esg.domain.sensor.dto.SensorRequest;
import br.com.fiap.esg.domain.sensor.dto.SensorResponse;
import br.com.fiap.esg.domain.usuario.UsuarioRepository;
import br.com.fiap.esg.infra.security.TokenService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(SensorController.class)
@AutoConfigureJsonTesters
class SensorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<SensorRequest> sensorRequestJson;

    @Autowired
    private JacksonTester<SensorResponse> sensorResponseJson;

    @MockitoBean
    private SensorService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/sensores").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        SensorRequest request = criarRequest();
        SensorResponse responseSimulada = criarResponse();

        when(service.cadastrar(any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                post("/api/sensores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sensorRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = sensorResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).endsWith("/api/sensores/1");
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando ativo tiver mais de um caractere")
    @WithMockUser
    void cadastrarCenario3() throws Exception {
        SensorRequest request = new SensorRequest(1L, "SIM");

        MockHttpServletResponse response = mvc.perform(
                post("/api/sensores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sensorRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando ativo for invalido")
    @WithMockUser
    void cadastrarCenario4() throws Exception {
        SensorRequest request = new SensorRequest(1L, "X");

        MockHttpServletResponse response = mvc.perform(
                post("/api/sensores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sensorRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao buscar por id")
    @WithMockUser
    void buscarPorIdCenario1() throws Exception {
        SensorResponse responseSimulada = criarResponse();

        when(service.buscarPorId(1L)).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(get("/api/sensores/1"))
                .andReturn().getResponse();

        String jsonEsperado = sensorResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao buscar sensor inexistente")
    @WithMockUser
    void buscarPorIdCenario2() throws Exception {
        when(service.buscarPorId(1L)).thenThrow(EntityNotFoundException.class);

        MockHttpServletResponse response = mvc.perform(get("/api/sensores/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao listar sensores")
    @WithMockUser
    void listarCenario1() throws Exception {
        when(service.listar(any())).thenReturn(new PageImpl<>(List.of(criarResponse()), PageRequest.of(0, 10), 1));

        MockHttpServletResponse response = mvc.perform(get("/api/sensores"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("S");
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao atualizar sensor")
    @WithMockUser
    void atualizarCenario1() throws Exception {
        SensorRequest request = criarRequest();
        SensorResponse responseSimulada = criarResponse();

        when(service.atualizar(eq(1L), any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                put("/api/sensores/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sensorRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = sensorResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 ao atualizar com informacoes invalidas")
    @WithMockUser
    void atualizarCenario2() throws Exception {
        SensorRequest request = new SensorRequest(1L, "X");

        MockHttpServletResponse response = mvc.perform(
                put("/api/sensores/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sensorRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 204 ao deletar sensor")
    @WithMockUser
    void deletarCenario1() throws Exception {
        doNothing().when(service).deletar(1L);

        MockHttpServletResponse response = mvc.perform(delete("/api/sensores/1").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isBlank();
        verify(service).deletar(1L);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao deletar sensor inexistente")
    @WithMockUser
    void deletarCenario2() throws Exception {
        doThrow(EntityNotFoundException.class).when(service).deletar(1L);

        MockHttpServletResponse response = mvc.perform(delete("/api/sensores/1").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private SensorRequest criarRequest() {
        return new SensorRequest(1L, "S");
    }

    private SensorResponse criarResponse() {
        return new SensorResponse(1L, null, "S");
    }
}
