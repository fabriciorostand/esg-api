package br.com.fiap.esg.domain.meta_consumo;

import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoRequest;
import br.com.fiap.esg.domain.meta_consumo.dto.MetaConsumoResponse;
import br.com.fiap.esg.mapper.MetaConsumoMapper;
import br.com.fiap.esg.domain.dispositivo.Dispositivo;
import br.com.fiap.esg.domain.dispositivo.DispositivoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MetaConsumoService {

    private final MetaConsumoRepository metaConsumoRepository;
    private final DispositivoRepository dispositivoRepository;
    private final MetaConsumoMapper mapper;

    @Transactional
    public MetaConsumoResponse cadastrar(MetaConsumoRequest request) {
        Dispositivo dispositivo = dispositivoRepository.findById(request.idDispositivo())
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo não encontrado com o ID: " + request.idDispositivo()));

        MetaConsumo metaConsumo = mapper.paraEntidade(request);
        metaConsumo.setDispositivo(dispositivo);

        return mapper.paraResponse(metaConsumoRepository.save(metaConsumo));
    }

    public MetaConsumoResponse buscarPorId(Long id) {
        return mapper.paraResponse(
                metaConsumoRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Meta de Consumo não encontrada"))
        );
    }
}