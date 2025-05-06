package com.buscarpersonas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.buscarpersonas.Entity.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, String> {
    List<Estudiante> findByNombreContainingIgnoreCase(String nombre);

    Estudiante findByRut(String rut);
}


