package com.buscarpersonas.dto;

public class EstudianteRetiradoDTO {
    private String nombreCompleto;
    private String rut;
    private String curso;
    private String nacionalidad;
    private String establecimiento;

    // Constructor vacío
    public EstudianteRetiradoDTO() {}

    // Constructor con todos los parámetros
    public EstudianteRetiradoDTO(String nombreCompleto, String rut, String curso, String nacionalidad, String establecimiento) {
        this.nombreCompleto = nombreCompleto;
        this.rut = rut;
        this.curso = curso;
        this.nacionalidad = nacionalidad;
        this.establecimiento = establecimiento;
    }

    // Getters y Setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    @Override
    public String toString() {
        return "EstudianteRetiradoDTO{" +
                "nombreCompleto='" + nombreCompleto + '\'' +
                ", rut='" + rut + '\'' +
                ", curso='" + curso + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", establecimiento='" + establecimiento + '\'' +
                '}';
    }
}