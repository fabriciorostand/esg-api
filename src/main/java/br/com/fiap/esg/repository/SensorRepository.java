package br.com.fiap.esg.repository;

import br.com.fiap.esg.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}