package br.com.fiap.esg.controller;

import br.com.fiap.esg.dto.ConsumoEnergeticoRequest;
import br.com.fiap.esg.dto.ConsumoEnergeticoResponse;
import br.com.fiap.esg.dto.ConsumoEnergeticoUpdateRequest;
import br.com.fiap.esg.service.ConsumoEnergeticoService;
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
public class ConsumoEnergeticoController {

    private final ConsumoEnergeticoService service;

    @PostMapping
    public ResponseEntity<ConsumoEnergeticoResponse> cadastrar(@RequestBody @Valid ConsumoEnergeticoRequest request, UriComponentsBuilder uriBuilder) {
        ConsumoEnergeticoResponse consumoEnergetico = service.cadastrar(request);

        URI uri = uriBuilder.path("/api/consumosenergeticos/{id}").buildAndExpand(consumoEnergetico.id()).toUri();

        return ResponseEntity
                .created(uri)
                .body(consumoEnergetico);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumoEnergeticoResponse> buscarPorId(@PathVariable Long id) {
        ConsumoEnergeticoResponse consumoEnergetico = service.buscarPorId(id);

        return ResponseEntity.ok(consumoEnergetico);
    }

    @GetMapping
    public ResponseEntity<Page<ConsumoEnergeticoResponse>> listar(Pageable paginacao) {
        Page<ConsumoEnergeticoResponse> consumosEnergeticos = service.listar(paginacao);

        return ResponseEntity.ok(consumosEnergeticos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsumoEnergeticoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ConsumoEnergeticoUpdateRequest request) {
        ConsumoEnergeticoResponse consumoEnergetico = service.atualizar(id, request);

        return ResponseEntity.ok(consumoEnergetico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

}
