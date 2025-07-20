//reporte de matriculas por colegio
package com.buscarpersonas.dto;

import java.time.LocalDate;
import java.util.List;

public class ReporteMatriculaDTO {
    
    // Información del establecimiento
    private Long establecimientoId;
    private String nombreEstablecimiento;
    private String direccionEstablecimiento;
    private String comunaEstablecimiento;
    private String telefonoEstablecimiento;
    
    // Información del reporte
    private LocalDate fechaGeneracion;
    private int totalEstudiantes;
    private List<EstudianteMatriculaDTO> estudiantes;
    
    // Constructores
    public ReporteMatriculaDTO() {}
    
    public ReporteMatriculaDTO(Long establecimientoId, String nombreEstablecimiento, 
                              String direccionEstablecimiento, String comunaEstablecimiento, 
                              String telefonoEstablecimiento) {
        this.establecimientoId = establecimientoId;
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.direccionEstablecimiento = direccionEstablecimiento;
        this.comunaEstablecimiento = comunaEstablecimiento;
        this.telefonoEstablecimiento = telefonoEstablecimiento;
        this.fechaGeneracion = LocalDate.now();
    }
    
    // Getters y Setters
    public Long getEstablecimientoId() {
        return establecimientoId;
    }
    
    public void setEstablecimientoId(Long establecimientoId) {
        this.establecimientoId = establecimientoId;
    }
    
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
    
    public String getTelefonoEstablecimiento() {
        return telefonoEstablecimiento;
    }
    
    public void setTelefonoEstablecimiento(String telefonoEstablecimiento) {
        this.telefonoEstablecimiento = telefonoEstablecimiento;
    }
    
    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }
    
    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    
    public int getTotalEstudiantes() {
        return totalEstudiantes;
    }
    
    public void setTotalEstudiantes(int totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }
    
    public List<EstudianteMatriculaDTO> getEstudiantes() {
        return estudiantes;
    }
    
    public void setEstudiantes(List<EstudianteMatriculaDTO> estudiantes) {
        this.estudiantes = estudiantes;
        this.totalEstudiantes = estudiantes != null ? estudiantes.size() : 0;
    }
    
    // Clase interna para representar cada estudiante
    public static class EstudianteMatriculaDTO {
        private String rut;
        private String nombre;
        private String apellido;
        private LocalDate fechaNacimiento;
        private String telefono;
        private String curso;
        private String nacionalidad;
        private String estado;
        
        // Constructores
        public EstudianteMatriculaDTO() {}
        
        public EstudianteMatriculaDTO(String rut, String nombre, String apellido, 
                                    LocalDate fechaNacimiento, String telefono, 
                                    String curso, String nacionalidad, String estado) {
            this.rut = rut;
            this.nombre = nombre;
            this.apellido = apellido;
            this.fechaNacimiento = fechaNacimiento;
            this.telefono = telefono;
            this.curso = curso;
            this.nacionalidad = nacionalidad;
            this.estado = estado;
        }
        
        // Getters y Setters
        public String getRut() {
            return rut;
        }
        
        public void setRut(String rut) {
            this.rut = rut;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        
        public String getApellido() {
            return apellido;
        }
        
        public void setApellido(String apellido) {
            this.apellido = apellido;
        }
        
        public LocalDate getFechaNacimiento() {
            return fechaNacimiento;
        }
        
        public void setFechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
        }
        
        public String getTelefono() {
            return telefono;
        }
        
        public void setTelefono(String telefono) {
            this.telefono = telefono;
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
        
        public String getNombreCompleto() {
            return nombre + " " + apellido;
        }
    }
}
