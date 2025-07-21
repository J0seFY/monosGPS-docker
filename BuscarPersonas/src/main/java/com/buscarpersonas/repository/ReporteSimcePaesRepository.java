package com.buscarpersonas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.buscarpersonas.Entity.Establecimiento;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ReporteSimcePaesRepository extends JpaRepository<Establecimiento, Long> {
    
    // Obtener datos del establecimiento
    @Query("SELECT e FROM Establecimiento e WHERE e.id = :establecimientoId")
    Optional<Establecimiento> findEstablecimientoById(@Param("establecimientoId") Long establecimientoId);
    
    // Obtener estadísticas de SIMCE por establecimiento
    @Query(value = """
        SELECT 
            COUNT(rp.id) as cantidad_estudiantes,
            COALESCE(AVG(rp.puntaje), 0) as promedio,
            COALESCE(MAX(rp.puntaje), 0) as puntaje_maximo,
            COALESCE(MIN(rp.puntaje), 0) as puntaje_minimo
        FROM resultado_prueba rp
        INNER JOIN estudiante e ON rp.estudiante_rut = e.rut
        WHERE e.establecimiento_id = :establecimientoId 
        AND rp.tipo_prueba = 'SIMCE'
        """, nativeQuery = true)
    Map<String, Object> getEstadisticasSimcePorEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    // Obtener estadísticas de PAES por establecimiento
    @Query(value = """
        SELECT 
            COUNT(rp.id) as cantidad_estudiantes,
            COALESCE(AVG(rp.puntaje), 0) as promedio,
            COALESCE(MAX(rp.puntaje), 0) as puntaje_maximo,
            COALESCE(MIN(rp.puntaje), 0) as puntaje_minimo
        FROM resultado_prueba rp
        INNER JOIN estudiante e ON rp.estudiante_rut = e.rut
        WHERE e.establecimiento_id = :establecimientoId 
        AND rp.tipo_prueba = 'PAES'
        """, nativeQuery = true)
    Map<String, Object> getEstadisticasPaesPorEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    // Obtener detalle de resultados SIMCE por establecimiento
    @Query(value = """
        SELECT 
            e.rut as estudiante_rut,
            e.nombre as estudiante_nombre,
            e.apellido as estudiante_apellido,
            e.curso as curso,
            rp.puntaje as puntaje,
            rp.fecha as fecha_prueba
        FROM resultado_prueba rp
        INNER JOIN estudiante e ON rp.estudiante_rut = e.rut
        WHERE e.establecimiento_id = :establecimientoId 
        AND rp.tipo_prueba = 'SIMCE'
        ORDER BY rp.puntaje DESC
        """, nativeQuery = true)
    java.util.List<Map<String, Object>> getDetalleSimcePorEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    // Obtener detalle de resultados PAES por establecimiento
    @Query(value = """
        SELECT 
            e.rut as estudiante_rut,
            e.nombre as estudiante_nombre,
            e.apellido as estudiante_apellido,
            e.curso as curso,
            rp.puntaje as puntaje,
            rp.fecha as fecha_prueba
        FROM resultado_prueba rp
        INNER JOIN estudiante e ON rp.estudiante_rut = e.rut
        WHERE e.establecimiento_id = :establecimientoId 
        AND rp.tipo_prueba = 'PAES'
        ORDER BY rp.puntaje DESC
        """, nativeQuery = true)
    java.util.List<Map<String, Object>> getDetallePaesPorEstablecimiento(@Param("establecimientoId") Long establecimientoId);
}