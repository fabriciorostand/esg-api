package br.com.fiap.esg.service;

import br.com.fiap.esg.dto.ConsumoEnergeticoRequest;
import br.com.fiap.esg.dto.ConsumoEnergeticoResponse;
import br.com.fiap.esg.dto.ConsumoEnergeticoUpdateRequest;
import br.com.fiap.esg.mapper.ConsumoEnergeticoMapper;
import br.com.fiap.esg.model.ConsumoEnergetico;
import br.com.fiap.esg.model.Sensor;
import br.com.fiap.esg.repository.ConsumoEnergeticoRepository;
import br.com.fiap.esg.repository.SensorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsumoEnergeticoService {

    private final ConsumoEnergeticoRepository consumoEnergeticoRepository;
    private final SensorRepository sensorRepository;
    private final ConsumoEnergeticoMapper mapper;

    @Transactional
    public ConsumoEnergeticoResponse cadastrar(ConsumoEnergeticoRequest request) {
        Sensor sensor = sensorRepository.findById(request.idSensor())
                .orElseThrow(EntityNotFoundException::new);

        ConsumoEnergetico consumoEnergetico = mapper.paraEntidade(request);
        consumoEnergetico.setSensor(sensor);

        return mapper.paraResponse(consumoEnergeticoRepository.save(consumoEnergetico));
    }

    public ConsumoEnergeticoResponse buscarPorId(Long id) {
        return mapper.paraResponse(
                consumoEnergeticoRepository.findById(id)
                        .orElseThrow(EntityNotFoundException::new));
    }

    public Page<ConsumoEnergeticoResponse> listar(Pageable paginacao) {
        return consumoEnergeticoRepository.findAll(paginacao)
                .map(mapper::paraResponse);
    }

    @Transactional
    public ConsumoEnergeticoResponse atualizar(Long id, ConsumoEnergeticoUpdateRequest request) {
        ConsumoEnergetico consumoEnergetico = consumoEnergeticoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        mapper.atualizarEntidade(request, consumoEnergetico);

        return mapper.paraResponse(consumoEnergetico);
    }

    @Transactional
    public void deletar(Long id) {
        ConsumoEnergetico consumoEnergetico = consumoEnergeticoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        consumoEnergeticoRepository.delete(consumoEnergetico);
    }

}