package com.tuempresa.establecimientos.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
