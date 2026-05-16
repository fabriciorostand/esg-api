package br.com.fiap.esg.repository;

import br.com.fiap.esg.model.ConsumoEnergetico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ConsumoEnergeticoRepository extends JpaRepository<ConsumoEnergetico, Long> {

    @Query("SELECT SUM(c.kwhConsumido) FROM ConsumoEnergetico c JOIN c.sensor s WHERE s.dispositivo.id = :dispositivoId AND c.dataMedicao BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularConsumoTotalDispositivo(@Param("dispositivoId") Long id, @Param("dataInicio") LocalDate inicio, @Param("dataFim") LocalDate fim);

    @Query("SELECT COUNT(c) FROM ConsumoEnergetico c JOIN c.sensor s WHERE s.dispositivo.id = :dispositivoId AND c.kwhConsumido >= :consumoMinimo AND c.dataMedicao >= :dataLimite")
    long contarConsumosAcimaDoMinimo(@Param("dispositivoId") Long id, @Param("consumoMinimo") BigDecimal consumoMinimo, @Param("dataLimite") LocalDate dataLimite);
}