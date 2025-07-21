package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.ReporteSimcePaesDTO;
import com.buscarpersonas.service.ReporteSimcePaesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/reportes")
public class ReporteSimcePaesController {
    
    @Autowired
    private ReporteSimcePaesService reporteService;
    
    /**
     * Obtiene los datos del reporte SIMCE y PAES en formato JSON
     * @param establecimientoId ID del establecimiento
     * @return Datos del reporte en formato DTO
     */
    @GetMapping("/simce-paes/{establecimientoId}")
    public ResponseEntity<ReporteSimcePaesDTO> obtenerDatosReporte(
            @PathVariable Long establecimientoId) {
        try {
            ReporteSimcePaesDTO reporte = reporteService.obtenerDatosReporte(establecimientoId);
            return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Genera y descarga el reporte SIMCE y PAES en formato PDF
     * @param establecimientoId ID del establecimiento (opcional, si no se proporciona genera reporte general)
     * @return Archivo PDF para descarga
     */
    @GetMapping("/simce-paes/pdf")
    public ResponseEntity<byte[]> descargarReportePDF(
            @RequestParam(required = false) Long establecimientoId) {
        try {
            // Si no se proporciona establecimientoId, usar un valor por defecto o manejar error
            if (establecimientoId == null) {
                return ResponseEntity.badRequest()
                    .headers(createErrorHeaders("El parámetro establecimientoId es requerido"))
                    .build();
            }
            
            byte[] pdfBytes = reporteService.generarPDFReporte(establecimientoId);
            
            // Crear nombre del archivo con timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "reporte_simce_paes_establecimiento_" + establecimientoId + "_" + timestamp + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound()
                .headers(createErrorHeaders("Establecimiento no encontrado"))
                .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(createErrorHeaders("Error interno del servidor al generar el reporte"))
                .build();
        }
    }
    
    /**
     * Endpoint alternativo con establecimientoId en el path
     * @param establecimientoId ID del establecimiento
     * @return Archivo PDF para descarga
     */
    @GetMapping("/simce-paes/{establecimientoId}/pdf")
    public ResponseEntity<byte[]> descargarReportePDFPorPath(
            @PathVariable Long establecimientoId) {
        try {
            byte[] pdfBytes = reporteService.generarPDFReporte(establecimientoId);
            
            // Crear nombre del archivo con timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "reporte_simce_paes_establecimiento_" + establecimientoId + "_" + timestamp + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound()
                .headers(createErrorHeaders("Establecimiento no encontrado"))
                .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(createErrorHeaders("Error interno del servidor al generar el reporte"))
                .build();
        }
    }
    
    /**
     * Método auxiliar para crear headers de error
     */
    private HttpHeaders createErrorHeaders(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Error-Message", message);
        return headers;
    }
}