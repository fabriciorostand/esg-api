package br.com.fiap.esg.service;

import br.com.fiap.esg.dto.UnidadeConsumidoraRequest;
import br.com.fiap.esg.dto.UnidadeConsumidoraResponse;
import br.com.fiap.esg.mapper.UnidadeConsumidoraMapper;
import br.com.fiap.esg.model.UnidadeConsumidora;
import br.com.fiap.esg.repository.UnidadeConsumidoraRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnidadeConsumidoraService {

    private final UnidadeConsumidoraRepository repository;
    private final UnidadeConsumidoraMapper mapper;

    @Transactional
    public UnidadeConsumidoraResponse cadastrar(UnidadeConsumidoraRequest request) {
        UnidadeConsumidora unidadeConsumidora = mapper.paraEntidade(request);

        return mapper.paraResponse(repository.save(unidadeConsumidora));
    }

    public UnidadeConsumidoraResponse buscarPorId(Long id) {
        return mapper.paraResponse(
                repository.findById(id)
                        .orElseThrow(EntityNotFoundException::new));
    }

    public Page<UnidadeConsumidoraResponse> listar(Pageable paginacao) {
        return repository.findAll(paginacao)
                .map(mapper::paraResponse);
    }

    @Transactional
    public UnidadeConsumidoraResponse atualizar(Long id, UnidadeConsumidoraRequest request) {
        UnidadeConsumidora unidadeConsumidora = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        mapper.atualizarEntidade(request, unidadeConsumidora);

        return mapper.paraResponse(unidadeConsumidora);
    }

    @Transactional
    public void deletar(Long id) {
        UnidadeConsumidora unidadeConsumidora = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        repository.delete(unidadeConsumidora);
    }

}