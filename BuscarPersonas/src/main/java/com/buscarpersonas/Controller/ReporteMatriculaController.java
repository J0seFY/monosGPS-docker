// controller/ReporteMatriculaController.java
package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteMatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/reportes")
public class ReporteMatriculaController {
    
    @Autowired
    private ReporteMatriculaService reporteService;
    
    @GetMapping("/matriculas-comunales/pdf")
    public ResponseEntity<byte[]> generarReporteMatriculasPDF(@RequestParam String comuna) {
        try {
            ByteArrayOutputStream baos = reporteService.generarReporteMatriculasPDF(comuna);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "attachment; filename=reporte_matriculas_" + comuna + ".pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar reporte: " + e.getMessage()).getBytes());
        }
    }
}