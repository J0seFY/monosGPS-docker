package com.buscarpersonas.TestService;

import com.buscarpersonas.dto.PersonaDTO;
import com.buscarpersonas.service.PersonaService;
import com.buscarpersonas.Entity.Estudiante;
import com.buscarpersonas.Entity.Profesor;
import com.buscarpersonas.Entity.Establecimiento;
import com.buscarpersonas.repository.EstudianteRepository;
import com.buscarpersonas.repository.ProfesorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonaServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private PersonaService personaService;

    // Helper para crear estudiantes
    private Estudiante crearEstudiante(String rut, String nombre, String apellido, String establecimientoNombre) {
        Establecimiento establecimiento = new Establecimiento();
        establecimiento.setId(1L);
        establecimiento.setNombre(establecimientoNombre);

        Estudiante estudiante = new Estudiante();
        estudiante.setRut(rut);
        estudiante.setNombre(nombre);
        estudiante.setApellido(apellido);
        estudiante.setTelefono("987654321");
        estudiante.setCurso("3ro Medio");
        estudiante.setEstablecimiento(establecimiento);

        return estudiante;
    }

    // Helper para crear profesores
    private Profesor crearProfesor(String rut, String nombre, String apellido, String establecimientoNombre) {
        Establecimiento establecimiento = new Establecimiento();
        establecimiento.setId(1L);
        establecimiento.setNombre(establecimientoNombre);

        Profesor profesor = new Profesor();
        profesor.setRut(rut);
        profesor.setNombre(nombre);
        profesor.setApellido(apellido);
        profesor.setTelefono("123456789");
        profesor.setAsignatura("Matemáticas");
        profesor.setEstablecimiento(establecimiento);

        return profesor;
    }

    @Test
    public void buscarPorNombre_DebeDevolverListaDePersonas() {
        // Datos de prueba
        String nombre = "Ana";

        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(crearEstudiante("123", "Ana", "Luis", "Colegio San José"));

        List<Profesor> profesores = new ArrayList<>();
        profesores.add(crearProfesor("456", "Ana", "Martínez", "Liceo Santa María"));

        when(estudianteRepository.findByNombreContainingIgnoreCase(nombre)).thenReturn(estudiantes);
        when(profesorRepository.findByNombreContainingIgnoreCase(nombre)).thenReturn(profesores);

        List<PersonaDTO> resultado = personaService.buscarPorNombre(nombre);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        PersonaDTO dtoEstudiante = resultado.get(0);
        assertEquals("Estudiante", dtoEstudiante.getTipo());
        assertEquals("123", dtoEstudiante.getRut());
        assertEquals("Colegio San José", dtoEstudiante.getEstablecimiento());

        PersonaDTO dtoProfesor = resultado.get(1);
        assertEquals("Profesor", dtoProfesor.getTipo());
        assertEquals("456", dtoProfesor.getRut());
        assertEquals("Liceo Santa María", dtoProfesor.getEstablecimiento());
    }

    @Test
    public void buscarPorRut_EstudianteEncontrado() {
        // Datos de prueba
        String rut = "123";
        Estudiante estudiante = crearEstudiante(rut, "Juan", "Pérez", "Colegio San José");

        when(estudianteRepository.findByRut(rut)).thenReturn(estudiante);

        PersonaDTO resultado = personaService.buscarPorRut(rut);

        assertNotNull(resultado);
        assertEquals("Estudiante", resultado.getTipo());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Colegio San José", resultado.getEstablecimiento());
    }

    @Test
    public void buscarPorRut_ProfesorEncontrado() {
        // Datos de prueba
        String rut = "456";
        Profesor profesor = crearProfesor(rut, "María", "González", "Liceo Santa María");

        when(estudianteRepository.findByRut(rut)).thenReturn(null);
        when(profesorRepository.findByRut(rut)).thenReturn(profesor);

        PersonaDTO resultado = personaService.buscarPorRut(rut);

        assertNotNull(resultado);
        assertEquals("Profesor", resultado.getTipo());
        assertEquals("María", resultado.getNombre());
        assertEquals("Liceo Santa María", resultado.getEstablecimiento());
    }

    @Test
    public void buscarPorRut_NoEncontrado() {
        String rut = "999";

        when(estudianteRepository.findByRut(rut)).thenReturn(null);
        when(profesorRepository.findByRut(rut)).thenReturn(null);

        PersonaDTO resultado = personaService.buscarPorRut(rut);

        assertNull(resultado);
    }
}