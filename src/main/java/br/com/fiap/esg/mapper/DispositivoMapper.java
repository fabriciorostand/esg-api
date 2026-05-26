package br.com.fiap.esg.mapper;

import br.com.fiap.esg.domain.dispositivo.dto.DispositivoRequest;
import br.com.fiap.esg.domain.dispositivo.dto.DispositivoResponse;
import br.com.fiap.esg.domain.dispositivo.Dispositivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UnidadeConsumidoraMapper.class})
public interface DispositivoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidadeConsumidora", ignore = true)
    Dispositivo paraEntidade(DispositivoRequest request);

    DispositivoResponse paraResponse(Dispositivo dispositivo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidadeConsumidora", ignore = true)
    void atualizarEntidade(DispositivoRequest request, @MappingTarget Dispositivo dispositivo);
}
