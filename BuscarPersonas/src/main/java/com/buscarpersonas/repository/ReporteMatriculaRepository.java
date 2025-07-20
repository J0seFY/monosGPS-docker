package com.buscarpersonas.repository;

import com.buscarpersonas.Entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// repository/ReporteMatriculaRepository.java
@Repository
public interface ReporteMatriculaRepository extends JpaRepository<Estudiante, String> {
    
    @Query("SELECT e FROM Estudiante e JOIN e.establecimiento est WHERE est.comuna = :comuna")
    List<Estudiante> findEstudiantesByComuna(@Param("comuna") String comuna);
    
    @Query("SELECT e FROM Estudiante e WHERE e.establecimiento.id = :establecimientoId")
    List<Estudiante> findEstudiantesByEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.establecimiento.id = :establecimientoId")
    int countEstudiantesByEstablecimiento(@Param("establecimientoId") Long establecimientoId);
    
    @Query("SELECT e.curso, COUNT(e) FROM Estudiante e WHERE e.establecimiento.id = :establecimientoId GROUP BY e.curso")
    List<Object[]> countEstudiantesByCursoAndEstablecimiento(@Param("establecimientoId") Long establecimientoId);

    /**
     * Obtiene la información completa de matrícula de un establecimiento específico
     * incluyendo datos del establecimiento y todos sus estudiantes matriculados
     */
    @Query(value = """
        SELECT 
            e.id as establecimiento_id,
            e.nombre as establecimiento_nombre,
            e.direccion as establecimiento_direccion,
            e.comuna as establecimiento_comuna,
            e.telefono as establecimiento_telefono,
            est.rut as estudiante_rut,
            est.nombre as estudiante_nombre,
            est.apellido as estudiante_apellido,
            est.fecha_nacimiento as estudiante_fecha_nacimiento,
            est.telefono as estudiante_telefono,
            est.curso as estudiante_curso,
            est.nacionalidad as estudiante_nacionalidad,
            est.estado as estudiante_estado
        FROM establecimiento e
        LEFT JOIN estudiante est ON e.id = est.establecimiento_id
        WHERE e.id = :establecimientoId
        ORDER BY est.curso, est.apellido, est.nombre
        """, nativeQuery = true)
    List<Object[]> findReporteMatriculaByEstablecimientoId(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Obtiene solo la información básica del establecimiento
     */
    @Query(value = """
        SELECT 
            e.id as establecimiento_id,
            e.nombre as establecimiento_nombre,
            e.direccion as establecimiento_direccion,
            e.comuna as establecimiento_comuna,
            e.telefono as establecimiento_telefono
        FROM establecimiento e
        WHERE e.id = :establecimientoId
        """, nativeQuery = true)
    Optional<Object[]> findEstablecimientoById(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Cuenta el total de estudiantes matriculados en un establecimiento
     */
    @Query(value = """
        SELECT COUNT(*)
        FROM estudiante est
        WHERE est.establecimiento_id = :establecimientoId
        """, nativeQuery = true)
    Long countEstudiantesByEstablecimientoId(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Obtiene estudiantes agrupados por curso para un establecimiento específico
     */
    @Query(value = """
        SELECT 
            est.curso,
            COUNT(*) as total_estudiantes,
            est.rut as estudiante_rut,
            est.nombre as estudiante_nombre,
            est.apellido as estudiante_apellido,
            est.fecha_nacimiento as estudiante_fecha_nacimiento,
            est.telefono as estudiante_telefono,
            est.nacionalidad as estudiante_nacionalidad,
            est.estado as estudiante_estado
        FROM estudiante est
        WHERE est.establecimiento_id = :establecimientoId
        GROUP BY est.curso, est.rut, est.nombre, est.apellido, 
                 est.fecha_nacimiento, est.telefono, est.nacionalidad, est.estado
        ORDER BY est.curso, est.apellido, est.nombre
        """, nativeQuery = true)
    List<Object[]> findEstudiantesPorCurso(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Verifica si existe un establecimiento con el ID dado
     */
    @Query(value = """
        SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
        FROM establecimiento e
        WHERE e.id = :establecimientoId
        """, nativeQuery = true)
    boolean existsEstablecimientoById(@Param("establecimientoId") Long establecimientoId);
    
    /**
     * Obtiene estadísticas resumidas del establecimiento
     */
    @Query(value = """
        SELECT 
            e.nombre as establecimiento_nombre,
            COUNT(est.rut) as total_estudiantes,
            COUNT(DISTINCT est.curso) as total_cursos,
            COUNT(CASE WHEN est.estado = 'ACTIVO' THEN 1 END) as estudiantes_activos,
            COUNT(CASE WHEN est.estado = 'INACTIVO' THEN 1 END) as estudiantes_inactivos
        FROM establecimiento e
        LEFT JOIN estudiante est ON e.id = est.establecimiento_id
        WHERE e.id = :establecimientoId
        GROUP BY e.id, e.nombre
        """, nativeQuery = true)
    Optional<Object[]> findEstadisticasEstablecimiento(@Param("establecimientoId") Long establecimientoId);
}
