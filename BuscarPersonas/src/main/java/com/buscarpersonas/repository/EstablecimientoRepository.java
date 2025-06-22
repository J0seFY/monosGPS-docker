package com.buscarpersonas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.buscarpersonas.Entity.Establecimiento;
import java.util.Optional;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Integer> {

    Establecimiento findByNombre(String nombre);
}
