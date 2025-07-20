package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteAccidentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportesAccidentes")
public class ReporteAccidentesController {

    @Autowired
    private ReporteAccidentesService service;

    @GetMapping("/accidentes")
    public ResponseEntity<byte[]> descargarReporteAccidentes() {
        byte[] contenido = service.generarReporteAccidentesPDF().readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_accidentes_comunal.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(contenido);
    }
}
