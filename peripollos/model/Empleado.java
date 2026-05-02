package com.peripollos.model;

public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String dni;
    private String telefono;
    private String cargo;

    public Empleado(int idEmpleado, String nombre, String dni, String telefono, String cargo) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.cargo = cargo;
    }

    public Empleado(String nombre, String dni, String telefono, String cargo) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.cargo = cargo;
    }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}
