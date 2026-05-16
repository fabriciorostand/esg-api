package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.sensor.dto.SensorRequest;
import br.com.fiap.esg.domain.sensor.dto.SensorResponse;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.domain.usuario.UsuarioRepository;
import br.com.fiap.esg.domain.sensor.SensorService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    private SensorRequest criarRequest() {
        return new SensorRequest(1L, "S");
    }

    private SensorResponse criarResponse() {
        return new SensorResponse(1L, null, "S");
    }
}
