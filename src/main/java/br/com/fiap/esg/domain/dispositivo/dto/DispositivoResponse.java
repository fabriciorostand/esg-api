package br.com.fiap.esg.domain.dispositivo.dto;

import br.com.fiap.esg.domain.unidade_consumidora.dto.UnidadeConsumidoraResponse;

public record DispositivoResponse(
        Long id,
        UnidadeConsumidoraResponse unidadeConsumidora,
        String nome,
        Double potenciaNominal,
        String status,
        Double consumoMinimoAtivo,
        Integer tempoOciosidadeLimite
) {
}
