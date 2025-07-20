package com.buscarpersonas.Controller;

import com.buscarpersonas.service.ReporteFuncionariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteFuncionariosController {

    @Autowired
    private ReporteFuncionariosService service;

    @GetMapping("/funcionarios/comunal")
    public ResponseEntity<byte[]> descargarReporteComunal() {
        byte[] contenido = service.generarPDFComunal().readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_funcionarios_comunal.pdf");

        return ResponseEntity.ok().headers(headers).body(contenido);
    }

    @GetMapping("/funcionarios/establecimiento/{id}")
    public ResponseEntity<byte[]> descargarPorEstablecimiento(@PathVariable("id") int establecimientoId) {
        byte[] contenido = service.generarPDFPorEstablecimiento(establecimientoId).readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_funcionarios_establecimiento.pdf");

        return ResponseEntity.ok().headers(headers).body(contenido);
    }
}
