package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.RendimientoDTO;
import com.buscarpersonas.service.ReporteRendimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes/rendimiento")
public class ReporteRendimientoController {

    @Autowired
    private ReporteRendimientoService reporteRendimientoService;

    @GetMapping("/comunal")
    public ResponseEntity<byte[]> generarReporteComunal() {
        try {
            List<RendimientoDTO> datos = reporteRendimientoService.obtenerDatosComunal();
            byte[] pdf = reporteRendimientoService.generarPDFRendimiento(datos, "Reporte de Rendimiento Comunal");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("rendimiento_comunal.pdf")
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generando PDF: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/establecimiento")
    public ResponseEntity<byte[]> generarReportePorEstablecimiento(@RequestParam String nombre) {
        try {
            List<RendimientoDTO> datos = reporteRendimientoService.obtenerDatosPorEstablecimiento(nombre);
            byte[] pdf = reporteRendimientoService.generarPDFRendimiento(datos, "Reporte por Establecimiento: " + nombre);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("rendimiento_establecimiento_" + nombre + ".pdf")
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generando PDF: " + e.getMessage()).getBytes());
        }
    }
}
