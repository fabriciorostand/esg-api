package br.com.fiap.esg.dto;

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
