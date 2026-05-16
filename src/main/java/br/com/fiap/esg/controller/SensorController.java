package br.com.fiap.esg.controller;

import br.com.fiap.esg.domain.sensor.dto.SensorRequest;
import br.com.fiap.esg.domain.sensor.dto.SensorResponse;
import br.com.fiap.esg.domain.sensor.SensorService;
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
}
