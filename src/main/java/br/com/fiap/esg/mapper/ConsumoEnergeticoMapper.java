package br.com.fiap.esg.mapper;

import br.com.fiap.esg.domain.consumo_energetico.dto.ConsumoEnergeticoRequest;
import br.com.fiap.esg.domain.consumo_energetico.dto.ConsumoEnergeticoResponse;
import br.com.fiap.esg.domain.consumo_energetico.dto.ConsumoEnergeticoUpdateRequest;
import br.com.fiap.esg.domain.consumo_energetico.ConsumoEnergetico;
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