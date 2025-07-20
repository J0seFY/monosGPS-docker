package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.ReporteMatriculaColegioDTO;
import com.buscarpersonas.service.ReporteMatriculaColegioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes/matricula")
public class ReporteMatriculaColegioController {

    @Autowired
    private ReporteMatriculaColegioService reporteMatriculaColegioService;

    @GetMapping("/establecimiento/{establecimientoId}")
    public ResponseEntity<ReporteMatriculaColegioDTO> obtenerReporteMatricula(@PathVariable Long establecimientoId) {
        try {
            ReporteMatriculaColegioDTO reporte = reporteMatriculaColegioService.obtenerDatosReporteMatricula(establecimientoId);
            return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/establecimiento/{establecimientoId}/pdf")
    public ResponseEntity<byte[]> descargarReporteMatriculaPDF(@PathVariable Long establecimientoId) {
        try {
            byte[] pdfContent = reporteMatriculaColegioService.generarPDFMatricula(establecimientoId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_matricula_establecimiento_" + establecimientoId + ".pdf");
            headers.setContentLength(pdfContent.length);
            
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
