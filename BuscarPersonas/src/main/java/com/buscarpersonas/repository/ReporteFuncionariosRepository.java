package com.buscarpersonas.repository;

import com.buscarpersonas.dto.ReporteFuncionariosDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.buscarpersonas.Entity.Profesor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteFuncionariosRepository extends CrudRepository<Profesor, String> {

    @Query("SELECT new com.buscarpersonas.dto.ReporteFuncionariosDTO(e.comuna, e.nombre, p.nombre, p.apellido, p.asignatura) " +
           "FROM Profesor p " +
           "JOIN Establecimiento e ON p.establecimiento.id = e.id " +
           "ORDER BY e.comuna, e.nombre, p.apellido")
    List<ReporteFuncionariosDTO> obtenerFuncionariosComunal();

    @Query("SELECT new com.buscarpersonas.dto.ReporteFuncionariosDTO(e.comuna, e.nombre, p.nombre, p.apellido, p.asignatura) " +
           "FROM Profesor p " +
           "JOIN Establecimiento e ON p.establecimiento.id = e.id " +
           "WHERE e.id = :establecimientoId " +
           "ORDER BY p.apellido")
    List<ReporteFuncionariosDTO> obtenerFuncionariosPorEstablecimiento(int establecimientoId);
}
