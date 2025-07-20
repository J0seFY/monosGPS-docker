package com.buscarpersonas.repository;

import com.buscarpersonas.Entity.Rendimiento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RendimientoRepository extends CrudRepository<Rendimiento, Long> {
    @Query("SELECT new com.buscarpersonas.dto.RendimientoDTO(r.establecimiento, AVG(r.nota), COUNT(r.id)) " +
           "FROM Rendimiento r GROUP BY r.establecimiento")
    List<com.buscarpersonas.dto.RendimientoDTO> obtenerResumenComunal();
}
