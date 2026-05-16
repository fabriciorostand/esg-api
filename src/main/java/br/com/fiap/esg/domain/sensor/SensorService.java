package br.com.fiap.esg.domain.sensor;

import br.com.fiap.esg.domain.sensor.dto.SensorRequest;
import br.com.fiap.esg.domain.sensor.dto.SensorResponse;
import br.com.fiap.esg.mapper.SensorMapper;
import br.com.fiap.esg.domain.dispositivo.Dispositivo;
import br.com.fiap.esg.domain.dispositivo.DispositivoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
        Dispositivo dispositivo = dispositivoRepository.findById(request.idDispositivo())
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo não encontrado com o ID: " + request.idDispositivo()));

        Sensor sensor = mapper.paraEntidade(request);
        sensor.setDispositivo(dispositivo);

        return mapper.paraResponse(sensorRepository.save(sensor));
    }

    public SensorResponse buscarPorId(Long id) {
        return mapper.paraResponse(
                sensorRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"))
        );
    }
}
