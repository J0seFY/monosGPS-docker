package com.buscarpersonas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para el reporte de resultados SIMCE
 */
public class ReporteSimceDTO {
    
    private Long establecimientoId;
    private String nombreEstablecimiento;
    private String direccionEstablecimiento;
    private String comunaEstablecimiento;
    private String telefonoEstablecimiento;
    private Integer totalEstudiantesSimce;
    private BigDecimal puntajePromedio;
    private Integer puntajeMaximo;
    private Integer puntajeMinimo;
    private LocalDate fechaGeneracion;
    private String periodoEvaluacion;
    
    // Constructor vacío
    public ReporteSimceDTO() {
        this.fechaGeneracion = LocalDate.now();
    }
    
    // Constructor con parámetros principales
    public ReporteSimceDTO(Long establecimientoId, String nombreEstablecimiento, 
                          String direccionEstablecimiento, String comunaEstablecimiento,
                          String telefonoEstablecimiento, Integer totalEstudiantesSimce,
                          BigDecimal puntajePromedio, Integer puntajeMaximo, Integer puntajeMinimo) {
        this.establecimientoId = establecimientoId;
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.direccionEstablecimiento = direccionEstablecimiento;
        this.comunaEstablecimiento = comunaEstablecimiento;
        this.telefonoEstablecimiento = telefonoEstablecimiento;
        this.totalEstudiantesSimce = totalEstudiantesSimce;
        this.puntajePromedio = puntajePromedio != null ? puntajePromedio.setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        this.puntajeMaximo = puntajeMaximo;
        this.puntajeMinimo = puntajeMinimo;
        this.fechaGeneracion = LocalDate.now();
    }
    
    // Getters y Setters
    public Long getEstablecimientoId() {
        return establecimientoId;
    }
    
    public void setEstablecimientoId(Long establecimientoId) {
        this.establecimientoId = establecimientoId;
    }
    
    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }
    
    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }
    
    public String getDireccionEstablecimiento() {
        return direccionEstablecimiento;
    }
    
    public void setDireccionEstablecimiento(String direccionEstablecimiento) {
        this.direccionEstablecimiento = direccionEstablecimiento;
    }
    
    public String getComunaEstablecimiento() {
        return comunaEstablecimiento;
    }
    
    public void setComunaEstablecimiento(String comunaEstablecimiento) {
        this.comunaEstablecimiento = comunaEstablecimiento;
    }
    
    public String getTelefonoEstablecimiento() {
        return telefonoEstablecimiento;
    }
    
    public void setTelefonoEstablecimiento(String telefonoEstablecimiento) {
        this.telefonoEstablecimiento = telefonoEstablecimiento;
    }
    
    public Integer getTotalEstudiantesSimce() {
        return totalEstudiantesSimce;
    }
    
    public void setTotalEstudiantesSimce(Integer totalEstudiantesSimce) {
        this.totalEstudiantesSimce = totalEstudiantesSimce;
    }
    
    public BigDecimal getPuntajePromedio() {
        return puntajePromedio;
    }
    
    public void setPuntajePromedio(BigDecimal puntajePromedio) {
        this.puntajePromedio = puntajePromedio != null ? puntajePromedio.setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
    }
    
    public Integer getPuntajeMaximo() {
        return puntajeMaximo;
    }
    
    public void setPuntajeMaximo(Integer puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }
    
    public Integer getPuntajeMinimo() {
        return puntajeMinimo;
    }
    
    public void setPuntajeMinimo(Integer puntajeMinimo) {
        this.puntajeMinimo = puntajeMinimo;
    }
    
    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }
    
    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    
    public String getPeriodoEvaluacion() {
        return periodoEvaluacion;
    }
    
    public void setPeriodoEvaluacion(String periodoEvaluacion) {
        this.periodoEvaluacion = periodoEvaluacion;
    }
    
    // Métodos de utilidad
    public String getPuntajePromedioFormateado() {
        return puntajePromedio != null ? puntajePromedio.toString() : "0.00";
    }
    
    public boolean tieneResultados() {
        return totalEstudiantesSimce != null && totalEstudiantesSimce > 0;
    }
    
    @Override
    public String toString() {
        return "ReporteSimceDTO{" +
                "establecimientoId=" + establecimientoId +
                ", nombreEstablecimiento='" + nombreEstablecimiento + '\'' +
                ", totalEstudiantesSimce=" + totalEstudiantesSimce +
                ", puntajePromedio=" + puntajePromedio +
                ", fechaGeneracion=" + fechaGeneracion +
                '}';
    }
}