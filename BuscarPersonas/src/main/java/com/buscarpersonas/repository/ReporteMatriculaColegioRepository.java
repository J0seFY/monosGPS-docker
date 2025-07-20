// ReporteMatriculaRepository.java
package com.buscarpersonas.repository;

import com.buscarpersonas.dto.ReporteMatriculaDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ReporteMatriculaColegioRepository extends JpaRepository<Object, Long> {

    @Query("SELECT new com.buscarpersonas.dto.ReporteMatriculaDTO(" +
           "e.nombre, e.direccion, e.comuna, e.telefono, " +
           "est.nombre, est.apellido, est.rut, est.curso, est.nacionalidad, est.estado) " +
           "FROM Estudiante est " +
           "JOIN est.establecimiento e " +
           "WHERE e.id = :idEstablecimiento")
    List<ReporteMatriculaDTO> obtenerReporteMatriculaPorEstablecimiento(@Param("idEstablecimiento") Integer idEstablecimiento);
}
