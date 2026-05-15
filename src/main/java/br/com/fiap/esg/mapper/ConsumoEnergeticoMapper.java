package br.com.fiap.esg.mapper;

import br.com.fiap.esg.dto.ConsumoEnergeticoRequest;
import br.com.fiap.esg.dto.ConsumoEnergeticoResponse;
import br.com.fiap.esg.dto.ConsumoEnergeticoUpdateRequest;
import br.com.fiap.esg.model.ConsumoEnergetico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {SensorMapper.class})
public interface ConsumoEnergeticoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sensor", ignore = true)
    ConsumoEnergetico paraEntidade(ConsumoEnergeticoRequest request);

    ConsumoEnergeticoResponse paraResponse(ConsumoEnergetico consumoEnergetico);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sensor", ignore = true)
    void atualizarEntidade(ConsumoEnergeticoUpdateRequest request, @MappingTarget ConsumoEnergetico consumoEnergetico);

}