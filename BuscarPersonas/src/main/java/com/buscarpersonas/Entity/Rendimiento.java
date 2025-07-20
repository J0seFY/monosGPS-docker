package com.buscarpersonas.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rendimiento")
public class Rendimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estudiante_rut")
    private String estudianteRut;

    @Column(name = "asignatura")
    private String asignatura;

    @Column(name = "nota")
    private BigDecimal nota;

    @Column(name = "fecha")
    private LocalDate fecha;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEstudianteRut() { return estudianteRut; }
    public void setEstudianteRut(String estudianteRut) { this.estudianteRut = estudianteRut; }

    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }

    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}