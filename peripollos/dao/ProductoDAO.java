package com.peripollos.dao;

import com.peripollos.db.ConexionDB;
import com.peripollos.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listarTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY categoria, nombre";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("categoria")
                ));
            }
        }
        return productos;
    }

    public List<Producto> listarPorCategoria(String categoria) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria = ? ORDER BY nombre";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getString("categoria")
                    ));
                }
            }
        }
        return productos;
    }

    public Producto obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getString("categoria")
                    );
                }
            }
        }
        return null;
    }

    public boolean insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, precio, categoria) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, producto.nombre());
            stmt.setDouble(2, producto.precio());
            stmt.setString(3, producto.categoria());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
}