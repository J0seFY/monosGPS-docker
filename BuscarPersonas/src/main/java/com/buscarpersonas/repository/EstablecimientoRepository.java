package com.buscarpersonas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.buscarpersonas.Entity.Establecimiento;
import java.util.Optional;
import java.util.List;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Integer> {

    Establecimiento findByNombre(String nombre);

    // Método para buscar establecimientos por comuna
    List<Establecimiento> findByComuna(String comuna);
    
    // Métodos adicionales útiles para reportes
    List<Establecimiento> findByComunaIgnoreCase(String comuna);
    List<Establecimiento> findByComunaOrderByNombre(String comuna);
    boolean existsByComuna(String comuna);
    int countByComuna(String comuna);
}
