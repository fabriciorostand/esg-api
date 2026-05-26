package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.consumo_energetico.dto.ConsumoEnergeticoRequest;
import br.com.fiap.esg.domain.consumo_energetico.dto.ConsumoEnergeticoResponse;
import br.com.fiap.esg.domain.consumo_energetico.dto.ConsumoEnergeticoUpdateRequest;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.domain.usuario.UsuarioRepository;
import br.com.fiap.esg.domain.consumo_energetico.ConsumoEnergeticoService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
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

@WebMvcTest(ConsumoEnergeticoController.class)
@AutoConfigureJsonTesters
class ConsumoEnergeticoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ConsumoEnergeticoRequest> consumoEnergeticoRequestJson;

    @Autowired
    private JacksonTester<ConsumoEnergeticoUpdateRequest> consumoEnergeticoUpdateRequestJson;

    @Autowired
    private JacksonTester<ConsumoEnergeticoResponse> consumoEnergeticoResponseJson;

    @MockitoBean
    private ConsumoEnergeticoService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/consumosenergeticos").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        ConsumoEnergeticoRequest request = criarRequest();
        ConsumoEnergeticoResponse responseSimulada = criarResponse();

        when(service.cadastrar(any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                post("/api/consumosenergeticos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consumoEnergeticoRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = consumoEnergeticoResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).endsWith("/api/consumosenergeticos/1");
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando campo obrigatorio nao for informado")
    @WithMockUser
    void cadastrarCenario3() throws Exception {
        ConsumoEnergeticoRequest request = new ConsumoEnergeticoRequest(
                null,
                BigDecimal.valueOf(22.5),
                LocalDate.of(2026, 1, 15)
        );

        MockHttpServletResponse response = mvc.perform(
                post("/api/consumosenergeticos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consumoEnergeticoRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando consumo for zero ou negativo")
    @WithMockUser
    void cadastrarCenario4() throws Exception {
        ConsumoEnergeticoRequest request = new ConsumoEnergeticoRequest(
                1L,
                BigDecimal.ZERO,
                LocalDate.of(2026, 1, 15)
        );

        MockHttpServletResponse response = mvc.perform(
                post("/api/consumosenergeticos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consumoEnergeticoRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao buscar por id")
    @WithMockUser
    void buscarPorIdCenario1() throws Exception {
        ConsumoEnergeticoResponse responseSimulada = criarResponse();

        when(service.buscarPorId(1L)).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(get("/api/consumosenergeticos/1"))
                .andReturn().getResponse();

        String jsonEsperado = consumoEnergeticoResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao buscar consumo inexistente")
    @WithMockUser
    void buscarPorIdCenario2() throws Exception {
        when(service.buscarPorId(1L)).thenThrow(EntityNotFoundException.class);

        MockHttpServletResponse response = mvc.perform(get("/api/consumosenergeticos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao listar consumos energeticos")
    @WithMockUser
    void listarCenario1() throws Exception {
        when(service.listar(any())).thenReturn(new PageImpl<>(List.of(criarResponse()), PageRequest.of(0, 10), 1));

        MockHttpServletResponse response = mvc.perform(get("/api/consumosenergeticos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("22.5");
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao atualizar consumo energetico")
    @WithMockUser
    void atualizarCenario1() throws Exception {
        ConsumoEnergeticoUpdateRequest request = criarUpdateRequest();
        ConsumoEnergeticoResponse responseSimulada = criarResponse();

        when(service.atualizar(eq(1L), any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                put("/api/consumosenergeticos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consumoEnergeticoUpdateRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = consumoEnergeticoResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 ao atualizar com informacoes invalidas")
    @WithMockUser
    void atualizarCenario2() throws Exception {
        ConsumoEnergeticoUpdateRequest request = new ConsumoEnergeticoUpdateRequest(null, LocalDate.of(2026, 1, 15));

        MockHttpServletResponse response = mvc.perform(
                put("/api/consumosenergeticos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consumoEnergeticoUpdateRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 204 ao deletar consumo energetico")
    @WithMockUser
    void deletarCenario1() throws Exception {
        doNothing().when(service).deletar(1L);

        MockHttpServletResponse response = mvc.perform(delete("/api/consumosenergeticos/1").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isBlank();
        verify(service).deletar(1L);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao deletar consumo inexistente")
    @WithMockUser
    void deletarCenario2() throws Exception {
        doThrow(EntityNotFoundException.class).when(service).deletar(1L);

        MockHttpServletResponse response = mvc.perform(delete("/api/consumosenergeticos/1").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ConsumoEnergeticoRequest criarRequest() {
        return new ConsumoEnergeticoRequest(
                1L,
                BigDecimal.valueOf(22.5),
                LocalDate.of(2026, 1, 15)
        );
    }

    private ConsumoEnergeticoUpdateRequest criarUpdateRequest() {
        return new ConsumoEnergeticoUpdateRequest(
                BigDecimal.valueOf(22.5),
                LocalDate.of(2026, 1, 15)
        );
    }

    private ConsumoEnergeticoResponse criarResponse() {
        return new ConsumoEnergeticoResponse(
                1L,
                null,
                BigDecimal.valueOf(22.5),
                LocalDate.of(2026, 1, 15)
        );
    }
}
