package com.buscarpersonas.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rendimiento")
public class Rendimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_rut")
    private String estudianteRut;

    @Column(name = "asignatura")
    private String asignatura;

    @Column(name = "nota")
    private Double nota;

    @Column(name = "fecha")
    private LocalDate fecha;

    // Constructor vacío
    public Rendimiento() {}

    // Getters y setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEstudianteRut() {
        return estudianteRut;
    }
    public void setEstudianteRut(String estudianteRut) {
        this.estudianteRut = estudianteRut;
    }

    public String getAsignatura() {
        return asignatura;
    }
    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public Double getNota() {
        return nota;
    }
    public void setNota(Double nota) {
        this.nota = nota;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
