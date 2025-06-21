package com.buscarpersonas.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.buscarpersonas.dto.PersonaDTO;
import com.buscarpersonas.service.PersonaService;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService service) {
        this.personaService = service;
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<Map<String, List<?>>> buscarPorNombre(@PathVariable String nombre) {
        Map<String, List<?>> response = Map.of("personas", personaService.buscarPorNombre(nombre));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> buscarPorId(@PathVariable String id) {
        PersonaDTO persona = personaService.buscarPorRut(id);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<Map<String, String>> agregarPersona(@RequestBody PersonaDTO personaDTO) {
        personaService.agregarPersona(personaDTO);
        Map<String, String> response = Map.of("mensaje", "Persona agregada exitosamente");
        return ResponseEntity.ok(response);
    }
}
