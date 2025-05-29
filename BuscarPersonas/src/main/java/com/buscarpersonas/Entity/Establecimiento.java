package com.buscarpersonas.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String telefono;

    @OneToMany(mappedBy = "establecimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estudiante> estudiantes;

    @OneToMany(mappedBy = "establecimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profesor> profesores;

    // Constructor vacío (ya lo tienes)
    public Establecimiento() {}

    // Constructor que acepta nombre (para tests o creación rápida)
    public Establecimiento(String nombre) {
        this.nombre = nombre;
    }
}
