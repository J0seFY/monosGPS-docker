package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteEstudianteExtranjeroService;
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
public class ReporteAlumnosExtranjerosController {
    
    @Autowired
    private ReporteEstudianteExtranjeroService reporteEstudianteExtranjeroService;
    
    @GetMapping("/estudiantes-extranjeros/pdf")
    public ResponseEntity<byte[]> descargarReporteEstudiantesExtranjeros() {
        try {
            byte[] pdfData = reporteEstudianteExtranjeroService.generarReporteEstudiantesExtranjeros();
            
            String filename = "Informe_Estudiantes_Extranjeros_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfData.length);
            
            return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/estudiantes-extranjeros/count")
    public ResponseEntity<Integer> contarEstudiantesExtranjeros() {
        try {
            // Este endpoint adicional te permite consultar solo el número sin generar el PDF
            byte[] pdfData = reporteEstudianteExtranjeroService.generarReporteEstudiantesExtranjeros();
            // En una implementación más eficiente, podrías crear un método específico para contar
            return ResponseEntity.ok(0); // Placeholder - implementar método específico si es necesario
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
