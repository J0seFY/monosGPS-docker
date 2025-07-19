package com.buscarpersonas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.buscarpersonas.Entity.Estudiante;
import com.buscarpersonas.dto.MatriculasComunalesDTO;

public interface EstudianteRepository extends JpaRepository<Estudiante, String> {
    List<Estudiante> findByNombreContainingIgnoreCase(String nombre);

    Estudiante findByRut(String rut);

    // Nuevos métodos para reporte comunal

    /**
     * Consulta para obtener la cantidad de estudiantes agrupados por comuna
     * @return Lista de MatriculasComunalesDTO con comuna y cantidad de estudiantes
     */
    @Query("SELECT new com.buscarpersonas.dto.MatriculasComunalesDTO(e.comuna, COUNT(est)) " +
           "FROM Estudiante est " +
           "JOIN est.establecimiento e " +
           "GROUP BY e.comuna " +
           "ORDER BY e.comuna ASC")
    List<MatriculasComunalesDTO> obtenerMatriculasPorComuna();

    /**
     * Consulta para obtener el total de estudiantes
     * @return Cantidad total de estudiantes
     */
    @Query("SELECT COUNT(est) FROM Estudiante est")
    Long obtenerTotalEstudiantes();

    /**
     * Consulta para obtener la cantidad de comunas distintas
     * @return Cantidad de comunas distintas
     */
    @Query("SELECT COUNT(DISTINCT e.comuna) FROM Estudiante est JOIN est.establecimiento e")
    Long obtenerTotalComunas();
}


