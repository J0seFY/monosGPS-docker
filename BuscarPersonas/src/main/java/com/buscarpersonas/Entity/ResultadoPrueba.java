package com.buscarpersonas.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "resultado_prueba")
public class ResultadoPrueba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_prueba", nullable = false)
    private String tipoPrueba;

    @Column(nullable = false)
    private Integer puntaje;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "estudiante_rut", referencedColumnName = "rut")
    private Estudiante estudiante;

    // Constructores
    public ResultadoPrueba() {}

    public ResultadoPrueba(String tipoPrueba, Integer puntaje, LocalDate fecha, Estudiante estudiante) {
        this.tipoPrueba = tipoPrueba;
        this.puntaje = puntaje;
        this.fecha = fecha;
        this.estudiante = estudiante;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getTipoPrueba() {
        return tipoPrueba;
    }

    public void setTipoPrueba(String tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}