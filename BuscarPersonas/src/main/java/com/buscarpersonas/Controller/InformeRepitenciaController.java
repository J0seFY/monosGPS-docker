package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.EstudianteRepitenciaDTO;
import com.buscarpersonas.service.InformeRepitenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/informes")
public class InformeRepitenciaController {

    @Autowired
    private InformeRepitenciaService informeRepitenciaService;

    @GetMapping("/repitencia/pdf")
    public ResponseEntity<byte[]> generarInformePDF(
            @RequestParam(value = "promedioMinimo", defaultValue = "4.0") BigDecimal promedioMinimo,
            @RequestParam(value = "asistenciaMinima", defaultValue = "85") BigDecimal asistenciaMinima) {
        
        try {
            byte[] pdfBytes = informeRepitenciaService.generarInformeRepitenciaPDF(promedioMinimo, asistenciaMinima);
            
            String fileName = "informe_repitencia_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/repitencia/estudiantes")
    public ResponseEntity<List<EstudianteRepitenciaDTO>> obtenerEstudiantesConRiesgo(
            @RequestParam(value = "promedioMinimo", defaultValue = "4.0") BigDecimal promedioMinimo,
            @RequestParam(value = "asistenciaMinima", defaultValue = "85") BigDecimal asistenciaMinima) {
        
        try {
            List<EstudianteRepitenciaDTO> estudiantes = informeRepitenciaService
                    .obtenerEstudiantesConRiesgo(promedioMinimo, asistenciaMinima);
            
            return new ResponseEntity<>(estudiantes, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/repitencia/vista-previa")
    public ResponseEntity<java.util.Map<String, Object>> obtenerVistaPrevia(
            @RequestParam(value = "promedioMinimo", defaultValue = "4.0") BigDecimal promedioMinimo,
            @RequestParam(value = "asistenciaMinima", defaultValue = "85") BigDecimal asistenciaMinima) {
        
        try {
            List<EstudianteRepitenciaDTO> estudiantes = informeRepitenciaService
                    .obtenerEstudiantesConRiesgo(promedioMinimo, asistenciaMinima);
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("totalEstudiantes", estudiantes.size());
            response.put("criterios", java.util.Map.of(
                "promedioMinimo", promedioMinimo,
                "asistenciaMinima", asistenciaMinima
            ));
            response.put("estudiantes", estudiantes.size() > 10 ? estudiantes.subList(0, 10) : estudiantes);
            response.put("hayMasEstudiantes", estudiantes.size() > 10);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}