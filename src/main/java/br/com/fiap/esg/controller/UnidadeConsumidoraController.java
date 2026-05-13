package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.UnidadeConsumidoraRequest;
import br.com.fiap.esg.dto.UnidadeConsumidoraResponse;
import br.com.fiap.esg.model.UnidadeConsumidora;
import br.com.fiap.esg.service.UnidadeConsumidoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}
