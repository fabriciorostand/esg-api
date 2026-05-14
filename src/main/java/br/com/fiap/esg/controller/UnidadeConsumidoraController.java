package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.UnidadeConsumidoraRequest;
import br.com.fiap.esg.dto.UnidadeConsumidoraResponse;
import br.com.fiap.esg.service.UnidadeConsumidoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/unidadesconsumidoras")
@RequiredArgsConstructor
public class UnidadeConsumidoraController {

    private final UnidadeConsumidoraService service;

    @PostMapping
    public ResponseEntity<UnidadeConsumidoraResponse> cadastrar(@RequestBody @Valid UnidadeConsumidoraRequest request, UriComponentsBuilder uriBuilder) {
        UnidadeConsumidoraResponse unidadeConsumidora = service.cadastrar(request);

        URI uri = uriBuilder.path("/api/unidadesconsumidoras/{id}").buildAndExpand(unidadeConsumidora.id()).toUri();

        return ResponseEntity
                .created(uri)
                .body(unidadeConsumidora);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeConsumidoraResponse> buscarPorId(@PathVariable Long id) {
        UnidadeConsumidoraResponse unidadeConsumidora = service.buscarPorId(id);

        return ResponseEntity.ok(unidadeConsumidora);
    }

    @GetMapping
    public ResponseEntity<Page<UnidadeConsumidoraResponse>> listar(Pageable paginacao) {
        Page<UnidadeConsumidoraResponse> unidadesConsumidoras = service.listar(paginacao);

        return ResponseEntity.ok(unidadesConsumidoras);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeConsumidoraResponse> atualizar(@PathVariable Long id, @RequestBody @Valid UnidadeConsumidoraRequest request) {
        UnidadeConsumidoraResponse unidadeConsumidora = service.atualizar(id, request);

        return ResponseEntity.ok(unidadeConsumidora);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

}