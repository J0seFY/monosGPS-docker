// controller/ReporteMatriculaController.java
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

    @GetMapping("/matriculas-comunales/pdf")
    public ResponseEntity<byte[]> generarReporteMatriculasPDF(@RequestParam String comuna) {
        try {
            ByteArrayOutputStream baos = reporteService.generarReporteMatriculasPDF(comuna);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "attachment; filename=reporte_matriculas_" + comuna + ".pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar reporte: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/matriculas-comunales/pdf")
    public ResponseEntity<byte[]> descargarInformeMatriculasPDF() {
        try {
            logger.info("Solicitud de informe general de matrículas comunales");

            byte[] pdfBytes = matriculasPDFService.generarInformeMatriculasComunales();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "informe_matriculas_comunales_" + timestamp + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nombreArchivo);
            headers.setContentLength(pdfBytes.length);

            logger.info("PDF generado correctamente: {}", nombreArchivo);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            logger.error("Error al generar informe general de matrículas comunales", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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