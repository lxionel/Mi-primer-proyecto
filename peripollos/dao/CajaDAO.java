package com.peripollos.dao;

import com.peripollos.db.ConexionDB;
import com.peripollos.model.Caja;
import com.peripollos.model.Usuario;
import java.sql.*;
import java.time.LocalDateTime;

public class CajaDAO {
    public boolean abrirCaja(Caja caja) throws SQLException {
        String sql = "INSERT INTO caja (fecha_apertura, monto_inicial, id_usuario, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(caja.fechaApertura()));
            stmt.setDouble(2, caja.montoInicial());
            stmt.setInt(3, caja.usuario().id());
            stmt.setString(4, caja.estado());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        caja.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean cerrarCaja(Caja caja) throws SQLException {
        String sql = "UPDATE caja SET fecha_cierre = ?, monto_final = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(caja.fechaCierre()));
            stmt.setDouble(2, caja.montoFinal());
            stmt.setString(3, caja.estado());
            stmt.setInt(4, caja.id());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    public Caja obtenerCajaAbierta() throws SQLException {
        String sql = "SELECT c.*, u.nombre as nombre_usuario, u.telefono, u.rol, u.clave " +
                "FROM caja c " +
                "INNER JOIN usuarios u ON c.id_usuario = u.id " +
                "WHERE c.estado = 'ABIERTA'";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("telefono"),
                        rs.getString("rol"),
                        rs.getString("clave")
                );

                LocalDateTime fechaCierre = null;
                Timestamp tsCierre = rs.getTimestamp("fecha_cierre");
                if (tsCierre != null) {
                    fechaCierre = tsCierre.toLocalDateTime();
                }

                return new Caja(
                        rs.getInt("id"),
                        rs.getTimestamp("fecha_apertura").toLocalDateTime(),
                        fechaCierre,
                        rs.getDouble("monto_inicial"),
                        rs.getDouble("monto_final"),
                        usuario,
                        rs.getString("estado")
                );
            }
        }
        return null; 
    }
}
