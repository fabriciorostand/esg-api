package br.com.fiap.esg.service;

import br.com.fiap.esg.dto.AlertaMetaRequest;
import br.com.fiap.esg.dto.AlertaMetaResponse;
import br.com.fiap.esg.mapper.AlertaMetaMapper;
import br.com.fiap.esg.model.AlertaMeta;
import br.com.fiap.esg.model.MetaConsumo;
import br.com.fiap.esg.repository.AlertaMetaRepository;
import br.com.fiap.esg.repository.MetaConsumoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlertaMetaService {

    private final AlertaMetaRepository alertaMetaRepository;
    private final MetaConsumoRepository metaConsumoRepository;
    private final AlertaMetaMapper mapper;

    @Transactional
    public AlertaMetaResponse cadastrar(AlertaMetaRequest request) {
        MetaConsumo metaConsumo = metaConsumoRepository.findById(request.idMetaConsumo())
                .orElseThrow(() -> new EntityNotFoundException("Meta de Consumo não encontrada com o ID: " + request.idMetaConsumo()));

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