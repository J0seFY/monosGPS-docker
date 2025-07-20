package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.RendimientoReporteDTO;
import com.buscarpersonas.service.RendimientoReporteService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/rendimiento")
@CrossOrigin(origins = "*")
public class RendimientoController {
    
    @Autowired
    private RendimientoReporteService rendimientoReporteService;
    
    /**
     * Obtiene datos de rendimiento por establecimiento (JSON)
     */
    @GetMapping("/establecimiento/{id}")
    public ResponseEntity<List<RendimientoReporteDTO>> obtenerRendimientoPorEstablecimiento(
            @PathVariable("id") Long establecimientoId) {
        try {
            List<RendimientoReporteDTO> datos = rendimientoReporteService.obtenerRendimientoPorEstablecimiento(establecimientoId);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obtiene datos de rendimiento comunal (JSON)
     */
    @GetMapping("/comunal")
    public ResponseEntity<List<RendimientoReporteDTO>> obtenerRendimientoComunal(
            @RequestParam(value = "comuna", required = false) String comuna) {
        try {
            List<RendimientoReporteDTO> datos = rendimientoReporteService.obtenerRendimientoComunal(comuna);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obtiene datos de rendimiento con filtros personalizados
     */
    @GetMapping("/filtrado")
    public ResponseEntity<List<RendimientoReporteDTO>> obtenerRendimientoConFiltros(
            @RequestParam(value = "establecimientoId", required = false) Long establecimientoId,
            @RequestParam(value = "asignatura", required = false) String asignatura,
            @RequestParam(value = "curso", required = false) String curso,
            @RequestParam(value = "fechaInicio", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        try {
            // Si no se proporcionan fechas, usar un rango por defecto (último año)
            if (fechaInicio == null) {
                fechaInicio = LocalDate.now().minusYears(1);
            }
            if (fechaFin == null) {
                fechaFin = LocalDate.now();
            }
            
            List<RendimientoReporteDTO> datos = rendimientoReporteService.obtenerRendimientoConFiltros(
                    establecimientoId, asignatura, curso, fechaInicio, fechaFin);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Descarga reporte PDF de rendimiento por establecimiento
     */
    @GetMapping("/establecimiento/{id}/pdf")
    public ResponseEntity<byte[]> descargarPDFRendimientoEstablecimiento(
            @PathVariable("id") Long establecimientoId) {
        try {
            byte[] pdfBytes = rendimientoReporteService.generarPDFRendimientoEstablecimiento(establecimientoId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "reporte_rendimiento_establecimiento_" + establecimientoId + "_" + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (DocumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Descarga reporte PDF de rendimiento comunal
     */
    @GetMapping("/comunal/pdf")
    public ResponseEntity<byte[]> descargarPDFRendimientoComunal(
            @RequestParam(value = "comuna", required = false) String comuna) {
        try {
            byte[] pdfBytes = rendimientoReporteService.generarPDFRendimientoComunal(comuna);
            
            String fileName = "reporte_rendimiento_comunal";
            if (comuna != null && !comuna.isEmpty()) {
                fileName += "_" + comuna.replaceAll("\\s+", "_");
            }
            fileName += "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (DocumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Endpoints auxiliares para obtener listas de filtros
     */
    @GetMapping("/comunas")
    public ResponseEntity<List<String>> obtenerComunas() {
        try {
            List<String> comunas = rendimientoReporteService.obtenerComunas();
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/asignaturas")
    public ResponseEntity<List<String>> obtenerAsignaturas() {
        try {
            List<String> asignaturas = rendimientoReporteService.obtenerAsignaturas();
            return ResponseEntity.ok(asignaturas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}