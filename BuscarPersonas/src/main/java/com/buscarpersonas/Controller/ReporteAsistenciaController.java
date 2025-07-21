package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteAsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes/asistencia")
public class ReporteAsistenciaController {

    @Autowired
    private ReporteAsistenciaService reporteAsistenciaService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generarPDF(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            byte[] pdf = reporteAsistenciaService.generarReporteAsistencia(fecha);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("asistencia_" + fecha + ".pdf")
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generando PDF: " + e.getMessage()).getBytes());
        }
    }
}
