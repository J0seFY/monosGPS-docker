package com.buscarpersonas.repository;

import com.buscarpersonas.dto.RendimientoReporteDTO;
import com.buscarpersonas.Entity.Rendimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RendimientoRepository extends JpaRepository<Rendimiento, Long> {
    
    /**
     * Obtiene el rendimiento por establecimiento específico
     */
    @Query("""
        SELECT new com.buscarpersonas.dto.RendimientoReporteDTO(
            e.nombre,
            e.comuna,
            r.asignatura,
            COALESCE(AVG(r.nota), 0),
            COUNT(DISTINCT est.rut),
            COUNT(r.nota),
            COALESCE(MAX(r.nota), 0),
            COALESCE(MIN(r.nota), 0),
            est.curso
        )
        FROM Rendimiento r
        JOIN r.estudiante est
        JOIN est.establecimiento e
        WHERE e.id = :establecimientoId
        GROUP BY e.nombre, e.comuna, r.asignatura, est.curso
        ORDER BY r.asignatura, est.curso
    """)
    List<RendimientoReporteDTO> obtenerRendimientoPorEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Obtiene el rendimiento a nivel comunal
     */
    @Query("""
        SELECT new com.buscarpersonas.dto.RendimientoReporteDTO(
            e.comuna,
            r.asignatura,
            COALESCE(AVG(r.nota), 0),
            COUNT(DISTINCT est.rut),
            COUNT(r.nota)
        )
        FROM Rendimiento r
        JOIN r.estudiante est
        JOIN est.establecimiento e
        WHERE (:comuna IS NULL OR e.comuna = :comuna)
        GROUP BY e.comuna, r.asignatura
        ORDER BY e.comuna, r.asignatura
    """)
    List<RendimientoReporteDTO> obtenerRendimientoComunal(@Param("comuna") String comuna);
    
    /**
     * Obtiene el rendimiento detallado por establecimiento con filtros adicionales
     */
    @Query("""
        SELECT new com.buscarpersonas.dto.RendimientoReporteDTO(
            e.nombre,
            e.comuna,
            r.asignatura,
            COALESCE(AVG(r.nota), 0),
            COUNT(DISTINCT est.rut),
            COUNT(r.nota),
            COALESCE(MAX(r.nota), 0),
            COALESCE(MIN(r.nota), 0),
            est.curso
        )
        FROM Rendimiento r
        JOIN r.estudiante est
        JOIN est.establecimiento e
        WHERE (:establecimientoId IS NULL OR e.id = :establecimientoId)
        AND (:asignatura IS NULL OR r.asignatura = :asignatura)
        AND (:curso IS NULL OR est.curso = :curso)
        AND r.fecha BETWEEN :fechaInicio AND :fechaFin
        GROUP BY e.nombre, e.comuna, r.asignatura, est.curso
        ORDER BY e.nombre, r.asignatura, est.curso
    """)
    List<RendimientoReporteDTO> obtenerRendimientoConFiltros(
        @Param("establecimientoId") Long establecimientoId,
        @Param("asignatura") String asignatura,
        @Param("curso") String curso,
        @Param("fechaInicio") java.time.LocalDate fechaInicio,
        @Param("fechaFin") java.time.LocalDate fechaFin
    );
    
    /**
     * Obtiene lista de comunas disponibles
     */
    @Query("SELECT DISTINCT e.comuna FROM Establecimiento e ORDER BY e.comuna")
    List<String> obtenerComunas();
    
    /**
     * Obtiene lista de asignaturas disponibles
     */
    @Query("SELECT DISTINCT r.asignatura FROM Rendimiento r ORDER BY r.asignatura")
    List<String> obtenerAsignaturas();
}