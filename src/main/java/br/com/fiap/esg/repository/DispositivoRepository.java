package br.com.fiap.esg.repository;

import br.com.fiap.esg.model.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    List<Dispositivo> findByStatus(String status);
}
