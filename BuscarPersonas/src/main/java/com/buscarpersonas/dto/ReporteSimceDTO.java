package com.buscarpersonas.dto;

import java.time.LocalDate;

public class ReporteSimceDTO {
    private String rutEstudiante;
    private String nombreEstudiante;
    private String apellidoEstudiante;
    private String nombreEstablecimiento;
    private Integer puntajeSimce;
    private LocalDate fechaPrueba;

    // Constructor
    public ReporteSimceDTO(String rutEstudiante, String nombreEstudiante, String apellidoEstudiante,
                           String nombreEstablecimiento, Integer puntajeSimce, LocalDate fechaPrueba) {
        this.rutEstudiante = rutEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.apellidoEstudiante = apellidoEstudiante;
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.puntajeSimce = puntajeSimce;
        this.fechaPrueba = fechaPrueba;
    }

    // Getters y setters
    public String getRutEstudiante() { return rutEstudiante; }
    public void setRutEstudiante(String rutEstudiante) { this.rutEstudiante = rutEstudiante; }

    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }

    public String getApellidoEstudiante() { return apellidoEstudiante; }
    public void setApellidoEstudiante(String apellidoEstudiante) { this.apellidoEstudiante = apellidoEstudiante; }

    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }

    public Integer getPuntajeSimce() { return puntajeSimce; }
    public void setPuntajeSimce(Integer puntajeSimce) { this.puntajeSimce = puntajeSimce; }

    public LocalDate getFechaPrueba() { return fechaPrueba; }
    public void setFechaPrueba(LocalDate fechaPrueba) { this.fechaPrueba = fechaPrueba; }
}
