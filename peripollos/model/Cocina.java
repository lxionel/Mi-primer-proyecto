package com.peripollos.model;

public class Cocina {
    private int idCocina;
    private String nombreArea;
    private String descripcion;

    // Constructor completo
    public Cocina(int idCocina, String nombreArea, String descripcion) {
        this.idCocina = idCocina;
        this.nombreArea = nombreArea;
        this.descripcion = descripcion;
    }

    // Constructor sin ID
    public Cocina(String nombreArea, String descripcion) {
        this.nombreArea = nombreArea;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdCocina() { return idCocina; }
    public void setIdCocina(int idCocina) { this.idCocina = idCocina; }

    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}

