package com.peripollos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=peripollos_db;encrypt=false;trustServerCertificate=true";
    private static final String USUARIO = "sa";
    private static final String CLAVE = "Lionel2006";

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQL Server no encontrado", e);
        }
    }

    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    public static boolean probarConexion() {
        try (Connection conn = obtenerConexion()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }
}