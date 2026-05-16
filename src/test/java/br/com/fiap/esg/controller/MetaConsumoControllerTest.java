package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.MetaConsumoRequest;
import br.com.fiap.esg.dto.MetaConsumoResponse;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.repository.UsuarioRepository;
import br.com.fiap.esg.service.MetaConsumoService;
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

@WebMvcTest(MetaConsumoController.class)
@AutoConfigureJsonTesters
class MetaConsumoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<MetaConsumoRequest> metaConsumoRequestJson;

    @Autowired
    private JacksonTester<MetaConsumoResponse> metaConsumoResponseJson;

    @MockitoBean
    private MetaConsumoService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/metas").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        MetaConsumoRequest request = criarRequest();
        MetaConsumoResponse responseSimulada = criarResponse();

        when(service.cadastrar(any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                post("/api/metas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(metaConsumoRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = metaConsumoResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).endsWith("/api/metas/1");
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando tipo ultrapassar tamanho maximo")
    @WithMockUser
    void cadastrarCenario3() throws Exception {
        MetaConsumoRequest request = new MetaConsumoRequest(
                1L,
                "MENSAL",
                BigDecimal.valueOf(500),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31)
        );

        MockHttpServletResponse response = mvc.perform(
                post("/api/metas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(metaConsumoRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao buscar por id")
    @WithMockUser
    void buscarPorIdCenario1() throws Exception {
        MetaConsumoResponse responseSimulada = criarResponse();

        when(service.buscarPorId(1L)).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(get("/api/metas/1"))
                .andReturn().getResponse();

        String jsonEsperado = metaConsumoResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao buscar meta inexistente")
    @WithMockUser
    void buscarPorIdCenario2() throws Exception {
        when(service.buscarPorId(1L)).thenThrow(EntityNotFoundException.class);

        MockHttpServletResponse response = mvc.perform(get("/api/metas/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private MetaConsumoRequest criarRequest() {
        return new MetaConsumoRequest(
                1L,
                "MTH",
                BigDecimal.valueOf(500),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31)
        );
    }

    private MetaConsumoResponse criarResponse() {
        return new MetaConsumoResponse(
                1L,
                null,
                "MTH",
                BigDecimal.valueOf(500),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31)
        );
    }
}
