package com.buscarpersonas.repository;

import com.buscarpersonas.dto.ReporteInasistenciasDTO;
import com.buscarpersonas.Entity.Asistencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteInasistenciasRepository extends CrudRepository<Asistencia, Long> {

    @Query("SELECT new com.buscarpersonas.dto.ReporteInasistenciasDTO(e.comuna, e.nombre, est.nombre, est.apellido, COUNT(a)) " +
           "FROM Asistencia a " +
           "JOIN Estudiante est ON a.estudianteRut = est.rut " +
           "JOIN Establecimiento e ON est.establecimiento.id = e.id " +
           "WHERE a.presente = false " +
           "GROUP BY e.comuna, e.nombre, est.nombre, est.apellido " +
           "ORDER BY e.comuna, e.nombre, est.apellido")
    List<ReporteInasistenciasDTO> obtenerInasistenciasComunal();

    @Query("SELECT new com.buscarpersonas.dto.ReporteInasistenciasDTO(e.comuna, e.nombre, est.nombre, est.apellido, COUNT(a)) " +
           "FROM Asistencia a " +
           "JOIN Estudiante est ON a.estudianteRut = est.rut " +
           "JOIN Establecimiento e ON est.establecimiento.id = e.id " +
           "WHERE a.presente = false AND e.id = :establecimientoId " +
           "GROUP BY e.comuna, e.nombre, est.nombre, est.apellido " +
           "ORDER BY est.apellido")
    List<ReporteInasistenciasDTO> obtenerInasistenciasPorEstablecimiento(int establecimientoId);
}
