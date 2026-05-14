package br.com.fiap.esg.mapper;

import br.com.fiap.esg.dto.MetaConsumoRequest;
import br.com.fiap.esg.dto.MetaConsumoResponse;
import br.com.fiap.esg.model.MetaConsumo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DispositivoMapper.class})
public interface MetaConsumoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dispositivo", ignore = true)
    MetaConsumo paraEntidade(MetaConsumoRequest request);

    MetaConsumoResponse paraResponse(MetaConsumo metaConsumo);
}