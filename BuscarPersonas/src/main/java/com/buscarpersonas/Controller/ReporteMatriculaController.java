package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.MatriculasComunalesDTO;
import com.buscarpersonas.repository.EstudianteRepository;
import com.buscarpersonas.service.MatriculasPDFService;
import com.buscarpersonas.service.ReporteMatriculaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteMatriculaController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReporteMatriculaController.class);

    @Autowired
    private ReporteMatriculaService reporteService; // Para PDF por comuna específica

    @Autowired
    private MatriculasPDFService matriculasPDFService; // Para PDF general por comunas

    @Autowired
    private EstudianteRepository estudianteRepository;

    /**
     * Generar reporte PDF por comuna específica
     * URL: /api/reportes/matriculas-comunales/pdf?comuna=Chillán
     */
    @GetMapping("/matriculas-comunales/pdf")
    public ResponseEntity<byte[]> generarReporteMatriculasPorComuna(@RequestParam String comuna) {
        try {
            logger.info("Generando reporte PDF para comuna: {}", comuna);
            
            ByteArrayOutputStream baos = reporteService.generarReporteMatriculasPDF(comuna);
            byte[] pdfBytes = baos.toByteArray();
            
            // Verificar que se generó contenido
            if (pdfBytes.length == 0) {
                logger.error("El PDF generado está vacío para comuna: {}", comuna);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "reporte_matriculas_" + comuna + "_" + timestamp + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nombreArchivo);
            headers.setContentLength(pdfBytes.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            logger.info("PDF generado correctamente para comuna {}: {} bytes", comuna, pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            logger.error("Error al generar reporte para comuna: " + comuna, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("{\"error\":\"Error al generar reporte: " + e.getMessage() + "\"}").getBytes());
        }
    }

    /**
     * Generar certificado comunal - ESTE ES EL ENDPOINT QUE ANGULAR ESTÁ LLAMANDO
     * URL: /api/reportes/matriculas-comunales/certificado?comuna=Chillán
     */
    @GetMapping("/matriculas-comunales/certificado")
    public ResponseEntity<byte[]> generarCertificadoComunal(@RequestParam String comuna) {
        try {
            logger.info("Generando certificado comunal para: {}", comuna);
            
            // Usar el mismo servicio que para el PDF por comuna
            ByteArrayOutputStream baos = reporteService.generarReporteMatriculasPDF(comuna);
            byte[] pdfBytes = baos.toByteArray();
            
            // Verificar que se generó contenido
            if (pdfBytes.length == 0) {
                logger.error("El certificado generado está vacío para comuna: {}", comuna);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "certificado_comunal_" + comuna + "_" + timestamp + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nombreArchivo);
            headers.setContentLength(pdfBytes.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            logger.info("Certificado generado correctamente para {}: {} bytes", comuna, pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            logger.error("Error al generar certificado para comuna: " + comuna, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("{\"error\":\"Error al generar certificado: " + e.getMessage() + "\"}").getBytes());
        }
    }

    /**
     * Generar informe general de todas las comunas
     * URL: /api/reportes/matriculas-comunales/informe-general
     */
    @GetMapping("/matriculas-comunales/informe-general")
    public ResponseEntity<byte[]> descargarInformeMatriculasGeneral() {
        try {
            logger.info("Solicitud de informe general de matrículas comunales");

            byte[] pdfBytes = matriculasPDFService.generarInformeMatriculasComunales();
            
            if (pdfBytes.length == 0) {
                logger.error("El informe general generado está vacío");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "informe_matriculas_comunales_" + timestamp + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nombreArchivo);
            headers.setContentLength(pdfBytes.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            logger.info("PDF general generado correctamente: {} ({} bytes)", nombreArchivo, pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            logger.error("Error al generar informe general de matrículas comunales", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("{\"error\":\"Error al generar informe general: " + e.getMessage() + "\"}").getBytes());
        }
    }

    /**
     * Datos de matrículas comunales en JSON
     */
    @GetMapping("/matriculas-comunales/datos")
    public ResponseEntity<Map<String, Object>> obtenerDatosMatriculasComunales() {
        try {
            logger.info("Obteniendo datos de matrículas comunales...");

            List<MatriculasComunalesDTO> datosMatriculas = estudianteRepository.obtenerMatriculasPorComuna();
            Long totalEstudiantes = estudianteRepository.obtenerTotalEstudiantes();
            Long totalComunas = estudianteRepository.obtenerTotalComunas();

            Map<String, Object> response = new HashMap<>();
            response.put("datosMatriculas", datosMatriculas);
            response.put("totalEstudiantes", totalEstudiantes);
            response.put("totalComunas", totalComunas);
            response.put("fechaGeneracion", LocalDateTime.now());
            response.put("status", "success");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error al obtener datos de matrículas comunales", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("mensaje", "Error al obtener los datos: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Health check
     */
    @GetMapping("/matriculas-comunales/health")
    public ResponseEntity<Map<String, String>> verificarSalud() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("servicio", "Matrículas Comunales");
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
}