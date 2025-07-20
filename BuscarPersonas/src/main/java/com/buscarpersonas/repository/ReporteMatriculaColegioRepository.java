package com.buscarpersonas.repository;

import com.buscarpersonas.Entity.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteMatriculaColegioRepository extends JpaRepository<Establecimiento, Long> {

    @Query("SELECT e FROM Establecimiento e LEFT JOIN FETCH e.estudiantes est WHERE e.id = :establecimientoId")
    Optional<Establecimiento> findEstablecimientoWithEstudiantes(@Param("establecimientoId") Long establecimientoId);

    @Query("SELECT COUNT(est) FROM Estudiante est WHERE est.establecimiento.id = :establecimientoId")
    int countEstudiantesByEstablecimientoId(@Param("establecimientoId") Long establecimientoId);

    @Query("SELECT est FROM Estudiante est WHERE est.establecimiento.id = :establecimientoId ORDER BY est.apellido ASC, est.nombre ASC")
    List<Object[]> findEstudiantesByEstablecimientoId(@Param("establecimientoId") Long establecimientoId);
}

