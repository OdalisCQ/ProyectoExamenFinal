package PaqueteExamen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Transacción extends JFrame {

    private JTextField tfCodigoProducto, tfCantidad;
    private JButton btnFacturar, btnListarVentas, btnRegresar;
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private JLabel ETitulo;
    private ProductoGUI productoGUI;
    private ImageIcon logoIcon;
    
     public Transacción(ProductoGUI productoGUI) {
    this.productoGUI = productoGUI; 
    setTitle("Facturación");
    setBounds(730, 250, 650, 740);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cambia a DISPOSE_ON_CLOSE
    setLocationRelativeTo(null);
    setLayout(null);
    getContentPane().setBackground(Color.BLACK);
    logoIcon = new ImageIcon("logo1.jpg");
        setIconImage(logoIcon.getImage());

    agregarComponentes();
}
    

    private void agregarComponentes() {
        
        ETitulo = new JLabel("Facturación");
        ETitulo.setBounds(235, 10, 350, 50);
        ETitulo.setFont(new Font("Arial", Font.BOLD, 34));
        ETitulo.setForeground(Color.YELLOW);
        add(ETitulo);

        JLabel labelCodigoProducto = new JLabel("Código Producto ");
        labelCodigoProducto.setBounds(65, 80, 150, 30);
        labelCodigoProducto.setForeground(Color.YELLOW);
        labelCodigoProducto.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelCodigoProducto);

        tfCodigoProducto = new JTextField();
        tfCodigoProducto.setBounds(230, 80, 300, 30);
        tfCodigoProducto.setForeground(Color.BLACK);
        tfCodigoProducto.setFont(new Font("TIMES", Font.BOLD, 16));
        add(tfCodigoProducto);

        JLabel labelCantidad = new JLabel("Cantidad ");
        labelCantidad.setBounds(65, 120, 150, 30);
        labelCantidad.setForeground(Color.YELLOW);
        labelCantidad.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelCantidad);

        tfCantidad = new JTextField();
        tfCantidad.setBounds(230, 120, 300, 30);
        tfCantidad.setForeground(Color.BLACK);
        tfCantidad.setFont(new Font("TIMES", Font.BOLD, 16));
        add(tfCantidad);

        btnFacturar = new JButton("Facturar");
        btnFacturar.setBounds(230, 170, 100, 40);
        btnFacturar.setBackground(Color.YELLOW);
        btnFacturar.setForeground(Color.BLACK);
        add(btnFacturar);

        btnListarVentas = new JButton("Listar Ventas");
        btnListarVentas.setBounds(350, 170, 150, 40);
        btnListarVentas.setBackground(Color.YELLOW);
        btnListarVentas.setForeground(Color.BLACK);
        add(btnListarVentas);
        
        btnRegresar = new JButton("VOLVER");
        btnRegresar.setBounds(410,570,180,30);
        btnRegresar.setBackground(Color.YELLOW);
        btnRegresar.setForeground(Color.BLACK);
        btnRegresar.addActionListener(e -> regresarProductoGUI());
        btnRegresar.setForeground(Color.BLACK);
        add(btnRegresar);

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTabla.addColumn("ID Venta");
        modeloTabla.addColumn("Código Producto");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Fecha");

        tablaVentas = new JTable(modeloTabla);
        tablaVentas.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        scrollPane.setBounds(60, 240, 500, 300);
        add(scrollPane);

        // Acción para el botón de facturar
        btnFacturar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String codigoProducto = tfCodigoProducto.getText().trim();
                String cantidadText = tfCantidad.getText().trim();

                if (codigoProducto.isEmpty() || cantidadText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.");
                    return;
                }

                int cantidad;
                try {
                    cantidad = Integer.parseInt(cantidadText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese una cantidad válida.");
                    return;
                }

                // Verificar si el código del producto existe
                if (!productoExiste(codigoProducto)) {
                    JOptionPane.showMessageDialog(null, "El código de producto no existe.");
                    return;
                }

                // Registrar la venta
                registrarVenta(codigoProducto, cantidad);
                limpiarCampos();
            }
        });

        // Acción para listar ventas
        btnListarVentas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarVentas();
            }
        });
    }
    



    
       
       private void regresarProductoGUI() {
    System.out.println("Regresando a ProductoGUI...");
    if (productoGUI != null) {
        productoGUI.setVisible(true); // Muestra ProductoGUI
    }
    System.out.println("Cerrando Transacción...");
    this.dispose(); // Cierra la ventana actual de Transacción
    }


    

    private boolean productoExiste(String codigoProducto) {
        try (Connection connection = ConexionBDMarias.obtenerConexion()) {
            String query = "SELECT COUNT(*) FROM Productos WHERE codigoProducto = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigoProducto);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar el producto: " + e.getMessage());
        }
        return false;
    }
    
    private int obtenerCantidadDisponible(String codigoProducto){
        int cantidadDisponible = 0;
        try (Connection connection = ConexionBDMarias.obtenerConexion()){
            String query = "SELECT cantidadExistente FROM Productos WHERE codigoProducto = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigoProducto);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                cantidadDisponible = resultSet.getInt("cantidadExistente");
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al obtener la cantidad que esta disponilble: " + e.getMessage());
        }
        return cantidadDisponible;
    }

    private void registrarVenta(String codigoProducto, int cantidad) {
        
        int cantidadDisponible = obtenerCantidadDisponible(codigoProducto);
        if(cantidad > cantidadDisponible){
            JOptionPane.showMessageDialog(null, "No hay suficientes productos, solo hay " + cantidadDisponible + " unidades.");
            return;
        }
        
        try (Connection connection = ConexionBDMarias.obtenerConexion()) {
            String query = "INSERT INTO ventas (codigoProducto, cantidad, fecha) VALUES (?, ?, NOW())";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigoProducto);
            preparedStatement.setInt(2, cantidad);
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Venta registrada exitosamente.");
                
                String queryActualizarStock = "UPDATE Productos SET cantidadExistente = cantidadExistente - ? WHERE codigoProducto = ?";
                PreparedStatement preparedStatementActualizar = connection.prepareStatement(queryActualizarStock);
                preparedStatementActualizar.setInt(1, cantidad);
                preparedStatementActualizar.setString(2, codigoProducto);
                int filasAfectadasStock = preparedStatementActualizar.executeUpdate();
                
                if (filasAfectadasStock > 0){
                    JOptionPane.showMessageDialog(null, "Stock actualizado correctamente");
                    
                } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el stock.");
                }
            } else {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar la venta.");
                    }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la venta: " + e.getMessage());
        }
    }
    
    private void procesarVenta(String codigoProducto, int cantidadSolicitada) {
    int cantidadDisponible = obtenerCantidadDisponible(codigoProducto);
    
    if (cantidadDisponible <= 0) {
        JOptionPane.showMessageDialog(null, "Error: No hay stock disponible para el producto " + codigoProducto);
        return; // Detener el proceso de venta
    }
    
    if (cantidadSolicitada > cantidadDisponible) {
        JOptionPane.showMessageDialog(null, "Error: La cantidad solicitada excede el stock disponible.");
       return;// Detener el proceso de venta
    }
    }

    private void listarVentas() {
        modeloTabla.setRowCount(0);
        try (Connection connection = ConexionBDMarias.obtenerConexion()) {
            String query = "SELECT * FROM ventas";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int idVenta = resultSet.getInt("ID_venta");
                String codigoProducto = resultSet.getString("codigoProducto");
                int cantidad = resultSet.getInt("cantidad");
                Timestamp fecha = resultSet.getTimestamp("fecha");
                modeloTabla.addRow(new Object[]{idVenta, codigoProducto, cantidad, fecha});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar las ventas: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        tfCodigoProducto.setText("");
        tfCantidad.setText("");
    }

    // Método main para iniciar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductoGUI productoGUI = new ProductoGUI();
            productoGUI.setVisible(true);
     
        
        });
    }

}
