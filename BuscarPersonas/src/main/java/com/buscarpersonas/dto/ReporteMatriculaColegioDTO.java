//para matriculas por colegio
package com.buscarpersonas.dto;

import java.util.List;

public class ReporteMatriculaColegioDTO {
    private String nombreEstablecimiento;
    private String direccionEstablecimiento;
    private String comunaEstablecimiento;
    private String telefonoEstablecimiento;
    private int totalEstudiantes;
    private List<EstudianteMatriculaColegioDTO> estudiantes;

    // Constructors
    public ReporteMatriculaColegioDTO() {}

    public ReporteMatriculaColegioDTO(String nombreEstablecimiento, String direccionEstablecimiento, 
                             String comunaEstablecimiento, String telefonoEstablecimiento,
                             int totalEstudiantes, List<EstudianteMatriculaColegioDTO> estudiantes) {
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.direccionEstablecimiento = direccionEstablecimiento;
        this.comunaEstablecimiento = comunaEstablecimiento;
        this.telefonoEstablecimiento = telefonoEstablecimiento;
        this.totalEstudiantes = totalEstudiantes;
        this.estudiantes = estudiantes;
    }

    // Getters and Setters
    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }

    public String getDireccionEstablecimiento() { return direccionEstablecimiento; }
    public void setDireccionEstablecimiento(String direccionEstablecimiento) { this.direccionEstablecimiento = direccionEstablecimiento; }

    public String getComunaEstablecimiento() { return comunaEstablecimiento; }
    public void setComunaEstablecimiento(String comunaEstablecimiento) { this.comunaEstablecimiento = comunaEstablecimiento; }

    public String getTelefonoEstablecimiento() { return telefonoEstablecimiento; }
    public void setTelefonoEstablecimiento(String telefonoEstablecimiento) { this.telefonoEstablecimiento = telefonoEstablecimiento; }

    public int getTotalEstudiantes() { return totalEstudiantes; }
    public void setTotalEstudiantes(int totalEstudiantes) { this.totalEstudiantes = totalEstudiantes; }

    public List<EstudianteMatriculaColegioDTO> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(List<EstudianteMatriculaColegioDTO> estudiantes) { this.estudiantes = estudiantes; }
}
