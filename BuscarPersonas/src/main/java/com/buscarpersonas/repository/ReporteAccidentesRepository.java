package com.buscarpersonas.repository;

import com.buscarpersonas.dto.ReporteAccidentesDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteAccidentesRepository extends CrudRepository<com.buscarpersonas.Entity.AccidenteEscolar, Long> {

    @Query("SELECT new com.buscarpersonas.dto.ReporteAccidentesDTO(e.comuna, e.nombre, COUNT(a.id)) " +
           "FROM AccidenteEscolar a " +
           "JOIN Establecimiento e ON a.establecimiento.id = e.id " +
           "GROUP BY e.comuna, e.nombre " +
           "ORDER BY e.comuna, e.nombre")
    List<ReporteAccidentesDTO> obtenerResumenAccidentes();
}
