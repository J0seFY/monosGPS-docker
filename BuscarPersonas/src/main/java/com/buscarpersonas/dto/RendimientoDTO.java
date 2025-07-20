package com.buscarpersonas.dto;

public class RendimientoDTO {
    private String nombreEstablecimiento;
    private String nombreEstudiante;
    private double promedioNotas;

    public RendimientoDTO() {}

    public RendimientoDTO(String nombreEstablecimiento, String nombreEstudiante, double promedioNotas) {
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.nombreEstudiante = nombreEstudiante;
        this.promedioNotas = promedioNotas;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public double getPromedioNotas() {
        return promedioNotas;
    }

    public void setPromedioNotas(double promedioNotas) {
        this.promedioNotas = promedioNotas;
    }
}
