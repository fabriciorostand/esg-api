package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.meta_consumo.MetaConsumoService;
import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoRequest;
import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
            @ApiResponse(responseCode = "404", description = "Dispositivo nao encontrado"),
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

    @GetMapping
    @Operation(description = "Endpoint responsavel por listar metas de consumo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metas de consumo listadas com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<MetaConsumoResponse>> listar(Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint responsavel por atualizar uma meta de consumo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meta de consumo atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Meta de consumo ou dispositivo nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<MetaConsumoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid MetaConsumoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint responsavel por deletar uma meta de consumo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Meta de consumo deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Meta de consumo nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
