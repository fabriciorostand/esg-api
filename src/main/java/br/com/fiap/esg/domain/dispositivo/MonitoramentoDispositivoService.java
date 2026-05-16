package br.com.fiap.esg.domain.dispositivo;

import br.com.fiap.esg.domain.consumo_energetico.ConsumoEnergeticoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class MonitoramentoDispositivoService {

    private final DispositivoRepository dispositivoRepository;
    private final ConsumoEnergeticoRepository consumoEnergeticoRepository;

    @Scheduled(cron = "0 0 0 * * *") // Roda todos os dias à meia-noite
    @Transactional
    public void verificarDispositivosOciosos() {
        log.info("Iniciando verificação de dispositivos ociosos...");
        
        List<Dispositivo> dispositivosLigados = dispositivoRepository.findByStatus("LIGADO");
        
        for (Dispositivo dispositivo : dispositivosLigados) {
            if (dispositivo.getTempoOciosidadeLimite() == null || dispositivo.getConsumoMinimoAtivo() == null) {
                continue;
            }

            LocalDate dataLimite = LocalDate.now().minusDays(dispositivo.getTempoOciosidadeLimite());
            
            long consumosAcimaDoMinimo = consumoEnergeticoRepository.contarConsumosAcimaDoMinimo(
                    dispositivo.getId(), 
                    dispositivo.getConsumoMinimoAtivo(), 
                    dataLimite
            );

            if (consumosAcimaDoMinimo == 0) {
                log.info("Dispositivo ID {} está ocioso. Alterando status para DESLIGADO.", dispositivo.getId());
                dispositivo.setStatus("DESLIGADO");
                dispositivoRepository.save(dispositivo);
            }
        }
        
        log.info("Verificação de dispositivos ociosos concluída.");
    }
}
