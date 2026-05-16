package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.DispositivoRequest;
import br.com.fiap.esg.dto.DispositivoResponse;
import br.com.fiap.esg.service.DispositivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/dispositivos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class DispositivoController {

    private final DispositivoService service;

    @PostMapping
    @Operation(description = "Endpoint responsavel por cadastrar um dispositivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dispositivo cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Unidade consumidora nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<DispositivoResponse> cadastrar(
            @RequestBody @Valid DispositivoRequest request, 
            UriComponentsBuilder uriBuilder) {
        
        DispositivoResponse response = service.cadastrar(request);
        
        URI uri = uriBuilder.path("/api/dispositivos/{id}").buildAndExpand(response.id()).toUri();
        
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint responsavel por buscar um dispositivo por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dispositivo nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<DispositivoResponse> buscarPorId(@PathVariable Long id) {
        DispositivoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }
}