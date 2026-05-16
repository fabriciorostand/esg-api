package br.com.fiap.esg.mapper;

import br.com.fiap.esg.domain.unidade_consumidora.dto.UnidadeConsumidoraRequest;
import br.com.fiap.esg.domain.unidade_consumidora.dto.UnidadeConsumidoraResponse;
import br.com.fiap.esg.domain.unidade_consumidora.UnidadeConsumidora;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UnidadeConsumidoraMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco.id", ignore = true)
    UnidadeConsumidora paraEntidade(UnidadeConsumidoraRequest request);

    UnidadeConsumidoraResponse paraResponse(UnidadeConsumidora unidadeConsumidora);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco.id", ignore = true)
    void atualizarEntidade(UnidadeConsumidoraRequest request, @MappingTarget UnidadeConsumidora unidadeConsumidora);

}