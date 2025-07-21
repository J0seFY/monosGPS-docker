package com.buscarpersonas.repository;

import com.buscarpersonas.dto.AsistenciaEstablecimientoDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReporteAsistenciaRepository extends CrudRepository<com.buscarpersonas.Entity.Establecimiento, Long> {

    @Query("""
        SELECT new com.buscarpersonas.dto.AsistenciaEstablecimientoDTO(
            e.nombre,
            COUNT(DISTINCT s.rut),
            COUNT(a),
            COUNT(DISTINCT s.rut) - COUNT(a)
        )
        FROM Establecimiento e
        JOIN Estudiante s ON s.establecimiento.id = e.id
        LEFT JOIN Asistencia a ON a.estudiante.rut = s.rut AND a.fecha = :fecha AND a.presente = true
        GROUP BY e.nombre
        ORDER BY e.nombre
    """)
    List<AsistenciaEstablecimientoDTO> obtenerReporteAsistenciaPorFecha(@Param("fecha") LocalDate fecha);
}
