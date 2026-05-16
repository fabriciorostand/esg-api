package br.com.fiap.esg.mapper;

import br.com.fiap.esg.domain.usuario.dto.RegistroRequest;
import br.com.fiap.esg.domain.usuario.dto.RegistroResponse;
import br.com.fiap.esg.domain.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", source = "senhaCriptografada")
    Usuario paraEntidade(RegistroRequest request, String senhaCriptografada);

    RegistroResponse paraResponse(Usuario usuario);

}