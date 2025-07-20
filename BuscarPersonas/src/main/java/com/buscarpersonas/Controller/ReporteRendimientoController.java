package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteRendimientoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportesRendimiento")
public class ReporteRendimientoController {

    private final ReporteRendimientoService service;

    public ReporteRendimientoController(ReporteRendimientoService service) {
        this.service = service;
    }

    @GetMapping("/rendimiento")
    public ResponseEntity<ByteArrayResource> obtenerReporteEnPDF() {
        try {
            byte[] pdfBytes = service.generarReportePDF();

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_rendimiento.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
