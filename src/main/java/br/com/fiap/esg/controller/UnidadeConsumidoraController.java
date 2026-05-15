package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.UnidadeConsumidoraRequest;
import br.com.fiap.esg.dto.UnidadeConsumidoraResponse;
import br.com.fiap.esg.service.UnidadeConsumidoraService;
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
@RequestMapping("/api/unidadesconsumidoras")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class UnidadeConsumidoraController {

    private final UnidadeConsumidoraService service;

    @PostMapping
    @Operation(description = "Endpoint responsavel por cadastrar uma unidade consumidora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unidade consumidora cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<UnidadeConsumidoraResponse> cadastrar(@RequestBody @Valid UnidadeConsumidoraRequest request, UriComponentsBuilder uriBuilder) {
        UnidadeConsumidoraResponse unidadeConsumidora = service.cadastrar(request);

        URI uri = uriBuilder.path("/api/unidadesconsumidoras/{id}").buildAndExpand(unidadeConsumidora.id()).toUri();

        return ResponseEntity
                .created(uri)
                .body(unidadeConsumidora);
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint responsavel por buscar uma unidade consumidora por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidade consumidora encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Unidade consumidora nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<UnidadeConsumidoraResponse> buscarPorId(@PathVariable Long id) {
        UnidadeConsumidoraResponse unidadeConsumidora = service.buscarPorId(id);

        return ResponseEntity.ok(unidadeConsumidora);
    }

    @GetMapping
    @Operation(description = "Endpoint responsavel por listar unidades consumidoras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidades consumidoras listadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<UnidadeConsumidoraResponse>> listar(Pageable paginacao) {
        Page<UnidadeConsumidoraResponse> unidadesConsumidoras = service.listar(paginacao);

        return ResponseEntity.ok(unidadesConsumidoras);
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint responsavel por atualizar uma unidade consumidora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidade consumidora atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Unidade consumidora nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<UnidadeConsumidoraResponse> atualizar(@PathVariable Long id, @RequestBody @Valid UnidadeConsumidoraRequest request) {
        UnidadeConsumidoraResponse unidadeConsumidora = service.atualizar(id, request);

        return ResponseEntity.ok(unidadeConsumidora);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint responsavel por deletar uma unidade consumidora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unidade consumidora deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Unidade consumidora nao encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

}
