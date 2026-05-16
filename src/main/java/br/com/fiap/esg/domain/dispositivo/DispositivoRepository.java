package br.com.fiap.esg.domain.dispositivo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    List<Dispositivo> findByStatus(String status);
}
