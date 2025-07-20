package com.buscarpersonas.repository;

import com.buscarpersonas.Entity.Rendimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RendimientoRepository extends JpaRepository<Rendimiento, Integer> {

    @Query(value = """
        SELECT 
            r.estudiante_rut,
            e.nombre,
            e.apellido,
            r.asignatura,
            AVG(r.nota) as promedio_notas,
            COUNT(r.nota) as cantidad_notas,
            MAX(r.nota) as nota_maxima,
            MIN(r.nota) as nota_minima,
            es.nombre_establecimiento
        FROM rendimiento r
        INNER JOIN estudiante e ON r.estudiante_rut = e.rut
        INNER JOIN establecimiento es ON e.establecimiento_id = es.id
        WHERE (:asignatura IS NULL OR r.asignatura = :asignatura)
        AND (:promedioMinimo IS NULL OR AVG(r.nota) >= :promedioMinimo)
        GROUP BY r.estudiante_rut, e.nombre, e.apellido, r.asignatura, es.nombre_establecimiento
        ORDER BY r.asignatura, AVG(r.nota) DESC
        """, nativeQuery = true)
    List<Object[]> findRendimientoPorAsignaturaRaw(
        @Param("asignatura") String asignatura,
        @Param("promedioMinimo") BigDecimal promedioMinimo
    );

    @Query(value = """
        SELECT DISTINCT asignatura 
        FROM rendimiento 
        ORDER BY asignatura
        """, nativeQuery = true)
    List<String> findAllAsignaturas();

    @Query(value = """
        SELECT 
            r.asignatura,
            AVG(r.nota) as promedio_general,
            COUNT(DISTINCT r.estudiante_rut) as total_estudiantes,
            COUNT(r.nota) as total_notas
        FROM rendimiento r
        WHERE (:asignatura IS NULL OR r.asignatura = :asignatura)
        GROUP BY r.asignatura
        ORDER BY r.asignatura
        """, nativeQuery = true)
    List<Object[]> findEstadisticasGeneralesPorAsignatura(@Param("asignatura") String asignatura);
}