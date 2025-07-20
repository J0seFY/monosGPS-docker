package com.buscarpersonas.repository;

import com.buscarpersonas.Entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

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
}