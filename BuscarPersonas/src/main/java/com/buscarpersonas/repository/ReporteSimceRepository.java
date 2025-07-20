package com.buscarpersonas.repository;

import com.buscarpersonas.dto.ReporteSimceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para consultas relacionadas con reportes SIMCE
 */
@Repository
public interface ReporteSimceRepository extends JpaRepository<Object, Long> {
    
    /**
     * Obtiene los datos del reporte SIMCE para un establecimiento específico
     */
    @Query(value = """
        SELECT 
            e.id as establecimientoId,
            e.nombre as nombreEstablecimiento,
            e.direccion as direccionEstablecimiento,
            e.comuna as comunaEstablecimiento,
            e.telefono as telefonoEstablecimiento,
            COUNT(DISTINCT est.rut) as totalEstudiantesSimce,
            ROUND(AVG(rp.puntaje), 2) as puntajePromedio,
            MAX(rp.puntaje) as puntajeMaximo,
            MIN(rp.puntaje) as puntajeMinimo
        FROM establecimiento e
        LEFT JOIN estudiante est ON e.id = est.establecimiento_id
        LEFT JOIN resultado_prueba rp ON est.rut = rp.estudiante_rut 
            AND UPPER(rp.tipo_prueba) = 'SIMCE'
        WHERE e.id = :establecimientoId
        GROUP BY e.id, e.nombre, e.direccion, e.comuna, e.telefono
        """, nativeQuery = true)
    Optional<Object[]> findDatosReporteSimceByEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Obtiene todos los reportes SIMCE de todos los establecimientos
     */
    @Query(value = """
        SELECT 
            e.id as establecimientoId,
            e.nombre as nombreEstablecimiento,
            e.direccion as direccionEstablecimiento,
            e.comuna as comunaEstablecimiento,
            e.telefono as telefonoEstablecimiento,
            COUNT(DISTINCT est.rut) as totalEstudiantesSimce,
            ROUND(AVG(rp.puntaje), 2) as puntajePromedio,
            MAX(rp.puntaje) as puntajeMaximo,
            MIN(rp.puntaje) as puntajeMinimo
        FROM establecimiento e
        LEFT JOIN estudiante est ON e.id = est.establecimiento_id
        LEFT JOIN resultado_prueba rp ON est.rut = rp.estudiante_rut 
            AND UPPER(rp.tipo_prueba) = 'SIMCE'
        GROUP BY e.id, e.nombre, e.direccion, e.comuna, e.telefono
        ORDER BY e.nombre
        """, nativeQuery = true)
    List<Object[]> findAllDatosReporteSimce();
    
    /**
     * Verifica si existe un establecimiento con el ID proporcionado
     */
    @Query(value = "SELECT COUNT(*) FROM establecimiento WHERE id = :establecimientoId", nativeQuery = true)
    Integer existeEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Obtiene el detalle de estudiantes y sus puntajes SIMCE para un establecimiento
     */
    @Query(value = """
        SELECT 
            est.rut,
            est.nombre,
            est.apellido,
            est.curso,
            rp.puntaje,
            rp.fecha
        FROM estudiante est
        INNER JOIN resultado_prueba rp ON est.rut = rp.estudiante_rut
        WHERE est.establecimiento_id = :establecimientoId 
            AND UPPER(rp.tipo_prueba) = 'SIMCE'
        ORDER BY est.apellido, est.nombre
        """, nativeQuery = true)
    List<Object[]> findDetalleEstudiantesSimceByEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Obtiene estadísticas adicionales para el reporte
     */
    @Query(value = """
        SELECT 
            COUNT(*) as totalPruebas,
            COUNT(DISTINCT rp.fecha) as fechasDistintas,
            MIN(rp.fecha) as fechaMasAntigua,
            MAX(rp.fecha) as fechaMasReciente
        FROM estudiante est
        INNER JOIN resultado_prueba rp ON est.rut = rp.estudiante_rut
        WHERE est.establecimiento_id = :establecimientoId 
            AND UPPER(rp.tipo_prueba) = 'SIMCE'
        """, nativeQuery = true)
    Optional<Object[]> findEstadisticasAdicionales(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Obtiene la distribución de puntajes por rangos
     */
    @Query(value = """
        SELECT 
            CASE 
                WHEN rp.puntaje < 250 THEN 'Insuficiente'
                WHEN rp.puntaje >= 250 AND rp.puntaje < 300 THEN 'Elemental'
                WHEN rp.puntaje >= 300 AND rp.puntaje < 350 THEN 'Adecuado'
                ELSE 'Avanzado'
            END as nivelLogro,
            COUNT(*) as cantidad
        FROM estudiante est
        INNER JOIN resultado_prueba rp ON est.rut = rp.estudiante_rut
        WHERE est.establecimiento_id = :establecimientoId 
            AND UPPER(rp.tipo_prueba) = 'SIMCE'
        GROUP BY 
            CASE 
                WHEN rp.puntaje < 250 THEN 'Insuficiente'
                WHEN rp.puntaje >= 250 AND rp.puntaje < 300 THEN 'Elemental'
                WHEN rp.puntaje >= 300 AND rp.puntaje < 350 THEN 'Adecuado'
                ELSE 'Avanzado'
            END
        ORDER BY MIN(rp.puntaje)
        """, nativeQuery = true)
    List<Object[]> findDistribucionPorNivelesLogro(@Param("establecimientoId") Long establecimientoId);
}