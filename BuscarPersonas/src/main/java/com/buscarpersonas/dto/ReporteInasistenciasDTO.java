package com.buscarpersonas.dto;

public class ReporteInasistenciasDTO {
    private String comuna;
    private String establecimiento;
    private String nombre;
    private String apellido;
    private Long cantidadInasistencias;

    public ReporteInasistenciasDTO(String comuna, String establecimiento, String nombre, String apellido, Long cantidadInasistencias) {
        this.comuna = comuna;
        this.establecimiento = establecimiento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cantidadInasistencias = cantidadInasistencias;
    }

    public String getComuna() { return comuna; }
    public String getEstablecimiento() { return establecimiento; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public Long getCantidadInasistencias() { return cantidadInasistencias; }
}
