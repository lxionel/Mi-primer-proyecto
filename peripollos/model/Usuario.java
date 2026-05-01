package com.peripollos.model;

public class Usuario extends Persona {
    private String rol;
    private String clave;

    public Usuario(int id, String nombre, String telefono, String rol, String clave) {
        super(id, nombre, telefono);
        this.rol = rol;
        this.clave = clave;
    }

    public Usuario(String nombre, String telefono, String rol, String clave) {
        super(nombre, telefono);
        this.rol = rol;
        this.clave = clave;
    }

    public String rol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String clave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean esAdministrador() {
        return "ADMINISTRADOR".equals(rol);
    }

    public boolean esCajero() {
        return "CAJERO".equals(rol);
    }
}