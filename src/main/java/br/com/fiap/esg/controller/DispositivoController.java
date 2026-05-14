package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.DispositivoRequest;
import br.com.fiap.esg.dto.DispositivoResponse;
import br.com.fiap.esg.service.DispositivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/dispositivos")
@RequiredArgsConstructor
public class DispositivoController {

    private final DispositivoService service;

    @PostMapping
    public ResponseEntity<DispositivoResponse> cadastrar(
            @RequestBody @Valid DispositivoRequest request, 
            UriComponentsBuilder uriBuilder) {
        
        DispositivoResponse response = service.cadastrar(request);
        
        URI uri = uriBuilder.path("/api/dispositivos/{id}").buildAndExpand(response.id()).toUri();
        
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispositivoResponse> buscarPorId(@PathVariable Long id) {
        DispositivoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }
}
