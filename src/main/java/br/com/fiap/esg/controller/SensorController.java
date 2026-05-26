package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.sensor.SensorService;
import br.com.fiap.esg.domain.sensor.dto.SensorRequest;
import br.com.fiap.esg.domain.sensor.dto.SensorResponse;
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
@RequestMapping("/api/sensores")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class SensorController {

    private final SensorService service;

    @PostMapping
    @Operation(description = "Endpoint responsavel por cadastrar um sensor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Dispositivo nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<SensorResponse> cadastrar(
            @RequestBody @Valid SensorRequest request,
            UriComponentsBuilder uriBuilder) {

        SensorResponse response = service.cadastrar(request);
        URI uri = uriBuilder.path("/api/sensores/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint responsavel por buscar um sensor por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sensor nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<SensorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @Operation(description = "Endpoint responsavel por listar sensores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensores listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<SensorResponse>> listar(Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint responsavel por atualizar um sensor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Sensor ou dispositivo nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<SensorResponse> atualizar(@PathVariable Long id, @RequestBody @Valid SensorRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint responsavel por deletar um sensor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sensor nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
