package com.peripollos.dao;

import com.peripollos.db.ConexionDB;
import com.peripollos.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public boolean insertar(Pedido pedido) throws SQLException {
        Connection conn = null;
        try {
            conn = ConexionDB.obtenerConexion();
            conn.setAutoCommit(false);

            String sqlPedido = "INSERT INTO pedidos (tipo, fecha, id_usuario, total, estado) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setString(1, "RESTAURANTE");
                stmtPedido.setTimestamp(2, Timestamp.valueOf(pedido.fecha()));
                stmtPedido.setInt(3, pedido.usuario().id());
                stmtPedido.setDouble(4, pedido.total());
                stmtPedido.setString(5, pedido.estado());

                int filasAfectadas = stmtPedido.executeUpdate();

                if (filasAfectadas > 0) {
                    try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                        if (rs.next()) {
                            pedido.setId(rs.getInt(1));
                        }
                    }

                    String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";

                    try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                        for (DetallePedido detalle : pedido.detalles()) {
                            stmtDetalle.setInt(1, pedido.id());
                            stmtDetalle.setInt(2, detalle.producto().id());
                            stmtDetalle.setInt(3, detalle.cantidad());
                            stmtDetalle.setDouble(4, detalle.subtotal());
                            stmtDetalle.addBatch();
                        }
                        stmtDetalle.executeBatch();
                    }

                    conn.commit();
                    return true;
                }
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Pedido> listarPedidosDelDia() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre as nombre_usuario, u.telefono as tel_usuario, u.rol, u.clave " +
                "FROM pedidos p " +
                "INNER JOIN usuarios u ON p.id_usuario = u.id " +
                "WHERE CAST(p.fecha AS DATE) = CAST(GETDATE() AS DATE) " +
                "ORDER BY p.fecha DESC";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("tel_usuario"),
                        rs.getString("rol"),
                        rs.getString("clave")
                );

                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getTimestamp("fecha").toLocalDateTime(),
                        usuario,
                        null
                );

                pedido.setEstado(rs.getString("estado"));
                pedido.setTotal(rs.getDouble("total"));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    public double obtenerTotalVentasDelDia() throws SQLException {
        String sql = "SELECT ISNULL(SUM(total), 0) as total FROM pedidos WHERE CAST(fecha AS DATE) = CAST(GETDATE() AS DATE)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }
}