package com.mensajesservice.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "estudiante") // ← útil para asegurar nombre de tabla
@Getter
@Setter
public class Estudiante {

    @Id
    @Column(length = 20)
    private String rut;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(length = 50)
    private String telefono;

    @Column(length = 100)
    private String curso;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 100)
    private String nacionalidad;

    @Column(length = 50)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establecimiento_id", referencedColumnName = "id")
    private Establecimiento establecimiento; // ← relación correctamente declarada
}

