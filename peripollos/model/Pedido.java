package com.peripollos.model;

import com.peripollos.interfaces.Imprimible;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Imprimible {
    private int id;
    private String tipo;
    private LocalDateTime fecha;
    private Usuario usuario;
    private Cliente cliente;
    private List<DetallePedido> detalles;
    private double total;
    private String estado;

    public Pedido(int id, String tipo, LocalDateTime fecha, Usuario usuario, Cliente cliente) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.usuario = usuario;
        this.cliente = cliente;
        this.detalles = new ArrayList<>();
        this.total = 0.0;
        this.estado = "PENDIENTE";
    }

    public Pedido(String tipo, Usuario usuario) {
        this.tipo = "RESTAURANTE";
        this.fecha = LocalDateTime.now();
        this.usuario = usuario;
        this.cliente = null;
        this.detalles = new ArrayList<>();
        this.total = 0.0;
        this.estado = "PENDIENTE";
    }

    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String tipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime fecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Usuario usuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cliente cliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetallePedido> detalles() {
        return detalles;
    }

    public double total() {
        return total;
    }
    public void setTotal(double total){
        this.total = total;
    }

    public String estado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        calcularTotal();
    }

    private void calcularTotal() {
        total = 0.0;
        for (DetallePedido detalle : detalles) {
            total += detalle.subtotal();
        }
    }

    @Override
    public String generarDocumento() {
        StringBuilder doc = new StringBuilder();
        doc.append("═════════════════════════════════════════\n");
        doc.append("        POLLERÍA PERIPOLLOS        \n");
        doc.append("═════════════════════════════════════════\n");
        doc.append(" BOLETA DE VENTA\n");
        doc.append("─────────────────────────────────────────\n");
        doc.append(" Pedido #: ").append(id).append("\n");
        doc.append(" Tipo: RESTAURANTE\n");
        doc.append(" Fecha: ").append(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        doc.append(" Atendido por: ").append(usuario.nombre()).append("\n");
        doc.append("─────────────────────────────────────────\n");
        doc.append(" DETALLE DEL PEDIDO:\n");
        doc.append("─────────────────────────────────────────\n");

        for (DetallePedido detalle : detalles) {
            doc.append(String.format(" %dx %s\n",
                    detalle.cantidad(),
                    detalle.producto().nombre()));
            doc.append(String.format("   S/ %.2f x %d = S/ %.2f\n",
                    detalle.producto().precio(),
                    detalle.cantidad(),
                    detalle.subtotal()));
        }

        doc.append("─────────────────────────────────────────\n");
        doc.append(String.format(" TOTAL: S/ %.2f\n", total));
        doc.append("═════════════════════════════════════════\n");
        doc.append("   Gracias por su preferencia!    \n");
        doc.append("═════════════════════════════════════════\n");

        return doc.toString();
    }
}