package com.buscarpersonas.dto;

import java.util.List;

public class ReporteMatriculaComunalDTO {
    private String nombreEstablecimiento;
    private String direccionEstablecimiento;
    private String comunaEstablecimiento;
    private int totalEstudiantes;
    private int estudiantesPorCurso;
    private String curso;
    private List<EstudianteReporteComunalDTO> estudiantes;
    
    // constructores, getters y setters

    public ReporteMatriculaComunalDTO(String nombreEstablecimiento, String direccionEstablecimiento, String comunaEstablecimiento, int totalEstudiantes, int estudiantesPorCurso, String curso, List<EstudianteReporteComunalDTO> estudiantes) {
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.direccionEstablecimiento = direccionEstablecimiento;
        this.comunaEstablecimiento = comunaEstablecimiento;
        this.totalEstudiantes = totalEstudiantes;
        this.estudiantesPorCurso = estudiantesPorCurso;
        this.curso = curso;
        this.estudiantes = estudiantes;
    }

    // Getters y Setters
    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getDireccionEstablecimiento() {
        return direccionEstablecimiento;
    }

    public void setDireccionEstablecimiento(String direccionEstablecimiento) {
        this.direccionEstablecimiento = direccionEstablecimiento;
    }

    public String getComunaEstablecimiento() {
        return comunaEstablecimiento;
    }

    public void setComunaEstablecimiento(String comunaEstablecimiento) {
        this.comunaEstablecimiento = comunaEstablecimiento;
    }

    public int getTotalEstudiantes() {
        return totalEstudiantes;
    }

    public void setTotalEstudiantes(int totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    public int getEstudiantesPorCurso() {
        return estudiantesPorCurso;
    }

    public void setEstudiantesPorCurso(int estudiantesPorCurso) {
        this.estudiantesPorCurso = estudiantesPorCurso;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<EstudianteReporteComunalDTO> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<EstudianteReporteComunalDTO> estudiantes) {
        this.estudiantes = estudiantes;
    }
}
