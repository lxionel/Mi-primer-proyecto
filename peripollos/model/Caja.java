package com.peripollos.model;

import com.peripollos.interfaces.Imprimible;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Caja implements Imprimible {
    private int id;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private double montoInicial;
    private double montoFinal;
    private Usuario usuario;
    private String estado;

    public Caja(double montoInicial, Usuario usuario) {
        this.fechaApertura = LocalDateTime.now();
        this.montoInicial = montoInicial;
        this.usuario = usuario;
        this.estado = "ABIERTA";
    }

    public Caja(int id, LocalDateTime fechaApertura, LocalDateTime fechaCierre,
                double montoInicial, double montoFinal, Usuario usuario, String estado) {
        this.id = id;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.usuario = usuario;
        this.estado = estado;
    }

    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime fechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime fechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public double montoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public double montoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public Usuario usuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String estado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void cerrarCaja(double montoFinal) {
        this.fechaCierre = LocalDateTime.now();
        this.montoFinal = montoFinal;
        this.estado = "CERRADA";
    }

    @Override
    public String generarDocumento() {
        StringBuilder doc = new StringBuilder();
        doc.append("═══════════════════════════════════\n");
        doc.append("        POLLERÍA PERIPOLLOS        \n");
        doc.append("═══════════════════════════════════\n");
        doc.append("    REPORTE DE CIERRE DE CAJA     \n");
        doc.append("───────────────────────────────────\n");
        doc.append("Caja #: ").append(id).append("\n");
        doc.append("Usuario: ").append(usuario.nombre()).append("\n");
        doc.append("Apertura: ").append(fechaApertura.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");

        if (fechaCierre != null) {
            doc.append("Cierre: ").append(fechaCierre.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        }

        doc.append("───────────────────────────────────\n");
        doc.append(String.format("Monto Inicial: S/ %.2f\n", montoInicial));
        doc.append(String.format("Monto Final:   S/ %.2f\n", montoFinal));
        doc.append(String.format("Ventas del día: S/ %.2f\n", montoFinal - montoInicial));
        doc.append("───────────────────────────────────\n");
        doc.append("Estado: ").append(estado).append("\n");
        doc.append("═══════════════════════════════════\n");

        return doc.toString();
    }
}