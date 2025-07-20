package com.buscarpersonas.repository;

import com.buscarpersonas.dto.EstudianteRetiradoDTO;
import com.buscarpersonas.Entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteEstudiantesRetiradosRepository extends JpaRepository<Estudiante, String> {

    /**
     * Obtiene todos los estudiantes retirados con información del establecimiento
     * Usa comparación case-insensitive para el estado
     */
    @Query("SELECT new com.buscarpersonas.dto.EstudianteRetiradoDTO(" +
           "CONCAT(e.nombre, ' ', e.apellido), " +
           "e.rut, " +
           "e.curso, " +
           "e.nacionalidad, " +
           "est.nombre) " +
           "FROM Estudiante e " +
           "JOIN e.establecimiento est " +
           "WHERE UPPER(e.estado) = UPPER('retirado') " +
           "ORDER BY est.nombre, e.nombre, e.apellido")
    List<EstudianteRetiradoDTO> findEstudiantesRetirados();

    /**
     * Obtiene estudiantes retirados por establecimiento específico
     */
    @Query("SELECT new com.buscarpersonas.dto.EstudianteRetiradoDTO(" +
           "CONCAT(e.nombre, ' ', e.apellido), " +
           "e.rut, " +
           "e.curso, " +
           "e.nacionalidad, " +
           "est.nombre) " +
           "FROM Estudiante e " +
           "JOIN e.establecimiento est " +
           "WHERE UPPER(e.estado) = UPPER('retirado') " +
           "AND est.id = :establecimientoId " +
           "ORDER BY e.nombre, e.apellido")
    List<EstudianteRetiradoDTO> findEstudiantesRetiradosByEstablecimiento(Long establecimientoId);

    /**
     * Cuenta total de estudiantes retirados
     */
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE UPPER(e.estado) = UPPER('retirado')")
    Long countEstudiantesRetirados();
}