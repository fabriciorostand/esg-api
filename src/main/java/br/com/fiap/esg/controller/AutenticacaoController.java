package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.usuario.dto.LoginRequest;
import br.com.fiap.esg.domain.usuario.dto.RegistroRequest;
import br.com.fiap.esg.domain.usuario.dto.RegistroResponse;
import br.com.fiap.esg.infra.security.TokenJWTResponse;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.domain.usuario.Usuario;
import br.com.fiap.esg.domain.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    @Operation(description = "Endpoint responsavel por registrar um novo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<RegistroResponse> registrar(@RequestBody @Valid RegistroRequest request) {
        var usuario = usuarioService.registrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @PostMapping("/login")
    @Operation(description = "Endpoint responsavel por autenticar um usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario autenticado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "401", description = "Credenciais invalidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TokenJWTResponse> logar(@RequestBody @Valid LoginRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        TokenJWTResponse response = new TokenJWTResponse(tokenJWT);

        return ResponseEntity.ok(response);
    }

}
