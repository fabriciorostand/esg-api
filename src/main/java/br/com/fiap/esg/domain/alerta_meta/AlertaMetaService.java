package br.com.fiap.esg.domain.alerta_meta;

import br.com.fiap.esg.domain.alerta_meta.dto.AlertaMetaRequest;
import br.com.fiap.esg.domain.alerta_meta.dto.AlertaMetaResponse;
import br.com.fiap.esg.domain.consumo_energetico.ConsumoEnergeticoRepository;
import br.com.fiap.esg.domain.meta_consumo.MetaConsumo;
import br.com.fiap.esg.domain.meta_consumo.MetaConsumoRepository;
import br.com.fiap.esg.infra.exception.ValidacaoException;
import br.com.fiap.esg.mapper.AlertaMetaMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        MetaConsumo metaConsumo = buscarMeta(request.idMetaConsumo());
        validarConsumoDaMeta(metaConsumo);

        AlertaMeta alertaMeta = mapper.paraEntidade(request);
        alertaMeta.setMetaConsumo(metaConsumo);

        return mapper.paraResponse(alertaMetaRepository.save(alertaMeta));
    }

    public AlertaMetaResponse buscarPorId(Long id) {
        return mapper.paraResponse(buscarAlerta(id));
    }

    public Page<AlertaMetaResponse> listar(Pageable paginacao) {
        return alertaMetaRepository.findAll(paginacao)
                .map(mapper::paraResponse);
    }

    @Transactional
    public AlertaMetaResponse atualizar(Long id, AlertaMetaRequest request) {
        AlertaMeta alertaMeta = buscarAlerta(id);
        MetaConsumo metaConsumo = buscarMeta(request.idMetaConsumo());

        mapper.atualizarEntidade(request, alertaMeta);
        alertaMeta.setMetaConsumo(metaConsumo);

        return mapper.paraResponse(alertaMeta);
    }

    @Transactional
    public void deletar(Long id) {
        alertaMetaRepository.delete(buscarAlerta(id));
    }

    private AlertaMeta buscarAlerta(Long id) {
        return alertaMetaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta de Meta nao encontrado"));
    }

    private MetaConsumo buscarMeta(Long id) {
        return metaConsumoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meta de Consumo nao encontrada com o ID: " + id));
    }

    private void validarConsumoDaMeta(MetaConsumo metaConsumo) {
        BigDecimal totalConsumo = consumoEnergeticoRepository.calcularConsumoTotalDispositivo(
                metaConsumo.getDispositivo().getId(),
                metaConsumo.getDataInicio(),
                metaConsumo.getDataFim()
        );

        if (totalConsumo == null) {
            totalConsumo = BigDecimal.ZERO;
        }

        if (totalConsumo.compareTo(metaConsumo.getMetaKwh()) < 0) {
            throw new ValidacaoException("O consumo atual (" + totalConsumo + " kWh) nao atingiu a meta de consumo (" + metaConsumo.getMetaKwh() + " kWh). Emissao de alerta barrada.");
        }
    }
}
