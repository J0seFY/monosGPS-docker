// Paso 1: DTO para estructurar los datos del informe
package com.buscarpersonas.dto;

public class ReporteMatriculaDTO {
    private String nombreEstablecimiento;
    private String direccion;
    private String comuna;
    private String telefono;
    private String nombreEstudiante;
    private String apellidoEstudiante;
    private String rutEstudiante;
    private String curso;
    private String nacionalidad;
    private String estado;

    // Constructor, getters y setters
    public ReporteMatriculaDTO(String nombreEstablecimiento, String direccion, String comuna, String telefono,
                               String nombreEstudiante, String apellidoEstudiante, String rutEstudiante,
                               String curso, String nacionalidad, String estado) {
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.direccion = direccion;
        this.comuna = comuna;
        this.telefono = telefono;
        this.nombreEstudiante = nombreEstudiante;
        this.apellidoEstudiante = apellidoEstudiante;
        this.rutEstudiante = rutEstudiante;
        this.curso = curso;
        this.nacionalidad = nacionalidad;
        this.estado = estado;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }


    public String getApellidoEstudiante() {
        return apellidoEstudiante;
    }

    public void setApellidoEstudiante(String apellidoEstudiante) {
        this.apellidoEstudiante = apellidoEstudiante;
    }

    public String getRutEstudiante() {
        return rutEstudiante;
    }

    public void setRutEstudiante(String rutEstudiante) {
        this.rutEstudiante = rutEstudiante;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
