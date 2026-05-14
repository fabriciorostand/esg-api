package br.com.fiap.esg.mapper;

import br.com.fiap.esg.dto.AlertaMetaRequest;
import br.com.fiap.esg.dto.AlertaMetaResponse;
import br.com.fiap.esg.model.AlertaMeta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MetaConsumoMapper.class})
public interface AlertaMetaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "metaConsumo", ignore = true)
    AlertaMeta paraEntidade(AlertaMetaRequest request);

    AlertaMetaResponse paraResponse(AlertaMeta alertaMeta);
}