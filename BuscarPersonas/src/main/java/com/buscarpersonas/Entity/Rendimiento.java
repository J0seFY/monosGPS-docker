package com.buscarpersonas.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rendimiento")
public class Rendimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_alumno", nullable = false)
    private String nombreAlumno;

    @Column(name = "curso", nullable = false)
    private String curso;

    @Column(name = "establecimiento", nullable = false)
    private String establecimiento;

    @Column(name = "comuna", nullable = false)
    private String comuna;

    @Column(name = "promedio_notas")
    private Double promedioNotas;

    @Column(name = "asistencia")
    private Double asistencia;

    @Column(name = "observaciones")
    private String observaciones;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public Double getPromedioNotas() {
        return promedioNotas;
    }

    public void setPromedioNotas(Double promedioNotas) {
        this.promedioNotas = promedioNotas;
    }

    public Double getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Double asistencia) {
        this.asistencia = asistencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
