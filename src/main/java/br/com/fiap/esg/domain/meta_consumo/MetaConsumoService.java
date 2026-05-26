package br.com.fiap.esg.domain.meta_consumo;

import br.com.fiap.esg.domain.dispositivo.Dispositivo;
import br.com.fiap.esg.domain.dispositivo.DispositivoRepository;
import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoRequest;
import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoResponse;
import br.com.fiap.esg.infra.exception.ValidacaoException;
import br.com.fiap.esg.mapper.MetaConsumoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MetaConsumoService {

    private final MetaConsumoRepository metaConsumoRepository;
    private final DispositivoRepository dispositivoRepository;
    private final MetaConsumoMapper mapper;

    @Transactional
    public MetaConsumoResponse cadastrar(MetaConsumoRequest request) {
        validarPeriodo(request);
        Dispositivo dispositivo = buscarDispositivo(request.idDispositivo());

        MetaConsumo metaConsumo = mapper.paraEntidade(request);
        metaConsumo.setDispositivo(dispositivo);

        return mapper.paraResponse(metaConsumoRepository.save(metaConsumo));
    }

    public MetaConsumoResponse buscarPorId(Long id) {
        return mapper.paraResponse(buscarMeta(id));
    }

    public Page<MetaConsumoResponse> listar(Pageable paginacao) {
        return metaConsumoRepository.findAll(paginacao)
                .map(mapper::paraResponse);
    }

    @Transactional
    public MetaConsumoResponse atualizar(Long id, MetaConsumoRequest request) {
        validarPeriodo(request);
        MetaConsumo metaConsumo = buscarMeta(id);
        Dispositivo dispositivo = buscarDispositivo(request.idDispositivo());

        mapper.atualizarEntidade(request, metaConsumo);
        metaConsumo.setDispositivo(dispositivo);

        return mapper.paraResponse(metaConsumo);
    }

    @Transactional
    public void deletar(Long id) {
        metaConsumoRepository.delete(buscarMeta(id));
    }

    private MetaConsumo buscarMeta(Long id) {
        return metaConsumoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meta de Consumo nao encontrada"));
    }

    private Dispositivo buscarDispositivo(Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo nao encontrado com o ID: " + id));
    }

    private void validarPeriodo(MetaConsumoRequest request) {
        if (request.dataInicio() != null && request.dataFim() != null && request.dataFim().isBefore(request.dataInicio())) {
            throw new ValidacaoException("A data de fim nao pode ser anterior a data de inicio.");
        }
    }
}
