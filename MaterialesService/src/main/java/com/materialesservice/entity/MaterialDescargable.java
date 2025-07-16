package com.materialesservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "material_descargable")
public class MaterialDescargable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del archivo no puede estar vacío")
    @Size(max = 255, message = "El nombre del archivo no puede exceder 255 caracteres")
    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @NotBlank(message = "El nombre original no puede estar vacío")
    @Size(max = 255, message = "El nombre original no puede exceder 255 caracteres")
    @Column(name = "nombre_original", nullable = false)
    private String nombreOriginal;

    @NotBlank(message = "La ruta del archivo no puede estar vacía")
    @Column(name = "ruta_archivo", nullable = false)
    private String rutaArchivo;

    @NotNull(message = "El nivel educativo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_educativo", nullable = false)
    private NivelEducativo nivelEducativo;

    @NotBlank(message = "El curso es obligatorio")
    @Size(max = 50, message = "El curso no puede exceder 50 caracteres")
    @Column(name = "curso", nullable = false)
    private String curso;

    @Column(name = "tipo_archivo")
    private String tipoArchivo;

    @Column(name = "tamaño_archivo")
    private Long tamañoArchivo;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public MaterialDescargable() {}

    public MaterialDescargable(String nombreArchivo, String nombreOriginal, String rutaArchivo,
                               NivelEducativo nivelEducativo, String curso, String tipoArchivo,
                               Long tamañoArchivo, String descripcion) {
        this.nombreArchivo = nombreArchivo;
        this.nombreOriginal = nombreOriginal;
        this.rutaArchivo = rutaArchivo;
        this.nivelEducativo = nivelEducativo;
        this.curso = curso;
        this.tipoArchivo = tipoArchivo;
        this.tamañoArchivo = tamañoArchivo;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getNombreOriginal() { return nombreOriginal; }
    public void setNombreOriginal(String nombreOriginal) { this.nombreOriginal = nombreOriginal; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public NivelEducativo getNivelEducativo() { return nivelEducativo; }
    public void setNivelEducativo(NivelEducativo nivelEducativo) { this.nivelEducativo = nivelEducativo; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }

    public Long getTamañoArchivo() { return tamañoArchivo; }
    public void setTamañoArchivo(Long tamañoArchivo) { this.tamañoArchivo = tamañoArchivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
