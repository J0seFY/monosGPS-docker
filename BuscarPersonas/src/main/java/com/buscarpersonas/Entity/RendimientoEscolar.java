package com.buscarpersonas.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rendimiento")
public class RendimientoEscolar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "promedio", nullable = false)
    private Double promedio;

    @ManyToOne
    @JoinColumn(name = "estudiante_rut", referencedColumnName = "rut", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "establecimiento_id", nullable = false)
    private Establecimiento establecimiento;

    // Constructor vacío (requerido por JPA)
    public RendimientoEscolar() {
    }

    // Constructor con parámetros (opcional)
    public RendimientoEscolar(Double promedio, Estudiante estudiante, Establecimiento establecimiento) {
        this.promedio = promedio;
        this.estudiante = estudiante;
        this.establecimiento = establecimiento;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

}
