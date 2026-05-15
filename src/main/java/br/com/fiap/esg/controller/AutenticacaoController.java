package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.LoginRequest;
import br.com.fiap.esg.dto.RegistroRequest;
import br.com.fiap.esg.dto.RegistroResponse;
import br.com.fiap.esg.infra.security.TokenJWTResponse;
import br.com.fiap.esg.infra.security.TokenService;
import br.com.fiap.esg.model.Usuario;
import br.com.fiap.esg.service.UsuarioService;
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
    public ResponseEntity<RegistroResponse> registrar(@RequestBody @Valid RegistroRequest request) {
        var usuario = usuarioService.registrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenJWTResponse> logar(@RequestBody @Valid LoginRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        TokenJWTResponse response = new TokenJWTResponse(tokenJWT);

        return ResponseEntity.ok(response);
    }

}