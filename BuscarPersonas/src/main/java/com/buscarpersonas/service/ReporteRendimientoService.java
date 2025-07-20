package com.buscarpersonas.service;

import com.buscarpersonas.dto.ReporteRendimientoDTO;
import com.buscarpersonas.repository.ReporteRendimientoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteRendimientoService {

    private final ReporteRendimientoRepository repository;

    public ReporteRendimientoService(ReporteRendimientoRepository repository) {
        this.repository = repository;
    }

    public List<ReporteRendimientoDTO> generarReporte() {
        return repository.obtenerPromedioRendimientoPorComunaYEstablecimiento()
                .stream()
                .map(obj -> new ReporteRendimientoDTO(
                        (String) obj[0],
                        (String) obj[1],
                        ((Number) obj[2]).doubleValue()
                ))
                .collect(Collectors.toList());
    }
}
