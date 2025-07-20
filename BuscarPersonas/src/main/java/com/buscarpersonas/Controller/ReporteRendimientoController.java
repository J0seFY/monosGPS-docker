package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.ReporteRendimientoDTO;
import com.buscarpersonas.service.ReporteRendimientoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/reportesRendimiento")
public class ReporteRendimientoController {

    private final ReporteRendimientoService service;

    public ReporteRendimientoController(ReporteRendimientoService service) {
        this.service = service;
    }

    @GetMapping("/api/reportes/rendimiento")
    public List<ReporteRendimientoDTO> obtenerReporte() {
        return service.generarReporte();
    }
}
