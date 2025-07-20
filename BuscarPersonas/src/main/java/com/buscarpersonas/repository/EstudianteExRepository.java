package com.buscarpersonas.repository;

import com.buscarpersonas.dto.EstudianteExtranjeroDTO;
import com.buscarpersonas.Entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstudianteExRepository extends JpaRepository<Estudiante, String> {
    
    @Query("SELECT new com.buscarpersonas.dto.EstudianteExtranjeroDTO(" +
           "e.nombre, e.apellido, e.rut, e.curso, e.nacionalidad, est.nombre) " +
           "FROM Estudiante e " +
           "JOIN e.establecimiento est " +
           "WHERE UPPER(e.nacionalidad) NOT IN ('CHILENA', 'CHILENO') " +
           "AND e.nacionalidad IS NOT NULL " +
           "ORDER BY est.nombre, e.apellido, e.nombre")
    List<EstudianteExtranjeroDTO> findEstudiantesExtranjeros();
}
