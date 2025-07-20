package com.materialesservice.Controller;


import com.materialesservice.Service.MaterialDescargableService;
import com.materialesservice.entity.MaterialDescargableDTO;
import com.materialesservice.enumeraciones.Curso;
import com.materialesservice.enumeraciones.NivelEducativo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/materiales")
@CrossOrigin(origins = "*")
public class MaterialDescargableController {

    @Autowired
    private MaterialDescargableService service;

    @PostMapping(value = "/subir", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirArchivo(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("nivelEducativo") NivelEducativo nivelEducativo,
            @RequestParam("curso") Curso curso) {

        try {
            MaterialDescargableDTO resultado = service.subirArchivo(archivo, nivelEducativo, curso);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir archivo: " + e.getMessage());
        }
    }

    @GetMapping("/listar/nivel/{nivel}/curso/{curso}")
    public ResponseEntity<List<MaterialDescargableDTO>> listarPorNivelYCurso(
            @PathVariable NivelEducativo nivel,
            @PathVariable Curso curso) {
        List<MaterialDescargableDTO> archivos = service.obtenerArchivosPorNivelYCurso(nivel, curso);
        return ResponseEntity.ok(archivos);
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) {
        try {
            Resource archivo = service.descargarArchivo(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + archivo.getFilename() + "\"")
                    .body(archivo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}