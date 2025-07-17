package com.materialesservice.entity;

import com.materialesservice.enumeraciones.Curso;
import com.materialesservice.enumeraciones.NivelEducativo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    @NotNull(message = "El curso es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "curso", nullable = false)
    private Curso curso;

    @Column(name = "tamaño_archivo")
    private Long tamañoArchivo;

    // Constructores
    public MaterialDescargable() {}

    public MaterialDescargable(String nombreArchivo, String nombreOriginal, String rutaArchivo,
                               NivelEducativo nivelEducativo, Curso curso) {
        this.nombreArchivo = nombreArchivo;
        this.nombreOriginal = nombreOriginal;
        this.rutaArchivo = rutaArchivo;
        this.nivelEducativo = nivelEducativo;
        this.curso = curso;
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

    public @NotNull(message = "El curso es obligatorio") Curso getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = Curso.valueOf(curso); }

}
