package br.com.fiap.esg.mapper;

import br.com.fiap.esg.dto.RegistroRequest;
import br.com.fiap.esg.dto.RegistroResponse;
import br.com.fiap.esg.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", source = "senhaCriptografada")
    Usuario paraEntidade(RegistroRequest request, String senhaCriptografada);

    RegistroResponse paraResponse(Usuario usuario);

}