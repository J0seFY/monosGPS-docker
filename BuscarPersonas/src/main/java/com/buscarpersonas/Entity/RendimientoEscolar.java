package com.buscarpersonas.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rendimiento_escolar")
public class RendimientoEscolar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "promedio")
    private Double promedio;

    @ManyToOne
    @JoinColumn(name = "estudiante_rut")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "establecimiento_id")
    private Establecimiento establecimiento;

    // Getters y setters
}
