package br.com.fiap.esg.mapper;

import br.com.fiap.esg.dto.SensorRequest;
import br.com.fiap.esg.dto.SensorResponse;
import br.com.fiap.esg.model.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DispositivoMapper.class})
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dispositivo", ignore = true)
    Sensor paraEntidade(SensorRequest request);

    SensorResponse paraResponse(Sensor sensor);
}
