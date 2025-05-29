package com.buscarpersonas.TestRest;

import com.buscarpersonas.dto.PersonaDTO;
import com.buscarpersonas.Controller.PersonaController;
import com.buscarpersonas.service.PersonaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class personaControllerTest {

    @Mock
    private PersonaService personaService;

    @InjectMocks
    private PersonaController personaController;

    @Test
    public void buscarPorNombre_DevuelveListaDePersonas() {
        // Datos de prueba
        String nombre = "Ana";

        List<PersonaDTO> personas = new ArrayList<>();
        personas.add(new PersonaDTO("Estudiante", "123", "Ana", "Luis", "987654321", "Colegio San José"));
        personas.add(new PersonaDTO("Profesor", "456", "Ana", "Martínez", "123456789", "Liceo Santa María"));

        when(personaService.buscarPorNombre(nombre)).thenReturn(personas);

        ResponseEntity<Map<String, List<?>>> response = personaController.buscarPorNombre(nombre);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("personas"));
        assertEquals(2, response.getBody().get("personas").size());
    }

    @Test
    public void buscarPorId_Encontrado() {
        String id = "123";
        PersonaDTO dto = new PersonaDTO("Estudiante", "123", "Juan", "Pérez", "987654321", "Colegio San José");

        when(personaService.buscarPorRut(id)).thenReturn(dto);

        ResponseEntity<PersonaDTO> response = personaController.buscarPorId(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void buscarPorId_NoEncontrado() {
        String id = "999";
        when(personaService.buscarPorRut(id)).thenReturn(null);

        ResponseEntity<PersonaDTO> response = personaController.buscarPorId(id);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}