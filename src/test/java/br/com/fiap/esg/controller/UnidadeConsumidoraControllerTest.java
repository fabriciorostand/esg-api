package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.unidade_consumidora.dto.EnderecoRequest;
import br.com.fiap.esg.domain.unidade_consumidora.dto.EnderecoResponse;
import br.com.fiap.esg.domain.unidade_consumidora.dto.UnidadeConsumidoraRequest;
import br.com.fiap.esg.domain.unidade_consumidora.dto.UnidadeConsumidoraResponse;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.domain.usuario.UsuarioRepository;
import br.com.fiap.esg.domain.unidade_consumidora.UnidadeConsumidoraService;
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

@WebMvcTest(UnidadeConsumidoraController.class)
@AutoConfigureJsonTesters
class UnidadeConsumidoraControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<UnidadeConsumidoraRequest> unidadeConsumidoraRequestJson;

    @Autowired
    private JacksonTester<UnidadeConsumidoraResponse> unidadeConsumidoraResponseJson;

    @MockitoBean
    private UnidadeConsumidoraService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/unidadesconsumidoras").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        UnidadeConsumidoraRequest request = criarRequest();
        UnidadeConsumidoraResponse responseSimulada = criarResponse();

        when(service.cadastrar(any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                post("/api/unidadesconsumidoras")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unidadeConsumidoraRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = unidadeConsumidoraResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).endsWith("/api/unidadesconsumidoras/1");
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando a API receber um JSON invalido")
    @WithMockUser
    void cadastrarCenario3() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/api/unidadesconsumidoras")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando campo obrigatorio nao for informado")
    @WithMockUser
    void cadastrarCenario4() throws Exception {
        UnidadeConsumidoraRequest request = new UnidadeConsumidoraRequest(
                criarEnderecoRequest(),
                "",
                "COMERCIAL",
                BigDecimal.valueOf(250.75)
        );

        MockHttpServletResponse response = mvc.perform(
                post("/api/unidadesconsumidoras")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unidadeConsumidoraRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando endereco interno estiver invalido")
    @WithMockUser
    void cadastrarCenario5() throws Exception {
        UnidadeConsumidoraRequest request = new UnidadeConsumidoraRequest(
                new EnderecoRequest("", "Rua das Flores", "100", "01001000", "Sao Paulo", "SP"),
                "Unidade Matriz",
                "COMERCIAL",
                BigDecimal.valueOf(250.75)
        );

        MockHttpServletResponse response = mvc.perform(
                post("/api/unidadesconsumidoras")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unidadeConsumidoraRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando area total for zero ou negativa")
    @WithMockUser
    void cadastrarCenario6() throws Exception {
        UnidadeConsumidoraRequest request = new UnidadeConsumidoraRequest(
                criarEnderecoRequest(),
                "Unidade Matriz",
                "COMERCIAL",
                BigDecimal.ZERO
        );

        MockHttpServletResponse response = mvc.perform(
                post("/api/unidadesconsumidoras")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unidadeConsumidoraRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao buscar por id")
    @WithMockUser
    void buscarPorIdCenario1() throws Exception {
        UnidadeConsumidoraResponse responseSimulada = criarResponse();

        when(service.buscarPorId(1L)).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(get("/api/unidadesconsumidoras/1"))
                .andReturn().getResponse();

        String jsonEsperado = unidadeConsumidoraResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao buscar unidade inexistente")
    @WithMockUser
    void buscarPorIdCenario2() throws Exception {
        when(service.buscarPorId(1L)).thenThrow(EntityNotFoundException.class);

        MockHttpServletResponse response = mvc.perform(get("/api/unidadesconsumidoras/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao listar unidades consumidoras")
    @WithMockUser
    void listarCenario1() throws Exception {
        when(service.listar(any())).thenReturn(new PageImpl<>(List.of(criarResponse()), PageRequest.of(0, 10), 1));

        MockHttpServletResponse response = mvc.perform(get("/api/unidadesconsumidoras"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("Unidade Matriz");
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao atualizar unidade consumidora")
    @WithMockUser
    void atualizarCenario1() throws Exception {
        UnidadeConsumidoraRequest request = criarRequest();
        UnidadeConsumidoraResponse responseSimulada = criarResponse();

        when(service.atualizar(eq(1L), any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                put("/api/unidadesconsumidoras/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unidadeConsumidoraRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = unidadeConsumidoraResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 ao atualizar com informacoes invalidas")
    @WithMockUser
    void atualizarCenario2() throws Exception {
        UnidadeConsumidoraRequest request = new UnidadeConsumidoraRequest(
                null,
                "Unidade Matriz",
                "COMERCIAL",
                BigDecimal.valueOf(250.75)
        );

        MockHttpServletResponse response = mvc.perform(
                put("/api/unidadesconsumidoras/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unidadeConsumidoraRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 204 ao deletar unidade consumidora")
    @WithMockUser
    void deletarCenario1() throws Exception {
        doNothing().when(service).deletar(1L);

        MockHttpServletResponse response = mvc.perform(delete("/api/unidadesconsumidoras/1").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isBlank();
        verify(service).deletar(1L);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 ao deletar unidade inexistente")
    @WithMockUser
    void deletarCenario2() throws Exception {
        doThrow(EntityNotFoundException.class).when(service).deletar(1L);

        MockHttpServletResponse response = mvc.perform(delete("/api/unidadesconsumidoras/1").with(csrf()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private UnidadeConsumidoraRequest criarRequest() {
        return new UnidadeConsumidoraRequest(
                criarEnderecoRequest(),
                "Unidade Matriz",
                "COMERCIAL",
                BigDecimal.valueOf(250.75)
        );
    }

    private EnderecoRequest criarEnderecoRequest() {
        return new EnderecoRequest(
                "Centro",
                "Rua das Flores",
                "100",
                "01001000",
                "Sao Paulo",
                "SP"
        );
    }

    private UnidadeConsumidoraResponse criarResponse() {
        return new UnidadeConsumidoraResponse(
                1L,
                new EnderecoResponse(1L, "Centro", "Rua das Flores", "100", "01001000", "Sao Paulo", "SP"),
                "Unidade Matriz",
                "COMERCIAL",
                BigDecimal.valueOf(250.75)
        );
    }
}
