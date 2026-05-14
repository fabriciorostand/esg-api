package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.MetaConsumoRequest;
import br.com.fiap.esg.dto.MetaConsumoResponse;
import br.com.fiap.esg.service.MetaConsumoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/metas")
@RequiredArgsConstructor
public class MetaConsumoController {

    private final MetaConsumoService service;

    @PostMapping
    public ResponseEntity<MetaConsumoResponse> cadastrar(
            @RequestBody @Valid MetaConsumoRequest request,
            UriComponentsBuilder uriBuilder) {

        MetaConsumoResponse response = service.cadastrar(request);
        URI uri = uriBuilder.path("/api/metas/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaConsumoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}