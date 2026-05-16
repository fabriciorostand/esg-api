package br.com.fiap.esg.service;

import br.com.fiap.esg.dto.AlertaMetaRequest;
import br.com.fiap.esg.dto.AlertaMetaResponse;
import br.com.fiap.esg.infra.exception.ValidacaoException;
import br.com.fiap.esg.mapper.AlertaMetaMapper;
import br.com.fiap.esg.model.AlertaMeta;
import br.com.fiap.esg.model.MetaConsumo;
import br.com.fiap.esg.repository.AlertaMetaRepository;
import br.com.fiap.esg.repository.ConsumoEnergeticoRepository;
import br.com.fiap.esg.repository.MetaConsumoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AlertaMetaService {

    private final AlertaMetaRepository alertaMetaRepository;
    private final MetaConsumoRepository metaConsumoRepository;
    private final ConsumoEnergeticoRepository consumoEnergeticoRepository;
    private final AlertaMetaMapper mapper;

    @Transactional
    public AlertaMetaResponse cadastrar(AlertaMetaRequest request) {
        MetaConsumo metaConsumo = metaConsumoRepository.findById(request.idMetaConsumo())
                .orElseThrow(() -> new EntityNotFoundException("Meta de Consumo não encontrada com o ID: " + request.idMetaConsumo()));

        BigDecimal totalConsumo = consumoEnergeticoRepository.calcularConsumoTotalDispositivo(
                metaConsumo.getDispositivo().getId(),
                metaConsumo.getDataInicio(),
                metaConsumo.getDataFim()
        );

        if (totalConsumo == null) {
            totalConsumo = BigDecimal.ZERO;
        }

        if (totalConsumo.compareTo(metaConsumo.getMetaKwh()) < 0) {
            throw new ValidacaoException("O consumo atual (" + totalConsumo + " kWh) não atingiu a meta de consumo (" + metaConsumo.getMetaKwh() + " kWh). Emissão de alerta barrada.");
        }

        AlertaMeta alertaMeta = mapper.paraEntidade(request);
        alertaMeta.setMetaConsumo(metaConsumo);

        return mapper.paraResponse(alertaMetaRepository.save(alertaMeta));
    }

    public AlertaMetaResponse buscarPorId(Long id) {
        return mapper.paraResponse(
                alertaMetaRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Alerta de Meta não encontrado"))
        );
    }
}