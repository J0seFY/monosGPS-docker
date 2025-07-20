package com.buscarpersonas.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rendimiento")
public class Rendimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_rut", referencedColumnName = "rut")
    private Estudiante estudiante;
    
    @Column(name = "asignatura", length = 100)
    private String asignatura;
    
    @Column(name = "nota", precision = 3, scale = 1)
    private BigDecimal nota;
    
    @Column(name = "fecha")
    private LocalDate fecha;
    
    // Constructores
    public Rendimiento() {}
    
    public Rendimiento(Estudiante estudiante, String asignatura, BigDecimal nota, LocalDate fecha) {
        this.estudiante = estudiante;
        this.asignatura = asignatura;
        this.nota = nota;
        this.fecha = fecha;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public String getAsignatura() {
        return asignatura;
    }
    
    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }
    
    public BigDecimal getNota() {
        return nota;
    }
    
    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}