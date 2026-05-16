package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.dispositivo.dto.DispositivoRequest;
import br.com.fiap.esg.domain.dispositivo.dto.DispositivoResponse;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.domain.usuario.UsuarioRepository;
import br.com.fiap.esg.domain.dispositivo.DispositivoService;
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

@WebMvcTest(DispositivoController.class)
@AutoConfigureJsonTesters
class DispositivoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DispositivoRequest> dispositivoRequestJson;

    @Autowired
    private JacksonTester<DispositivoResponse> dispositivoResponseJson;

    @MockitoBean
    private DispositivoService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/dispositivos").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        DispositivoRequest request = criarRequest();
        DispositivoResponse responseSimulada = criarResponse();

        when(service.cadastrar(any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                post("/api/dispositivos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dispositivoRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = dispositivoResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).endsWith("/api/dispositivos/1");
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando campo obrigatorio nao for informado")
    @WithMockUser
    void cadastrarCenario3() throws Exception {
        DispositivoRequest request = new DispositivoRequest(null, "Ar Condicionado", 3500.0, "ATIVO", 150.0, 30);

        MockHttpServletResponse response = mvc.perform(
                post("/api/dispositivos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dispositivoRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao buscar por id")
    @WithMockUser
    void buscarPorIdCenario1() throws Exception {
        DispositivoResponse responseSimulada = criarResponse();

        when(service.buscarPorId(1L)).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(get("/api/dispositivos/1"))
                .andReturn().getResponse();

        String jsonEsperado = dispositivoResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao buscar dispositivo inexistente")
    @WithMockUser
    void buscarPorIdCenario2() throws Exception {
        when(service.buscarPorId(1L)).thenThrow(EntityNotFoundException.class);

        MockHttpServletResponse response = mvc.perform(get("/api/dispositivos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private DispositivoRequest criarRequest() {
        return new DispositivoRequest(1L, "Ar Condicionado", 3500.0, "ATIVO", 150.0, 30);
    }

    private DispositivoResponse criarResponse() {
        return new DispositivoResponse(1L, null, "Ar Condicionado", 3500.0, "ATIVO", 150.0, 30);
    }
}
