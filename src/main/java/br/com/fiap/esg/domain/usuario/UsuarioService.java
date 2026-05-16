package br.com.fiap.esg.domain.usuario;

import br.com.fiap.esg.domain.usuario.dto.RegistroRequest;
import br.com.fiap.esg.domain.usuario.dto.RegistroResponse;
import br.com.fiap.esg.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper mapper;

    @Transactional
    public RegistroResponse registrar(RegistroRequest request) {
        String senha = request.senha();
        String senhaCriptografada = passwordEncoder.encode(senha);

        Usuario usuario = mapper.paraEntidade(request, senhaCriptografada);

        Usuario usuarioCadastrado = usuarioRepository.save(usuario);

        return mapper.paraResponse(usuarioCadastrado);
    }

}