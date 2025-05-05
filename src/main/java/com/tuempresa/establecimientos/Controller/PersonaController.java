package com.tuempresa.establecimientos.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuempresa.establecimientos.dto.PersonaDTO;
import com.tuempresa.establecimientos.service.PersonaService;

@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*") // habilita CORS
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService service) {
        this.personaService = service;
    }

    @GetMapping("/buscar")
    public ResponseEntity<Map<String, List<?>>> buscarPorNombre(@RequestParam String nombre) {
        Map<String, List<?>> response = Map.of("personas", personaService.buscarPorNombre(nombre));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> buscarPorId(@PathVariable Long id) {
        PersonaDTO persona = personaService.buscarPorRut(id.toString());
        if (persona != null) {
            return ResponseEntity.ok(persona);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

