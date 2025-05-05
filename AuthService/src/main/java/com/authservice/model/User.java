package com.authservice.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class User {

    @Id
    private String rut;

    private String nombres;
    private String apellido1;
    private String apellido2;
    private String email;
    private String rol;
    private String establecimiento;
    private String password;

    public User(String rut, String password, String establecimiento, String rol, String email, String apellido2, String apellido1, String nombres) {
        this.rut = rut;
        this.password = password;
        this.establecimiento = establecimiento;
        this.rol = rol;
        this.email = email;
        this.apellido2 = apellido2;
        this.apellido1 = apellido1;
        this.nombres = nombres;
    }

    public User() {

    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
