package br.com.fiap.esg.domain.sensor;

import br.com.fiap.esg.domain.dispositivo.Dispositivo;
import br.com.fiap.esg.domain.dispositivo.DispositivoRepository;
import br.com.fiap.esg.domain.sensor.dto.SensorRequest;
import br.com.fiap.esg.domain.sensor.dto.SensorResponse;
import br.com.fiap.esg.mapper.SensorMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final DispositivoRepository dispositivoRepository;
    private final SensorMapper mapper;

    @Transactional
    public SensorResponse cadastrar(SensorRequest request) {
        Dispositivo dispositivo = buscarDispositivo(request.idDispositivo());

        Sensor sensor = mapper.paraEntidade(request);
        sensor.setDispositivo(dispositivo);

        return mapper.paraResponse(sensorRepository.save(sensor));
    }

    public SensorResponse buscarPorId(Long id) {
        return mapper.paraResponse(buscarSensor(id));
    }

    public Page<SensorResponse> listar(Pageable paginacao) {
        return sensorRepository.findAll(paginacao)
                .map(mapper::paraResponse);
    }

    @Transactional
    public SensorResponse atualizar(Long id, SensorRequest request) {
        Sensor sensor = buscarSensor(id);
        Dispositivo dispositivo = buscarDispositivo(request.idDispositivo());

        mapper.atualizarEntidade(request, sensor);
        sensor.setDispositivo(dispositivo);

        return mapper.paraResponse(sensor);
    }

    @Transactional
    public void deletar(Long id) {
        sensorRepository.delete(buscarSensor(id));
    }

    private Sensor buscarSensor(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor nao encontrado"));
    }

    private Dispositivo buscarDispositivo(Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo nao encontrado com o ID: " + id));
    }
}
