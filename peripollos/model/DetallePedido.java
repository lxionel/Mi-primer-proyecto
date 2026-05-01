package com.peripollos.model;

public class DetallePedido {
    private int id;
    private int idPedido;
    private Producto producto;
    private int cantidad;
    private double subtotal;

    public DetallePedido(int id, int idPedido, Producto producto, int cantidad) {
        this.id = id;
        this.idPedido = idPedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = producto.precio() * cantidad;
    }

    public DetallePedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = producto.precio() * cantidad;
    }

    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int idPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Producto producto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int cantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = producto.precio() * cantidad;
    }

    public double subtotal() {
        return subtotal;
    }

    public void calcularSubtotal() {
        this.subtotal = producto.precio() * cantidad;
    }
}