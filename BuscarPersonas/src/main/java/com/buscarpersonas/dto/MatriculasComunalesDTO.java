package com.buscarpersonas.dto;

public class MatriculasComunalesDTO {
    
    private String comuna;
    private Long cantidadEstudiantes;
    
    public MatriculasComunalesDTO() {}
    
    public MatriculasComunalesDTO(String comuna, Long cantidadEstudiantes) {
        this.comuna = comuna;
        this.cantidadEstudiantes = cantidadEstudiantes;
    }
    
    public String getComuna() {
        return comuna;
    }
    
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    
    public Long getCantidadEstudiantes() {
        return cantidadEstudiantes;
    }
    
    public void setCantidadEstudiantes(Long cantidadEstudiantes) {
        this.cantidadEstudiantes = cantidadEstudiantes;
    }
    
    @Override
    public String toString() {
        return "MatriculasComunalesDTO{" +
                "comuna='" + comuna + '\'' +
                ", cantidadEstudiantes=" + cantidadEstudiantes +
                '}';
    }
}