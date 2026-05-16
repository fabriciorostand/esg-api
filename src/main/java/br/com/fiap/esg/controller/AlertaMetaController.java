package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.alerta_meta.dto.AlertaMetaRequest;
import br.com.fiap.esg.domain.alerta_meta.dto.AlertaMetaResponse;
import br.com.fiap.esg.domain.alerta_meta.AlertaMetaService;
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
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class AlertaMetaController {

    private final AlertaMetaService service;

    @PostMapping
    @Operation(description = "Endpoint responsavel por cadastrar um alerta de meta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alerta de meta cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Meta de consumo nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<AlertaMetaResponse> cadastrar(
            @RequestBody @Valid AlertaMetaRequest request,
            UriComponentsBuilder uriBuilder) {

        AlertaMetaResponse response = service.cadastrar(request);
        URI uri = uriBuilder.path("/api/alertas/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint responsavel por buscar um alerta de meta por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta de meta encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alerta de meta nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<AlertaMetaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
