package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.ReporteSimceDTO;
import com.buscarpersonas.service.ReporteSimceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import javax.validation.constraints.NotNull;
// import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la generación de reportes SIMCE
 */
@RestController
@RequestMapping("/api/reportes")
public class ReporteSimceController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReporteSimceController.class);
    
    @Autowired
    private ReporteSimceService reporteSimceService;
    
    /**
     * Genera y descarga el reporte SIMCE en formato PDF para un establecimiento específico
     * 
     * @param establecimientoId ID del establecimiento
     * @return ResponseEntity con el archivo PDF
     */
    @GetMapping("/simce")
    public ResponseEntity<?> generarReporteSimce(
            @RequestParam Long establecimientoId) {
        
        logger.info("Solicitud de reporte SIMCE para establecimiento ID: {}", establecimientoId);

        if (establecimientoId == null || establecimientoId <= 0) {
            logger.warn("ID de establecimiento inválido: {}", establecimientoId);
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("VALIDATION_ERROR", "El ID del establecimiento debe ser un número positivo y no nulo."));
        }
        
        try {
            // Generar el PDF
            byte[] pdfBytes = reporteSimceService.generarReporteSimcePDF(establecimientoId);
            
            // Crear nombre del archivo con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("Reporte_SIMCE_Establecimiento_%d_%s.pdf", establecimientoId, timestamp);
            
            // Configurar headers para la descarga
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfBytes.length);
            
            logger.info("Reporte SIMCE generado exitosamente. Archivo: {}, Tamaño: {} bytes", 
                       fileName, pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ByteArrayResource(pdfBytes));
                    
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("VALIDATION_ERROR", e.getMessage()));
                    
        } catch (Exception e) {
            logger.error("Error al generar reporte SIMCE para establecimiento {}: {}", 
                        establecimientoId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", 
                          "Error interno del servidor al generar el reporte"));
        }
    }
    
    /**
     * Obtiene los datos del reporte SIMCE en formato JSON (sin generar PDF)
    public ResponseEntity<?> obtenerDatosReporteSimce(
            @RequestParam Long establecimientoId) {
        
        logger.info("Solicitud de datos de reporte SIMCE para establecimiento ID: {}", establecimientoId);

        if (establecimientoId == null || establecimientoId <= 0) {
            logger.warn("ID de establecimiento inválido: {}", establecimientoId);
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("VALIDATION_ERROR", "El ID del establecimiento debe ser un número positivo y no nulo."));
        }
        
        try {
            // Aquí podrías implementar un método en el servicio que solo obtenga los datos
            // sin generar el PDF, si necesitas esta funcionalidad
        
        logger.info("Solicitud de datos de reporte SIMCE para establecimiento ID: {}", establecimientoId);
        
        try {
            // Aquí podrías implementar un método en el servicio que solo obtenga los datos
            // sin generar el PDF, si necesitas esta funcionalidad
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Funcionalidad de obtener datos disponible");
            response.put("establecimientoId", establecimientoId);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al obtener datos de reporte SIMCE para establecimiento {}: {}", 
                        establecimientoId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", 
                          "Error interno del servidor al obtener los datos"));
        }
    }
    
    /**
     * Obtiene un resumen de todos los establecimientos con datos SIMCE
     * 
     * @return ResponseEntity con la lista de reportes de todos los establecimientos
     */
    @GetMapping("/simce/resumen")
    public ResponseEntity<?> obtenerResumenReportesSimce() {
        
        logger.info("Solicitud de resumen de reportes SIMCE de todos los establecimientos");
        
        try {
            List<ReporteSimceDTO> reportes = reporteSimceService.obtenerTodosLosReportesSimce();
            
            Map<String, Object> response = new HashMap<>();
            response.put("totalEstablecimientos", reportes.size());
            response.put("reportes", reportes);
            response.put("timestamp", LocalDateTime.now());
            
            logger.info("Resumen de reportes SIMCE generado. Total establecimientos: {}", reportes.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al obtener resumen de reportes SIMCE: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", 
                          "Error interno del servidor al obtener el resumen"));
        }
    }
    
    /**
     * Genera reporte SIMCE consolidado de todos los establecimientos
     * 
     * @return ResponseEntity con el archivo PDF consolidado
     */
    @GetMapping("/simce/consolidado")
    public ResponseEntity<?> generarReporteSimceConsolidado() {
        
        logger.info("Solicitud de reporte SIMCE consolidado");
        
        try {
            // Aquí podrías implementar la funcionalidad para generar un PDF consolidado
            // de todos los establecimientos si es necesario
            
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(createErrorResponse("NOT_IMPLEMENTED", 
                          "Funcionalidad de reporte consolidado no implementada aún"));
                          
        } catch (Exception e) {
            logger.error("Error al generar reporte SIMCE consolidado: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", 
                          "Error interno del servidor al generar el reporte consolidado"));
        }
    }
    
    /**
     * Endpoint de health check para verificar el estado del servicio
     * 
     * @return ResponseEntity con el estado del servicio
     */
    @GetMapping("/simce/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "ReporteSimceService");
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * Manejo de excepciones para parámetros inválidos
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("Parámetro inválido: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createErrorResponse("INVALID_PARAMETER", e.getMessage()));
    }
    
    /**
     * Manejo de excepciones generales
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        logger.error("Error no controlado: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("INTERNAL_SERVER_ERROR", 
                      "Error interno del servidor"));
    }
    
    /**
     * Crea una respuesta de error estandarizada
     */
    private Map<String, Object> createErrorResponse(String errorCode, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("errorCode", errorCode);
        error.put("message", message);
        error.put("timestamp", LocalDateTime.now());
        return error;
    }
}