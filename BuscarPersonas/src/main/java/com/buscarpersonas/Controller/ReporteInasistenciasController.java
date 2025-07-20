package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteInasistenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportesInasistencias")
public class ReporteInasistenciasController {

    @Autowired
    private ReporteInasistenciasService service;

    @GetMapping("/inasistencias/comunal")
    public ResponseEntity<byte[]> descargarComunal() {
        byte[] contenido = service.generarPDFComunal().readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_inasistencias_comunal.pdf");

        return ResponseEntity.ok().headers(headers).body(contenido);
    }

    @GetMapping("/inasistencias/establecimiento/{id}")
    public ResponseEntity<byte[]> descargarPorEstablecimiento(@PathVariable("id") int id) {
        byte[] contenido = service.generarPDFPorEstablecimiento(id).readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_inasistencias_establecimiento.pdf");

        return ResponseEntity.ok().headers(headers).body(contenido);
    }
}
