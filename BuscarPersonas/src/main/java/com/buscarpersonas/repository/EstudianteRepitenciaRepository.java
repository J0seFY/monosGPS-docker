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
        SELECT 
            e.rut,
            e.nombre,
            e.apellido,
            e.curso,
            COALESCE(AVG(r.nota), 0.0) as promedio_notas,
            CASE 
                WHEN COUNT(a.id) > 0 THEN 
                    ROUND((COUNT(CASE WHEN a.presente = true THEN 1 END) * 100.0) / COUNT(a.id), 2)
                ELSE 0.0
            END as porcentaje_asistencia,
            est.nombre as nombre_establecimiento
        FROM estudiante e
        LEFT JOIN rendimiento r ON e.rut = r.estudiante_rut
        LEFT JOIN asistencia a ON e.rut = a.estudiante_rut
        LEFT JOIN establecimiento est ON e.establecimiento_id = est.id
        WHERE e.estado = 'ACTIVO'
        GROUP BY e.rut, e.nombre, e.apellido, e.curso, est.nombre
        HAVING (COALESCE(AVG(r.nota), 0.0) < :promedioMinimo 
            OR (CASE 
                WHEN COUNT(a.id) > 0 THEN 
                    (COUNT(CASE WHEN a.presente = true THEN 1 END) * 100.0) / COUNT(a.id)
                ELSE 0.0
            END) < :asistenciaMinima)
        ORDER BY est.nombre, e.curso, e.apellido, e.nombre
        """, nativeQuery = true)
    List<Object[]> findEstudiantesConRiesgoRepitenciaRaw(
            @Param("promedioMinimo") BigDecimal promedioMinimo,
            @Param("asistenciaMinima") BigDecimal asistenciaMinima
    );

    @Query(value = """
        SELECT COUNT(DISTINCT subquery.rut)
        FROM (
            SELECT 
                e.rut,
                COALESCE(AVG(r.nota), 0.0) as promedio_notas,
                CASE 
                    WHEN COUNT(a.id) > 0 THEN 
                        (COUNT(CASE WHEN a.presente = true THEN 1 END) * 100.0) / COUNT(a.id)
                    ELSE 0.0
                END as porcentaje_asistencia
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
        ) as subquery
        """, nativeQuery = true)
    Long countEstudiantesConRiesgoRepitencia(
            @Param("promedioMinimo") BigDecimal promedioMinimo,
            @Param("asistenciaMinima") BigDecimal asistenciaMinima
    );
}