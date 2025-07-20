package com.buscarpersonas.dto;

public class ReporteRendimientoDTO {

    private String comuna;
    private String establecimiento;
    private Double promedio;

    public ReporteRendimientoDTO(String comuna, String establecimiento, Double promedio) {
        this.comuna = comuna;
        this.establecimiento = establecimiento;
        this.promedio = promedio;
    }

    // Getters y setters
}
