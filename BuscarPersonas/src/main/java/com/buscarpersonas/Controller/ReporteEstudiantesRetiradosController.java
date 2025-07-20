package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteEstudiantesRetiradosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/reportes")
public class ReporteEstudiantesRetiradosController {

    private static final Logger logger = LoggerFactory.getLogger(ReporteEstudiantesRetiradosController.class);

    @Autowired
    private ReporteEstudiantesRetiradosService reporteService;

    /**
     * Endpoint para descargar el reporte completo de estudiantes retirados
     */
    @GetMapping("/estudiantes-retirados/pdf")
    public ResponseEntity<byte[]> descargarReporteEstudiantesRetirados() {
        try {
            logger.info("Solicitud de descarga de reporte de estudiantes retirados");

            byte[] pdfBytes = reporteService.generarReportePDF();

            // Generar nombre de archivo con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "estudiantes_retirados_" + timestamp + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            logger.info("Reporte de estudiantes retirados generado exitosamente: {}", filename);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error de negocio al generar reporte: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .header("X-Error-Message", e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.error("Error interno al generar reporte de estudiantes retirados: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Error interno del servidor")
                    .build();
        }
    }

    /**
     * Endpoint para descargar el reporte de estudiantes retirados por establecimiento
     */
    @GetMapping("/estudiantes-retirados/pdf/establecimiento/{establecimientoId}")
    public ResponseEntity<byte[]> descargarReporteEstudiantesRetiradosPorEstablecimiento(
            @PathVariable Long establecimientoId) {
        try {
            logger.info("Solicitud de descarga de reporte de estudiantes retirados para establecimiento: {}", establecimientoId);

            byte[] pdfBytes = reporteService.generarReportePDFPorEstablecimiento(establecimientoId);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "estudiantes_retirados_establecimiento_" + establecimientoId + "_" + timestamp + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            logger.info("Reporte de estudiantes retirados por establecimiento generado exitosamente: {}", filename);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error de negocio al generar reporte por establecimiento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .header("X-Error-Message", e.getMessage())
                    .build();
        } catch (Exception e) {
            logger.error("Error interno al generar reporte por establecimiento: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Error interno del servidor")
                    .build();
        }
    }

    /**
     * Endpoint para obtener el conteo de estudiantes retirados
     */
    @GetMapping("/estudiantes-retirados/count")
    public ResponseEntity<?> obtenerConteoEstudiantesRetirados() {
        try {
            logger.info("Solicitud de conteo de estudiantes retirados");
            Long count = reporteService.obtenerTotalEstudiantesRetirados();
            
            return ResponseEntity.ok().body(new ConteoResponse(count));
            
        } catch (Exception e) {
            logger.error("Error al obtener conteo de estudiantes retirados: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor"));
        }
    }

    /**
     * Endpoint para verificar disponibilidad del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Servicio de reportes operativo");
    }

    // Clases internas para respuestas
    public static class ConteoResponse {
        private Long total;
        
        public ConteoResponse(Long total) {
            this.total = total;
        }
        
        public Long getTotal() { return total; }
        public void setTotal(Long total) { this.total = total; }
    }

    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}