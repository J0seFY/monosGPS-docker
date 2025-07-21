package com.buscarpersonas.dto;

import java.math.BigDecimal;

public class ReporteSimcePaesDTO {
    
    private Long establecimientoId;
    private String nombreEstablecimiento;
    private String direccionEstablecimiento;
    private String comunaEstablecimiento;
    private String telefonoEstablecimiento;
    
    // Datos SIMCE
    private Integer cantidadEstudiantesSimce;
    private BigDecimal promedioSimce;
    private Integer puntajeMaximoSimce;
    private Integer puntajeMinimoSimce;
    
    // Datos PAES
    private Integer cantidadEstudiantesPaes;
    private BigDecimal promedioPaes;
    private Integer puntajeMaximoPaes;
    private Integer puntajeMinimoPaes;
    
    // Constructores
    public ReporteSimcePaesDTO() {}
    
    public ReporteSimcePaesDTO(Long establecimientoId, String nombreEstablecimiento, 
                              String direccionEstablecimiento, String comunaEstablecimiento,
                              String telefonoEstablecimiento) {
        this.establecimientoId = establecimientoId;
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.direccionEstablecimiento = direccionEstablecimiento;
        this.comunaEstablecimiento = comunaEstablecimiento;
        this.telefonoEstablecimiento = telefonoEstablecimiento;
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
    
    public Integer getCantidadEstudiantesSimce() {
        return cantidadEstudiantesSimce;
    }
    
    public void setCantidadEstudiantesSimce(Integer cantidadEstudiantesSimce) {
        this.cantidadEstudiantesSimce = cantidadEstudiantesSimce;
    }
    
    public BigDecimal getPromedioSimce() {
        return promedioSimce;
    }
    
    public void setPromedioSimce(BigDecimal promedioSimce) {
        this.promedioSimce = promedioSimce;
    }
    
    public Integer getPuntajeMaximoSimce() {
        return puntajeMaximoSimce;
    }
    
    public void setPuntajeMaximoSimce(Integer puntajeMaximoSimce) {
        this.puntajeMaximoSimce = puntajeMaximoSimce;
    }
    
    public Integer getPuntajeMinimoSimce() {
        return puntajeMinimoSimce;
    }
    
    public void setPuntajeMinimoSimce(Integer puntajeMinimoSimce) {
        this.puntajeMinimoSimce = puntajeMinimoSimce;
    }
    
    public Integer getCantidadEstudiantesPaes() {
        return cantidadEstudiantesPaes;
    }
    
    public void setCantidadEstudiantesPaes(Integer cantidadEstudiantesPaes) {
        this.cantidadEstudiantesPaes = cantidadEstudiantesPaes;
    }
    
    public BigDecimal getPromedioPaes() {
        return promedioPaes;
    }
    
    public void setPromedioPaes(BigDecimal promedioPaes) {
        this.promedioPaes = promedioPaes;
    }
    
    public Integer getPuntajeMaximoPaes() {
        return puntajeMaximoPaes;
    }
    
    public void setPuntajeMaximoPaes(Integer puntajeMaximoPaes) {
        this.puntajeMaximoPaes = puntajeMaximoPaes;
    }
    
    public Integer getPuntajeMinimoPaes() {
        return puntajeMinimoPaes;
    }
    
    public void setPuntajeMinimoPaes(Integer puntajeMinimoPaes) {
        this.puntajeMinimoPaes = puntajeMinimoPaes;
    }
}