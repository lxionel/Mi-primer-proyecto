package com.peripollos.dao;

import com.peripollos.db.ConexionDB;
import com.peripollos.model.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public boolean insertar(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleados (nombre, dni, telefono, cargo) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getDni());
            stmt.setString(3, empleado.getTelefono());
            stmt.setString(4, empleado.getCargo());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        empleado.setIdEmpleado(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empleados.add(new Empleado(
                        rs.getInt("idEmpleado"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("cargo")
                ));
            }
        }
        return empleados;
    }

    public Empleado obtenerPorId(int idEmpleado) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE idEmpleado = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmpleado);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(
                            rs.getInt("idEmpleado"),
                            rs.getString("nombre"),
                            rs.getString("dni"),
                            rs.getString("telefono"),
                            rs.getString("cargo")
                    );
                }
            }
        }
        return null;
    }
}