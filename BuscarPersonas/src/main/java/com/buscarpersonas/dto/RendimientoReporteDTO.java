package com.buscarpersonas.dto;

import java.math.BigDecimal;

public class RendimientoReporteDTO {
    
    private String establecimientoNombre;
    private String comuna;
    private String asignatura;
    private BigDecimal promedioGeneral;
    private Long totalEstudiantes;
    private Long estudiantesConNotas;
    private BigDecimal notaMaxima;
    private BigDecimal notaMinima;
    private String curso;
    
    // Constructor completo
    public RendimientoReporteDTO(String establecimientoNombre, String comuna, String asignatura, 
                                BigDecimal promedioGeneral, Long totalEstudiantes, Long estudiantesConNotas,
                                BigDecimal notaMaxima, BigDecimal notaMinima, String curso) {
        this.establecimientoNombre = establecimientoNombre;
        this.comuna = comuna;
        this.asignatura = asignatura;
        this.promedioGeneral = promedioGeneral;
        this.totalEstudiantes = totalEstudiantes;
        this.estudiantesConNotas = estudiantesConNotas;
        this.notaMaxima = notaMaxima;
        this.notaMinima = notaMinima;
        this.curso = curso;
    }
    
    // Constructor para datos comunales
    public RendimientoReporteDTO(String comuna, String asignatura, BigDecimal promedioGeneral, 
                                Long totalEstudiantes, Long estudiantesConNotas) {
        this.comuna = comuna;
        this.asignatura = asignatura;
        this.promedioGeneral = promedioGeneral;
        this.totalEstudiantes = totalEstudiantes;
        this.estudiantesConNotas = estudiantesConNotas;
    }
    
    // Getters y Setters
    public String getEstablecimientoNombre() {
        return establecimientoNombre;
    }
    
    public void setEstablecimientoNombre(String establecimientoNombre) {
        this.establecimientoNombre = establecimientoNombre;
    }
    
    public String getComuna() {
        return comuna;
    }
    
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    
    public String getAsignatura() {
        return asignatura;
    }
    
    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }
    
    public BigDecimal getPromedioGeneral() {
        return promedioGeneral;
    }
    
    public void setPromedioGeneral(BigDecimal promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }
    
    public Long getTotalEstudiantes() {
        return totalEstudiantes;
    }
    
    public void setTotalEstudiantes(Long totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }
    
    public Long getEstudiantesConNotas() {
        return estudiantesConNotas;
    }
    
    public void setEstudiantesConNotas(Long estudiantesConNotas) {
        this.estudiantesConNotas = estudiantesConNotas;
    }
    
    public BigDecimal getNotaMaxima() {
        return notaMaxima;
    }
    
    public void setNotaMaxima(BigDecimal notaMaxima) {
        this.notaMaxima = notaMaxima;
    }
    
    public BigDecimal getNotaMinima() {
        return notaMinima;
    }
    
    public void setNotaMinima(BigDecimal notaMinima) {
        this.notaMinima = notaMinima;
    }
    
    public String getCurso() {
        return curso;
    }
    
    public void setCurso(String curso) {
        this.curso = curso;
    }
}