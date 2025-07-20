package com.buscarpersonas.dto;

public class ReporteFuncionariosDTO {

    private String comuna;
    private String establecimiento;
    private String nombre;
    private String apellido;
    private String asignatura;

    public ReporteFuncionariosDTO(String comuna, String establecimiento, String nombre, String apellido, String asignatura) {
        this.comuna = comuna;
        this.establecimiento = establecimiento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.asignatura = asignatura;
    }

    public String getComuna() { return comuna; }
    public String getEstablecimiento() { return establecimiento; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getAsignatura() { return asignatura; }
}
