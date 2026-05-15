package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.DispositivoRequest;
import br.com.fiap.esg.dto.DispositivoResponse;
import br.com.fiap.esg.service.DispositivoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class DispositivoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private DispositivoService service;

    @Autowired
    private JacksonTester<DispositivoRequest> dispositivoRequestJson;

    @Autowired
    private JacksonTester<DispositivoResponse> dispositivoResponseJson;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações estão inválidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        var response = mvc.perform(post("/api/dispositivos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações estão válidas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        var request = new DispositivoRequest(1L, "Ar Condicionado", 3500.0, "ATIVO", 150.0, 30);
        var responseSimulada = new DispositivoResponse(1L, null, "Ar Condicionado", 3500.0, "ATIVO", 150.0, 30);

        when(service.cadastrar(any())).thenReturn(responseSimulada);

        var response = mvc.perform(
                post("/api/dispositivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dispositivoRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        var jsonEsperado = dispositivoResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}