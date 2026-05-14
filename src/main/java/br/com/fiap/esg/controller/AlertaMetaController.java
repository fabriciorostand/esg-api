package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.AlertaMetaRequest;
import br.com.fiap.esg.dto.AlertaMetaResponse;
import br.com.fiap.esg.service.AlertaMetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaMetaController {

    private final AlertaMetaService service;

    @PostMapping
    public ResponseEntity<AlertaMetaResponse> cadastrar(
            @RequestBody @Valid AlertaMetaRequest request,
            UriComponentsBuilder uriBuilder) {

        AlertaMetaResponse response = service.cadastrar(request);
        URI uri = uriBuilder.path("/api/alertas/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaMetaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}