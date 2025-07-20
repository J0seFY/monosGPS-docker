 package com.buscarpersonas.dto;

import java.math.BigDecimal;

public class RendimientoAsignaturaDTO {
    private String estudianteRut;
    private String nombreCompleto;
    private String asignatura;
    private BigDecimal promedioNotas;
    private Integer cantidadNotas;
    private BigDecimal notaMaxima;
    private BigDecimal notaMinima;
    private String nombreEstablecimiento;

    public RendimientoAsignaturaDTO() {}

    public RendimientoAsignaturaDTO(String estudianteRut, String nombre, String apellido, 
                                   String asignatura, BigDecimal promedioNotas, 
                                   Integer cantidadNotas, BigDecimal notaMaxima, 
                                   BigDecimal notaMinima, String nombreEstablecimiento) {
        this.estudianteRut = estudianteRut;
        this.nombreCompleto = nombre + " " + apellido;
        this.asignatura = asignatura;
        this.promedioNotas = promedioNotas;
        this.cantidadNotas = cantidadNotas;
        this.notaMaxima = notaMaxima;
        this.notaMinima = notaMinima;
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    // Getters y Setters
    public String getEstudianteRut() { return estudianteRut; }
    public void setEstudianteRut(String estudianteRut) { this.estudianteRut = estudianteRut; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

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

    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }
}