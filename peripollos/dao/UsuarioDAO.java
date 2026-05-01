package com.peripollos.dao;

import com.peripollos.db.ConexionDB;
import com.peripollos.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario autenticar(String nombre, String clave) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE nombre = ? AND clave = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, clave);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("rol"),
                            rs.getString("clave")
                    );
                }
            }
        }
        return null;
    }

    public boolean insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, telefono, rol, clave) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.nombre());
            stmt.setString(2, usuario.telefono());
            stmt.setString(3, usuario.rol());
            stmt.setString(4, usuario.clave());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("rol"),
                        rs.getString("clave")
                ));
            }
        }
        return usuarios;
    }
}