package PaqueteExamen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionBDMarias {
    
    private static final String URL = "jdbc:mysql://localhost:3306/BDMarias"; 
    private static final String USER = "root";  
    private static final String PASSWORD = "Odalis13!?"; 
    
    private static Connection conn = null; 

    // Método para obtener la conexión a la base de datos (patrón Singleton)
    public static Connection obtenerConexion() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión establecida correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al establecer la conexión.");
                throw e; 
            }
        } else {
            System.out.println("Conexión ya existente.");
        }
        return conn;
    }

    // Método para cerrar la conexión (si es necesario)
    public static void cerrarConexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión.");
            e.printStackTrace();
        }
    }

    public static void insertarProducto(String codigo, String nombre, double precio, int cantidad) {
        String sql = "INSERT INTO productos (codigoProducto, nombreProducto, precioUnitario, cantidadExistente) VALUES (?, ?, ?, ?)";
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.setString(2, nombre);
            stmt.setDouble(3, precio);
            stmt.setInt(4, cantidad);
            stmt.executeUpdate();
            System.out.println("Producto insertado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar el producto: " + e.getMessage());
        }
    }

    public static void actualizarProducto(String codigo, String nombre, double precio, int cantidad) {
        String sql = "UPDATE productos SET nombreProducto = ?, precioUnitario = ?, cantidadExistente = ? WHERE codigoProducto = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setInt(3, cantidad); 
            stmt.setString(4, codigo); 
            stmt.executeUpdate();
            System.out.println("Producto actualizado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage());
        }
    }

    public static void eliminarProducto(String codigo) {
        String sql = "DELETE FROM productos WHERE codigoProducto = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.executeUpdate();
            System.out.println("Producto eliminado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
        }
    }

    static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
