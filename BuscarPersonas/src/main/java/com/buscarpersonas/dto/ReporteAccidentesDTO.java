package com.buscarpersonas.dto;

public class ReporteAccidentesDTO {
    private String comuna;
    private String establecimiento;
    private Long cantidadAccidentes;

    public ReporteAccidentesDTO(String comuna, String establecimiento, Long cantidadAccidentes) {
        this.comuna = comuna;
        this.establecimiento = establecimiento;
        this.cantidadAccidentes = cantidadAccidentes;
    }

    public String getComuna() { return comuna; }
    public String getEstablecimiento() { return establecimiento; }
    public Long getCantidadAccidentes() { return cantidadAccidentes; }
}
