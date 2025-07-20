package com.buscarpersonas.dto;

import java.math.BigDecimal;

public class EstudianteRepitenciaDTO {
    private String rut;
    private String nombreCompleto;
    private String curso;
    private BigDecimal promedioNotas;
    private BigDecimal porcentajeAsistencia;
    private String nombreEstablecimiento;

    // Constructores
    public EstudianteRepitenciaDTO() {}

    public EstudianteRepitenciaDTO(String rut, String nombre, String apellido, String curso, 
                                   BigDecimal promedioNotas, BigDecimal porcentajeAsistencia, 
                                   String nombreEstablecimiento) {
        this.rut = rut;
        this.nombreCompleto = nombre + " " + apellido;
        this.curso = curso;
        this.promedioNotas = promedioNotas;
        this.porcentajeAsistencia = porcentajeAsistencia;
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    // Getters y Setters
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public BigDecimal getPromedioNotas() { return promedioNotas; }
    public void setPromedioNotas(BigDecimal promedioNotas) { this.promedioNotas = promedioNotas; }

    public BigDecimal getPorcentajeAsistencia() { return porcentajeAsistencia; }
    public void setPorcentajeAsistencia(BigDecimal porcentajeAsistencia) { this.porcentajeAsistencia = porcentajeAsistencia; }

    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }
}