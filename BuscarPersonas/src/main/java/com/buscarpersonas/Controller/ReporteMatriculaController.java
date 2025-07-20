package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.MatriculasComunalesDTO;
import com.buscarpersonas.dto.ReporteMatriculaDTO;
import com.buscarpersonas.repository.EstudianteRepository;
import com.buscarpersonas.service.MatriculasPDFService;
import com.buscarpersonas.service.ReporteMatriculaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteMatriculaController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReporteMatriculaController.class);

    @Autowired
    private ReporteMatriculaService reporteService; // Para PDF por comuna específica

    @Autowired
    private MatriculasPDFService matriculasPDFService; // Para PDF general por comunas

    @Autowired
    private EstudianteRepository estudianteRepository;

    /* ********************** */
    /* Funcionalidades existentes (NO MODIFICAR) */
    /* ********************** */

    // [Todas tus funciones existentes permanecen exactamente igual]
    // ... (generarReporteMatriculasPorComuna, generarCertificadoComunal, etc.)
    // No las repito aquí para mantener el código conciso, pero deben permanecer

    /* ********************** */
    /* Nuevas funcionalidades */
    /* ********************** */

    /**
     * Obtiene el reporte de matrícula en formato JSON para un establecimiento
     */
    @GetMapping("/matricula/establecimiento/{establecimientoId}")
    public ResponseEntity<?> obtenerReporteMatricula(@PathVariable Long establecimientoId) {
        try {
            logger.info("Solicitando reporte de matrícula para establecimiento ID: {}", establecimientoId);
            
            ReporteMatriculaDTO reporte = reporteService.generarReporteMatricula(establecimientoId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reporte generado exitosamente");
            response.put("data", reporte);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            logger.error("Error al generar reporte de matrícula: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al generar el reporte: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            logger.error("Error interno al generar reporte de matrícula", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Descarga el reporte de matrícula en formato PDF para un establecimiento
     */
    @GetMapping("/matricula/establecimiento/{establecimientoId}/pdf")
    public ResponseEntity<?> descargarReporteMatriculaPdf(@PathVariable Long establecimientoId) {
        try {
            logger.info("Solicitando descarga de PDF de matrícula para establecimiento ID: {}", establecimientoId);
            
            byte[] pdfBytes = reporteService.generarPdfReporteMatricula(establecimientoId);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("reporte_matricula_establecimiento_%d_%s.pdf", 
                establecimientoId, timestamp);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            logger.info("PDF generado exitosamente. Tamaño: {} bytes, Archivo: {}", 
                pdfBytes.length, filename);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
                
        } catch (RuntimeException e) {
            logger.error("Error al generar PDF de matrícula: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al generar el PDF: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (DocumentException | IOException e) {
            logger.error("Error al generar documento PDF", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al generar el documento PDF");
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            
        } catch (Exception e) {
            logger.error("Error interno al generar PDF de matrícula", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtiene estadísticas básicas del establecimiento
     */
    @GetMapping("/matricula/establecimiento/{establecimientoId}/estadisticas")
    public ResponseEntity<?> obtenerEstadisticasEstablecimiento(@PathVariable Long establecimientoId) {
        try {
            logger.info("Solicitando estadísticas para establecimiento ID: {}", establecimientoId);
            
            Optional<Object[]> estadisticas = reporteService.obtenerEstadisticasEstablecimiento(establecimientoId);
            
            if (estadisticas.isPresent()) {
                Object[] stats = estadisticas.get();
                
                Map<String, Object> estadisticasMap = new HashMap<>();
                estadisticasMap.put("nombreEstablecimiento", stats[0]);
                estadisticasMap.put("totalEstudiantes", stats[1]);
                estadisticasMap.put("totalCursos", stats[2]);
                estadisticasMap.put("estudiantesActivos", stats[3]);
                estadisticasMap.put("estudiantesInactivos", stats[4]);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Estadísticas obtenidas exitosamente");
                response.put("data", estadisticasMap);
                response.put("timestamp", LocalDateTime.now());
                
                return ResponseEntity.ok(response);
                
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "No se encontró el establecimiento con ID: " + establecimientoId);
                errorResponse.put("timestamp", LocalDateTime.now());
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas del establecimiento", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}