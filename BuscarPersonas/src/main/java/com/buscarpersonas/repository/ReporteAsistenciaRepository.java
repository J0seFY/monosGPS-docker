package com.buscarpersonas.repository;

import com.buscarpersonas.dto.AsistenciaEstablecimientoDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReporteAsistenciaRepository extends Repository<Object, Long> {

    @Query("""
        SELECT new com.buscarpersonas.dto.AsistenciaEstablecimientoDTO(
            e.nombre,
            COUNT(DISTINCT s.rut),
            COUNT(a),
            COUNT(DISTINCT s.rut) - COUNT(a)
        )
        FROM establecimiento e
        JOIN estudiante s ON s.establecimiento_id = e.id
        LEFT JOIN asistencia a ON a.estudiante_rut = s.rut AND a.fecha = :fecha AND a.presente = true
        GROUP BY e.nombre
        ORDER BY e.nombre
    """)
    List<AsistenciaEstablecimientoDTO> obtenerReporteAsistenciaPorFecha(@Param("fecha") LocalDate fecha);
}
