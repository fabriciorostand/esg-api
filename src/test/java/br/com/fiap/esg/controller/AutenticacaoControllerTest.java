package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.usuario.dto.LoginRequest;
import br.com.fiap.esg.domain.usuario.dto.RegistroRequest;
import br.com.fiap.esg.domain.usuario.dto.RegistroResponse;
import br.com.fiap.esg.infra.security.TokenJWTResponse;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.domain.usuario.Usuario;
import br.com.fiap.esg.domain.usuario.UsuarioRepository;
import br.com.fiap.esg.domain.usuario.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AutenticacaoController.class)
@AutoConfigureJsonTesters
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<RegistroRequest> registroRequestJson;

    @Autowired
    private JacksonTester<RegistroResponse> registroResponseJson;

    @Autowired
    private JacksonTester<LoginRequest> loginRequestJson;

    @Autowired
    private JacksonTester<TokenJWTResponse> tokenJWTResponseJson;

    @MockitoBean
    private AuthenticationManager manager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deveria devolver codigo http 201 ao registrar usuario com informacoes validas")
    @WithMockUser
    void registrarCenario1() throws Exception {
        RegistroRequest request = new RegistroRequest("usuario@email.com", "Senha123");
        RegistroResponse responseSimulada = new RegistroResponse(1L, "usuario@email.com");

        when(usuarioService.registrar(any())).thenReturn(responseSimulada);

        MockHttpServletResponse response = mvc.perform(
                post("/api/autenticacao/registro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registroRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = registroResponseJson.write(responseSimulada).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 ao registrar usuario com senha invalida")
    @WithMockUser
    void registrarCenario2() throws Exception {
        RegistroRequest request = new RegistroRequest("usuario@email.com", "senha");

        MockHttpServletResponse response = mvc.perform(
                post("/api/autenticacao/registro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registroRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao autenticar usuario com credenciais validas")
    @WithMockUser
    void loginCenario1() throws Exception {
        LoginRequest request = new LoginRequest("usuario@email.com", "Senha123");
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@email.com");
        usuario.setSenha("Senha123");
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);

        when(manager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(tokenService.gerarToken(usuario)).thenReturn("token-jwt");

        MockHttpServletResponse response = mvc.perform(
                post("/api/autenticacao/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        String jsonEsperado = tokenJWTResponseJson.write(new TokenJWTResponse("token-jwt")).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 ao autenticar usuario com email invalido")
    @WithMockUser
    void loginCenario2() throws Exception {
        LoginRequest request = new LoginRequest("email-invalido", "Senha123");

        MockHttpServletResponse response = mvc.perform(
                post("/api/autenticacao/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson.write(request).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
