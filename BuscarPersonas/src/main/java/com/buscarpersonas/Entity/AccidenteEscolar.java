package com.buscarpersonas.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "accidente_escolar")
public class AccidenteEscolar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "establecimiento_id", nullable = false)
    private Establecimiento establecimiento;

    @ManyToOne
    @JoinColumn(name = "estudiante_rut")
    private Estudiante estudiante;

    // Getters y setters
    public Long getId() { return id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Establecimiento getEstablecimiento() { return establecimiento; }
    public void setEstablecimiento(Establecimiento establecimiento) { this.establecimiento = establecimiento; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
}
