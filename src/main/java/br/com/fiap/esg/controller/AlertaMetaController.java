package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.alerta_meta.AlertaMetaService;
import br.com.fiap.esg.domain.alerta_meta.dto.AlertaMetaRequest;
import br.com.fiap.esg.domain.alerta_meta.dto.AlertaMetaResponse;
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

    @GetMapping
    @Operation(description = "Endpoint responsavel por listar alertas de meta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alertas de meta listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<AlertaMetaResponse>> listar(Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint responsavel por atualizar um alerta de meta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta de meta atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Alerta de meta ou meta de consumo nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<AlertaMetaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid AlertaMetaRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint responsavel por deletar um alerta de meta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alerta de meta deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alerta de meta nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
