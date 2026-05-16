package br.com.fiap.esg.mapper;

import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoRequest;
import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoResponse;
import br.com.fiap.esg.domain.meta_consumo.MetaConsumo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DispositivoMapper.class})
public interface MetaConsumoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dispositivo", ignore = true)
    MetaConsumo paraEntidade(MetaConsumoRequest request);

    MetaConsumoResponse paraResponse(MetaConsumo metaConsumo);
}