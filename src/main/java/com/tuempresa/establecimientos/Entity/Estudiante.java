package com.tuempresa.establecimientos.Entity;

import jakarta.persistence.Entity;
import java.time.LocalDate;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Estudiante {

    @Id
    private String rut;
    
    private String nombre;
    private String apellido;
    private String telefono;
    private String curso;

    @ManyToOne
    @JoinColumn(name = "establecimiento_id")
    private Establecimiento establecimiento;
}

