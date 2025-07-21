package com.buscarpersonas.dto;

import java.math.BigDecimal;

public class RendimientoAsignaturaDTO {
    private String estudianteRut;
    private String asignatura;
    private BigDecimal promedioNotas;
    private Integer cantidadNotas;
    private BigDecimal notaMaxima;
    private BigDecimal notaMinima;

    public RendimientoAsignaturaDTO() {}

    public RendimientoAsignaturaDTO(String estudianteRut, String asignatura, 
                                   BigDecimal promedioNotas, Integer cantidadNotas, 
                                   BigDecimal notaMaxima, BigDecimal notaMinima) {
        this.estudianteRut = estudianteRut;
        this.asignatura = asignatura;
        this.promedioNotas = promedioNotas;
        this.cantidadNotas = cantidadNotas;
        this.notaMaxima = notaMaxima;
        this.notaMinima = notaMinima;
    }

    // Getters y Setters
    public String getEstudianteRut() { return estudianteRut; }
    public void setEstudianteRut(String estudianteRut) { this.estudianteRut = estudianteRut; }

    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }

    public BigDecimal getPromedioNotas() { return promedioNotas; }
    public void setPromedioNotas(BigDecimal promedioNotas) { this.promedioNotas = promedioNotas; }

    public Integer getCantidadNotas() { return cantidadNotas; }
    public void setCantidadNotas(Integer cantidadNotas) { this.cantidadNotas = cantidadNotas; }

    public BigDecimal getNotaMaxima() { return notaMaxima; }
    public void setNotaMaxima(BigDecimal notaMaxima) { this.notaMaxima = notaMaxima; }

    public BigDecimal getNotaMinima() { return notaMinima; }
    public void setNotaMinima(BigDecimal notaMinima) { this.notaMinima = notaMinima; }
}