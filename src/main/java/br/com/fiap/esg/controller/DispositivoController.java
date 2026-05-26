package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.dispositivo.DispositivoService;
import br.com.fiap.esg.domain.dispositivo.dto.DispositivoRequest;
import br.com.fiap.esg.domain.dispositivo.dto.DispositivoResponse;
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
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @Operation(description = "Endpoint responsavel por listar dispositivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivos listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<DispositivoResponse>> listar(Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint responsavel por atualizar um dispositivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Dispositivo ou unidade consumidora nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<DispositivoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid DispositivoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint responsavel por deletar um dispositivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dispositivo deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dispositivo nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
