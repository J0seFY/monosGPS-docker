package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.RendimientoAsignaturaDTO;
import com.buscarpersonas.service.InformeRendimientoService;
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
public class InformeRendimientoController {

    @Autowired
    private InformeRendimientoService informeRendimientoService;

    @GetMapping("/rendimiento/pdf")
    public ResponseEntity<byte[]> generarInformePDF(
            @RequestParam(value = "asignatura", required = false) String asignatura,
            @RequestParam(value = "promedioMinimo", required = false) BigDecimal promedioMinimo) {
        
        try {
            byte[] pdfBytes = informeRendimientoService.generarInformeRendimientoPDF(asignatura, promedioMinimo);
            
            String fileName = "informe_rendimiento_";
            if (asignatura != null) {
                fileName += asignatura.toLowerCase().replaceAll("\\s+", "_") + "_";
            }
            fileName += LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
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

    @GetMapping("/rendimiento/datos")
    public ResponseEntity<List<RendimientoAsignaturaDTO>> obtenerRendimientoPorAsignatura(
            @RequestParam(value = "asignatura", required = false) String asignatura,
            @RequestParam(value = "promedioMinimo", required = false) BigDecimal promedioMinimo) {
        
        try {
            List<RendimientoAsignaturaDTO> rendimientos = informeRendimientoService
                    .obtenerRendimientoPorAsignatura(asignatura, promedioMinimo);
            
            return new ResponseEntity<>(rendimientos, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rendimiento/vista-previa")
    public ResponseEntity<java.util.Map<String, Object>> obtenerVistaPrevia(
            @RequestParam(value = "asignatura", required = false) String asignatura,
            @RequestParam(value = "promedioMinimo", required = false) BigDecimal promedioMinimo) {
        
        try {
            List<RendimientoAsignaturaDTO> rendimientos = informeRendimientoService
                    .obtenerRendimientoPorAsignatura(asignatura, promedioMinimo);
            
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("totalRegistros", rendimientos.size());
            response.put("criterios", java.util.Map.of(
                "asignatura", asignatura != null ? asignatura : "Todas",
                "promedioMinimo", promedioMinimo != null ? promedioMinimo : "Sin filtro"
            ));
            response.put("rendimientos", rendimientos.size() > 15 ? rendimientos.subList(0, 15) : rendimientos);
            response.put("hayMasRegistros", rendimientos.size() > 15);
            
            // Calcular estadísticas básicas
            if (!rendimientos.isEmpty()) {
                double promedioGeneral = rendimientos.stream()
                        .mapToDouble(r -> r.getPromedioNotas().doubleValue())
                        .average()
                        .orElse(0.0);
                
                response.put("estadisticas", java.util.Map.of(
                    "promedioGeneral", Math.round(promedioGeneral * 10.0) / 10.0,
                    "totalAsignaturas", rendimientos.stream()
                            .map(RendimientoAsignaturaDTO::getAsignatura)
                            .distinct()
                            .count(),
                    "totalEstudiantes", rendimientos.stream()
                            .map(RendimientoAsignaturaDTO::getEstudianteRut)
                            .distinct()
                            .count()
                ));
            }
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rendimiento/asignaturas")
    public ResponseEntity<List<String>> obtenerAsignaturas() {
        try {
            List<String> asignaturas = informeRendimientoService.obtenerTodasLasAsignaturas();
            return new ResponseEntity<>(asignaturas, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}