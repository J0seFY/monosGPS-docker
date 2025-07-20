// ReporteMatriculaController.java
package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteMatriculaColegioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportesPorcolegio/Colegio")
public class ReporteMatriculaColegioController {

    @Autowired
    private ReporteMatriculaColegioService reporteService;

    @GetMapping("/matricula/{idEstablecimiento}")
    public ResponseEntity<InputStreamResource> descargarReporteMatricula(@PathVariable Integer idEstablecimiento) {
        var pdfStream = reporteService.generarPdfPorEstablecimiento(idEstablecimiento);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_matricula.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}
