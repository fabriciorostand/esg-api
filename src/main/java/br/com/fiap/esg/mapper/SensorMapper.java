package br.com.fiap.esg.mapper;

import br.com.fiap.esg.domain.sensor.dto.SensorRequest;
import br.com.fiap.esg.domain.sensor.dto.SensorResponse;
import br.com.fiap.esg.domain.sensor.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DispositivoMapper.class})
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dispositivo", ignore = true)
    Sensor paraEntidade(SensorRequest request);

    SensorResponse paraResponse(Sensor sensor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dispositivo", ignore = true)
    void atualizarEntidade(SensorRequest request, @MappingTarget Sensor sensor);
}
