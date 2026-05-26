package br.com.fiap.esg.domain.dispositivo;

import br.com.fiap.esg.domain.dispositivo.dto.DispositivoRequest;
import br.com.fiap.esg.domain.dispositivo.dto.DispositivoResponse;
import br.com.fiap.esg.domain.unidade_consumidora.UnidadeConsumidora;
import br.com.fiap.esg.domain.unidade_consumidora.UnidadeConsumidoraRepository;
import br.com.fiap.esg.mapper.DispositivoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;
    private final UnidadeConsumidoraRepository unidadeConsumidoraRepository;
    private final DispositivoMapper mapper;

    @Transactional
    public DispositivoResponse cadastrar(DispositivoRequest request) {
        UnidadeConsumidora unidade = buscarUnidade(request.idUnidadeConsumidora());

        Dispositivo dispositivo = mapper.paraEntidade(request);
        dispositivo.setUnidadeConsumidora(unidade);

        return mapper.paraResponse(dispositivoRepository.save(dispositivo));
    }

    public DispositivoResponse buscarPorId(Long id) {
        return mapper.paraResponse(buscarDispositivo(id));
    }

    public Page<DispositivoResponse> listar(Pageable paginacao) {
        return dispositivoRepository.findAll(paginacao)
                .map(mapper::paraResponse);
    }

    @Transactional
    public DispositivoResponse atualizar(Long id, DispositivoRequest request) {
        Dispositivo dispositivo = buscarDispositivo(id);
        UnidadeConsumidora unidade = buscarUnidade(request.idUnidadeConsumidora());

        mapper.atualizarEntidade(request, dispositivo);
        dispositivo.setUnidadeConsumidora(unidade);

        return mapper.paraResponse(dispositivo);
    }

    @Transactional
    public void deletar(Long id) {
        dispositivoRepository.delete(buscarDispositivo(id));
    }

    private Dispositivo buscarDispositivo(Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo nao encontrado"));
    }

    private UnidadeConsumidora buscarUnidade(Long id) {
        return unidadeConsumidoraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade Consumidora nao encontrada com o ID: " + id));
    }
}
