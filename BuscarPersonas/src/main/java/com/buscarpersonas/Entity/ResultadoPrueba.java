package com.buscarpersonas.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "resultado_prueba")
public class ResultadoPrueba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_rut", nullable = false)
    private String estudianteRut;

    @Column(name = "tipo_prueba", nullable = false)
    private String tipoPrueba; // Ej: "SIMCE", "PAES"

    @Column(name = "puntaje", nullable = false)
    private Integer puntaje;

    @Column(name = "fecha")
    private LocalDate fecha;

    // Constructor vacío requerido por JPA
    public ResultadoPrueba() {
    }

    public ResultadoPrueba(String estudianteRut, String tipoPrueba, Integer puntaje, LocalDate fecha) {
        this.estudianteRut = estudianteRut;
        this.tipoPrueba = tipoPrueba;
        this.puntaje = puntaje;
        this.fecha = fecha;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getEstudianteRut() {
        return estudianteRut;
    }

    public void setEstudianteRut(String estudianteRut) {
        this.estudianteRut = estudianteRut;
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
}
