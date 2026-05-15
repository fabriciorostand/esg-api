package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.MetaConsumoRequest;
import br.com.fiap.esg.dto.MetaConsumoResponse;
import br.com.fiap.esg.service.MetaConsumoService;
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
@RequestMapping("/api/metas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class MetaConsumoController {

    private final MetaConsumoService service;

    @PostMapping
    @Operation(description = "Endpoint responsavel por cadastrar uma meta de consumo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Meta de consumo cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Unidade consumidora nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<MetaConsumoResponse> cadastrar(
            @RequestBody @Valid MetaConsumoRequest request,
            UriComponentsBuilder uriBuilder) {

        MetaConsumoResponse response = service.cadastrar(request);
        URI uri = uriBuilder.path("/api/metas/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint responsavel por buscar uma meta de consumo por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meta de consumo encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Meta de consumo nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<MetaConsumoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
