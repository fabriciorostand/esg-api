package br.com.fiap.esg.mapper;

import br.com.fiap.esg.dto.UnidadeConsumidoraRequest;
import br.com.fiap.esg.dto.UnidadeConsumidoraResponse;
import br.com.fiap.esg.model.UnidadeConsumidora;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnidadeConsumidoraMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco.id", ignore = true)
    UnidadeConsumidora paraEntidade(UnidadeConsumidoraRequest request);

    UnidadeConsumidoraResponse paraResponse(UnidadeConsumidora unidadeConsumidora);

}