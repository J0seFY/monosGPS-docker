package com.buscarpersonas.repository;

import com.buscarpersonas.Entity.ResultadoPrueba;
import com.buscarpersonas.dto.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReporteSimceRepository extends JpaRepository<ResultadoPrueba, Long> {

    @Query("SELECT new com.buscarpersonas.dto.ReporteSimceDTO(e.rut, e.nombre, e.apellido, est.nombre, r.puntaje, r.fecha) " +
           "FROM ResultadoPrueba r " +
           "JOIN Estudiante e ON r.estudianteRut = e.rut " +
           "JOIN Establecimiento est ON e.establecimientoId = est.id " +
           "WHERE r.tipoPrueba = 'SIMCE' AND est.comuna = :comuna")
    List<ReporteSimceDTO> findResultadosSimcePorComuna(@Param("comuna") String comuna);
}
