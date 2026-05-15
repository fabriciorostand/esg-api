package br.com.fiap.esg.repository;

import br.com.fiap.esg.model.ConsumoEnergetico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumoEnergeticoRepository extends JpaRepository<ConsumoEnergetico, Long> {
}