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

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }
}
