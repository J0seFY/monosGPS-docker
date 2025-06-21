package com.buscarpersonas.service;

import com.buscarpersonas.Entity.Establecimiento;
import com.buscarpersonas.Entity.Estudiante;
import com.buscarpersonas.Entity.Profesor;
import com.buscarpersonas.dto.PersonaDTO;
import com.buscarpersonas.repository.EstudianteRepository;
import com.buscarpersonas.repository.ProfesorRepository;
import com.buscarpersonas.repository.EstablecimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonaService {

    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final EstablecimientoRepository establecimientoRepository;

    @Autowired
    public PersonaService(EstudianteRepository estudianteRepository, ProfesorRepository profesorRepository,
            EstablecimientoRepository establecimientoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.profesorRepository = profesorRepository;
        this.establecimientoRepository = establecimientoRepository;
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
            dto.setEstablecimiento(
                    e.getEstablecimiento() != null ? e.getEstablecimiento().getNombre() : "Sin establecimiento");
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
            dto.setEstablecimiento(
                    p.getEstablecimiento() != null ? p.getEstablecimiento().getNombre() : "Sin establecimiento");

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
            dto.setEstablecimiento(
                    estudiante.getEstablecimiento() != null ? estudiante.getEstablecimiento().getNombre()
                            : "Sin establecimiento");

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
            dto.setEstablecimiento(
                    profesor.getEstablecimiento() != null ? profesor.getEstablecimiento().getNombre()
                            : "Sin establecimiento");

            return dto;
        }

        // No se encontró
        return null;
    }

    public void agregarPersona(PersonaDTO personaDTO) {
    Establecimiento est = establecimientoRepository.findByNombre(personaDTO.getEstablecimiento());

    
    if (personaDTO.getTipo().equalsIgnoreCase("Estudiante")) {
        Estudiante estudiante = new Estudiante();
        estudiante.setRut(personaDTO.getRut());
        estudiante.setNombre(personaDTO.getNombre());
        estudiante.setApellido(personaDTO.getApellido());
        estudiante.setTelefono(personaDTO.getTelefono());
        estudiante.setCurso(personaDTO.getCurso());
        estudiante.setFechaNacimiento(personaDTO.getFechaNacimiento());
        estudiante.setEstablecimiento(est);
        estudianteRepository.save(estudiante);
        
    } else if (personaDTO.getTipo().equalsIgnoreCase("Profesor")) {
        Profesor profesor = new Profesor();
        profesor.setRut(personaDTO.getRut());
        profesor.setNombre(personaDTO.getNombre());
        profesor.setApellido(personaDTO.getApellido());
        profesor.setTelefono(personaDTO.getTelefono());
        profesor.setAsignatura(personaDTO.getAsignatura());
        profesor.setEstablecimiento(est);
        profesorRepository.save(profesor);
    }
}
}
