package br.com.fiap.esg.mapper;

import br.com.fiap.esg.dto.DispositivoRequest;
import br.com.fiap.esg.dto.DispositivoResponse;
import br.com.fiap.esg.model.Dispositivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UnidadeConsumidoraMapper.class})
public interface DispositivoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidadeConsumidora", ignore = true)
    Dispositivo paraEntidade(DispositivoRequest request);

    DispositivoResponse paraResponse(Dispositivo dispositivo);
}
