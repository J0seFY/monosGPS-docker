package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteSimceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/reportes")
public class ReporteSimceController {

    private final ReporteSimceService reporteSimceService;

    public ReporteSimceController(ReporteSimceService reporteSimceService) {
        this.reporteSimceService = reporteSimceService;
    }

    @GetMapping("/simce")
    public ResponseEntity<byte[]> descargarReporteSimce(@RequestParam String comuna) {
        ByteArrayInputStream bis = reporteSimceService.generarReporteSimcePdf(comuna);

        byte[] pdfBytes = bis.readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_SIMCE_" + comuna + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
