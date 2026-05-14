package br.com.fiap.esg.service;

import br.com.fiap.esg.dto.DispositivoRequest;
import br.com.fiap.esg.dto.DispositivoResponse;
import br.com.fiap.esg.mapper.DispositivoMapper;
import br.com.fiap.esg.model.Dispositivo;
import br.com.fiap.esg.model.UnidadeConsumidora;
import br.com.fiap.esg.repository.DispositivoRepository;
import br.com.fiap.esg.repository.UnidadeConsumidoraRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
        UnidadeConsumidora unidade = unidadeConsumidoraRepository.findById(request.idUnidadeConsumidora())
                .orElseThrow(() -> new EntityNotFoundException("Unidade Consumidora não encontrada com o ID: " + request.idUnidadeConsumidora()));

        Dispositivo dispositivo = mapper.paraEntidade(request);
        
        dispositivo.setUnidadeConsumidora(unidade);

        return mapper.paraResponse(dispositivoRepository.save(dispositivo));
    }

    public DispositivoResponse buscarPorId(Long id) {
        return mapper.paraResponse(
                dispositivoRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Dispositivo não encontrado"))
        );
    }
}
