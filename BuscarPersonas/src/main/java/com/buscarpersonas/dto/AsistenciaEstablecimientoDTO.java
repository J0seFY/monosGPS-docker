package com.buscarpersonas.dto;

public class AsistenciaEstablecimientoDTO {
    private String nombreEstablecimiento;
    private Long totalEstudiantes;
    private Long presentes;
    private Long ausentes;

    // Constructor vacío (importante para frameworks como Jackson)
    public AsistenciaEstablecimientoDTO() {
    }

    // Constructor completo
    public AsistenciaEstablecimientoDTO(String nombreEstablecimiento, Long totalEstudiantes, Long presentes, Long ausentes) {
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.totalEstudiantes = totalEstudiantes;
        this.presentes = presentes;
        this.ausentes = ausentes;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public Long getTotalEstudiantes() {
        return totalEstudiantes;
    }

    public void setTotalEstudiantes(Long totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    public Long getPresentes() {
        return presentes;
    }

    public void setPresentes(Long presentes) {
        this.presentes = presentes;
    }

    public Long getAusentes() {
        return ausentes;
    }

    public void setAusentes(Long ausentes) {
        this.ausentes = ausentes;
    }
}
