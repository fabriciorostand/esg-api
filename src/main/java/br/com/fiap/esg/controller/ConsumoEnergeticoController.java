package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.ConsumoEnergeticoRequest;
import br.com.fiap.esg.dto.ConsumoEnergeticoResponse;
import br.com.fiap.esg.dto.ConsumoEnergeticoUpdateRequest;
import br.com.fiap.esg.service.ConsumoEnergeticoService;
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
@RequestMapping("/api/consumosenergeticos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class ConsumoEnergeticoController {

    private final ConsumoEnergeticoService service;

    @PostMapping
    @Operation(description = "Endpoint responsavel por cadastrar um consumo energetico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consumo energetico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Sensor nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ConsumoEnergeticoResponse> cadastrar(@RequestBody @Valid ConsumoEnergeticoRequest request, UriComponentsBuilder uriBuilder) {
        ConsumoEnergeticoResponse consumoEnergetico = service.cadastrar(request);

        URI uri = uriBuilder.path("/api/consumosenergeticos/{id}").buildAndExpand(consumoEnergetico.id()).toUri();

        return ResponseEntity
                .created(uri)
                .body(consumoEnergetico);
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint responsavel por buscar um consumo energetico por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consumo energetico encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consumo energetico nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ConsumoEnergeticoResponse> buscarPorId(@PathVariable Long id) {
        ConsumoEnergeticoResponse consumoEnergetico = service.buscarPorId(id);

        return ResponseEntity.ok(consumoEnergetico);
    }

    @GetMapping
    @Operation(description = "Endpoint responsavel por listar consumos energeticos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consumos energeticos listados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<ConsumoEnergeticoResponse>> listar(Pageable paginacao) {
        Page<ConsumoEnergeticoResponse> consumosEnergeticos = service.listar(paginacao);

        return ResponseEntity.ok(consumosEnergeticos);
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint responsavel por atualizar um consumo energetico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consumo energetico atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de requisicao"),
            @ApiResponse(responseCode = "404", description = "Consumo energetico nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ConsumoEnergeticoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ConsumoEnergeticoUpdateRequest request) {
        ConsumoEnergeticoResponse consumoEnergetico = service.atualizar(id, request);

        return ResponseEntity.ok(consumoEnergetico);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint responsavel por deletar um consumo energetico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consumo energetico deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consumo energetico nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

}
