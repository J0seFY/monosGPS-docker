package com.buscarpersonas.repository;

import com.buscarpersonas.dto.EstudianteRepitenciaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.buscarpersonas.Entity.Estudiante;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EstudianteRepitenciaRepository extends JpaRepository<Estudiante, String> {

    @Query(value = """
        SELECT new com.buscarpersonas.dto.EstudianteRepitenciaDTO(
            e.rut,
            e.nombre,
            e.apellido,
            e.curso,
            COALESCE(AVG(r.nota), 0.0),
            CASE 
                WHEN COUNT(a.id) > 0 THEN 
                    ROUND((COUNT(CASE WHEN a.presente = true THEN 1 END) * 100.0) / COUNT(a.id), 2)
                ELSE 0.0
            END,
            est.nombre
        )
        FROM Estudiante e
        LEFT JOIN Rendimiento r ON e.rut = r.estudianteRut
        LEFT JOIN Asistencia a ON e.rut = a.estudianteRut
        LEFT JOIN Establecimiento est ON e.establecimientoId = est.id
        WHERE e.estado = 'ACTIVO'
        GROUP BY e.rut, e.nombre, e.apellido, e.curso, est.nombre
        HAVING (COALESCE(AVG(r.nota), 0.0) < :promedioMinimo 
            OR (CASE 
                WHEN COUNT(a.id) > 0 THEN 
                    (COUNT(CASE WHEN a.presente = true THEN 1 END) * 100.0) / COUNT(a.id)
                ELSE 0.0
            END) < :asistenciaMinima)
        ORDER BY est.nombre, e.curso, e.apellido, e.nombre
        """)
    List<EstudianteRepitenciaDTO> findEstudiantesConRiesgoRepitencia(
            @Param("promedioMinimo") BigDecimal promedioMinimo,
            @Param("asistenciaMinima") BigDecimal asistenciaMinima
    );

    @Query(value = """
        SELECT COUNT(DISTINCT e.rut)
        FROM estudiante e
        LEFT JOIN rendimiento r ON e.rut = r.estudiante_rut
        LEFT JOIN asistencia a ON e.rut = a.estudiante_rut
        WHERE e.estado = 'ACTIVO'
        GROUP BY e.rut
        HAVING (COALESCE(AVG(r.nota), 0.0) < :promedioMinimo 
            OR (CASE 
                WHEN COUNT(a.id) > 0 THEN 
                    (COUNT(CASE WHEN a.presente = true THEN 1 END) * 100.0) / COUNT(a.id)
                ELSE 0.0
            END) < :asistenciaMinima)
        """, nativeQuery = true)
    Long countEstudiantesConRiesgoRepitencia(
            @Param("promedioMinimo") BigDecimal promedioMinimo,
            @Param("asistenciaMinima") BigDecimal asistenciaMinima
    );
}