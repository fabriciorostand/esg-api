package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.SensorRequest;
import br.com.fiap.esg.dto.SensorResponse;
import br.com.fiap.esg.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/sensores")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService service;

    @PostMapping
    public ResponseEntity<SensorResponse> cadastrar(
            @RequestBody @Valid SensorRequest request, 
            UriComponentsBuilder uriBuilder) {
        
        SensorResponse response = service.cadastrar(request);
        URI uri = uriBuilder.path("/api/sensores/{id}").buildAndExpand(response.id()).toUri();
        
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
