package br.com.fiap.esg.service;

import br.com.fiap.esg.dto.UnidadeConsumidoraRequest;
import br.com.fiap.esg.dto.UnidadeConsumidoraResponse;
import br.com.fiap.esg.mapper.UnidadeConsumidoraMapper;
import br.com.fiap.esg.repository.UnidadeConsumidoraRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnidadeConsumidoraService {

    private final UnidadeConsumidoraRepository repository;
    private final UnidadeConsumidoraMapper mapper;

    @Transactional
    public UnidadeConsumidoraResponse cadastrar(UnidadeConsumidoraRequest request) {
        return mapper.paraResponse(
                repository.save(
                        mapper.paraEntidade(request)));
    }

    public UnidadeConsumidoraResponse buscarPorId(Long id) {
        return mapper.paraResponse(
                repository.findById(id)
                        .orElseThrow(EntityNotFoundException::new));
    }

}