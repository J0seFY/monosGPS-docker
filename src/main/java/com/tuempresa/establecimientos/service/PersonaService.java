package com.tuempresa.establecimientos.service;

import com.tuempresa.establecimientos.Entity.Estudiante;
import com.tuempresa.establecimientos.Entity.Profesor;
import com.tuempresa.establecimientos.dto.PersonaDTO;
import com.tuempresa.establecimientos.repository.EstudianteRepository;
import com.tuempresa.establecimientos.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonaService {

    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;

    @Autowired
    public PersonaService(EstudianteRepository estudianteRepository, ProfesorRepository profesorRepository) {
        this.estudianteRepository = estudianteRepository;
        this.profesorRepository = profesorRepository;
    }

    public List<PersonaDTO> buscarPorNombre(String nombre) {
        List<PersonaDTO> personas = new ArrayList<>();

        List<Estudiante> estudiantes = estudianteRepository.findByNombreContainingIgnoreCase(nombre);
        for (Estudiante e : estudiantes) {
            PersonaDTO dto = new PersonaDTO();
            dto.setTipo("Estudiante");
            dto.setRut(e.getRut());
            dto.setNombre(e.getNombre());
            dto.setApellido(e.getApellido());
            dto.setTelefono(e.getTelefono());
            dto.setEstablecimiento(e.getEstablecimiento().getNombre());
            personas.add(dto);
        }

        List<Profesor> profesores = profesorRepository.findByNombreContainingIgnoreCase(nombre);
        for (Profesor p : profesores) {
            PersonaDTO dto = new PersonaDTO();
            dto.setTipo("Profesor");
            dto.setRut(p.getRut());
            dto.setNombre(p.getNombre());
            dto.setApellido(p.getApellido());
            dto.setTelefono(p.getTelefono());
            dto.setEstablecimiento(p.getEstablecimiento().getNombre());
            personas.add(dto);
        }

        return personas;
    }

    public PersonaDTO buscarPorRut(String rut) {
        // Intentar buscar en estudiantes
        Estudiante estudiante = estudianteRepository.findByRut(rut);
        if (estudiante != null) {
            PersonaDTO dto = new PersonaDTO();
            dto.setTipo("Estudiante");
            dto.setRut(estudiante.getRut());
            dto.setNombre(estudiante.getNombre());
            dto.setApellido(estudiante.getApellido());
            dto.setTelefono(estudiante.getTelefono());
            dto.setEstablecimiento(estudiante.getEstablecimiento().getNombre());
            return dto;
        }
    
        // Intentar buscar en profesores
        Profesor profesor = profesorRepository.findByRut(rut);
        if (profesor != null) {
            PersonaDTO dto = new PersonaDTO();
            dto.setTipo("Profesor");
            dto.setRut(profesor.getRut());
            dto.setNombre(profesor.getNombre());
            dto.setApellido(profesor.getApellido());
            dto.setTelefono(profesor.getTelefono());
            dto.setEstablecimiento(profesor.getEstablecimiento().getNombre());
            return dto;
        }
    
        // No se encontró
        return null;
    }
    
}
