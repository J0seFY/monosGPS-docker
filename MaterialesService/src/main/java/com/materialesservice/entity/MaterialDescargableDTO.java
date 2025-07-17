package com.materialesservice.entity;

import com.materialesservice.enumeraciones.Curso;
import com.materialesservice.enumeraciones.NivelEducativo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MaterialDescargableDTO {

    private Long id;

    @NotBlank(message = "El nombre original es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nombreOriginal;

    @NotNull(message = "El nivel educativo es obligatorio")
    private NivelEducativo nivelEducativo;

    @NotNull(message = "El curso es obligatorio")
    private Curso curso;

    // Constructores
    public MaterialDescargableDTO() {}

    public MaterialDescargableDTO(Long id, String nombreOriginal, NivelEducativo nivelEducativo,
                                  Curso curso) {
        this.id = id;
        this.nombreOriginal = nombreOriginal;
        this.nivelEducativo = nivelEducativo;
        this.curso = curso;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreOriginal() { return nombreOriginal; }
    public void setNombreOriginal(String nombreOriginal) { this.nombreOriginal = nombreOriginal; }

    public NivelEducativo getNivelEducativo() { return nivelEducativo; }
    public void setNivelEducativo(NivelEducativo nivelEducativo) { this.nivelEducativo = nivelEducativo; }

    public @NotNull(message = "El curso es obligatorio") Curso getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = Curso.valueOf(curso); }
}