package com.peripollos.dao;

import com.peripollos.db.ConexionDB;
import com.peripollos.model.Cocina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CocinaDAO {

    public boolean insertar(Cocina cocina) throws SQLException {
        String sql = "INSERT INTO cocina (nombreArea, descripcion) VALUES (?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cocina.getNombreArea());
            stmt.setString(2, cocina.getDescripcion());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cocina.setIdCocina(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public List<Cocina> listarTodas() throws SQLException {
        List<Cocina> cocinas = new ArrayList<>();
        String sql = "SELECT * FROM cocina";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cocinas.add(new Cocina(
                        rs.getInt("idCocina"),
                        rs.getString("nombreArea"),
                        rs.getString("descripcion")
                ));
            }
        }
        return cocinas;
    }

    public Cocina obtenerPorId(int idCocina) throws SQLException {
        String sql = "SELECT * FROM cocina WHERE idCocina = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCocina);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cocina(
                            rs.getInt("idCocina"),
                            rs.getString("nombreArea"),
                            rs.getString("descripcion")
                    );
                }
            }
        }
        return null;
    }
}
