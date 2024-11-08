package PaqueteExamen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductoGUI extends JFrame {

    private JTextField tfCodigo, tfNombre, tfPrecio, tfCantidad;
    private JButton btnInsertar, btnActualizar, btnEliminar, btnListar, btnabrirTransaccion ,btnbuscarCodigo, btnbuscarNombre;
    private JTextField tfbuscarCodigo, tfbuscarNombre;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JLabel ETitulo;
    private ImageIcon logoIcon;

    public ProductoGUI() {

        setTitle("Gestión de Productos");
        setBounds(730, 250, 650, 840); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        


        // Configurar icono de la ventana
        logoIcon = new ImageIcon("logo1.jpg");
        setIconImage(logoIcon.getImage());

        // Fondo y estilo de la ventana
        getContentPane().setBackground(Color.BLACK);

        agregarComponentes();

        btnInsertar.setBackground(Color.YELLOW);
        btnInsertar.setForeground(Color.BLACK);
        btnActualizar.setBackground(Color.YELLOW);
        btnActualizar.setForeground(Color.BLACK);
        btnEliminar.setBackground(Color.YELLOW);
        btnEliminar.setForeground(Color.BLACK);
        btnListar.setBackground(Color.YELLOW);
        btnListar.setForeground(Color.BLACK);
        btnabrirTransaccion.setBackground(Color.YELLOW);
        btnabrirTransaccion.setForeground(Color.BLACK);
        btnbuscarCodigo.setBackground(Color.YELLOW);
        btnbuscarCodigo.setForeground(Color.BLACK);
        btnbuscarNombre.setBackground(Color.YELLOW);
        btnbuscarNombre.setForeground(Color.BLACK);

        // Acciones de los botones
        btnbuscarCodigo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String codigo = tfbuscarCodigo.getText().trim();

                if (codigo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese el código del producto.");
                    return;
                }

                buscarProductoPorCodigo(codigo);  // Llamamos a la función de búsqueda por código
            }
        });
        
        btnbuscarNombre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = tfbuscarNombre.getText().trim();

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese el nombre del producto.");
                    return;
                }
                buscarProductoPorNombre(nombre);  
            }
        });
        
        btnInsertar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String codigo = tfCodigo.getText().trim();
                    String nombre = tfNombre.getText().trim();
                    String precioText = tfPrecio.getText().trim();
                    String cantidadText = tfCantidad.getText().trim();

                    if (codigo.isEmpty() || nombre.isEmpty() || precioText.isEmpty() || cantidadText.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.");
                        return;
                    }

                    double precio = Double.parseDouble(precioText);
                    int cantidad = Integer.parseInt(cantidadText);

                    // Llamar a la función para insertar el producto
                    ConexionBDMarias.insertarProducto(codigo, nombre, precio, cantidad);

                    // Limpiar campos de entrada después de insertar
                   limpiarCampos();
     
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese valores válidos en los campos de precio y cantidad.");
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String codigo = tfCodigo.getText();
                String nombre = tfNombre.getText();
                double precio = 0;
                int cantidad = 0;
                try {
                    precio = Double.parseDouble(tfPrecio.getText());
                    cantidad = Integer.parseInt(tfCantidad.getText());
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un precio valido.");
                    return;
                }
                if (codigo.isEmpty()|| nombre.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Por porfa complete todos los campos");
                    return;
                }
                ConexionBDMarias.actualizarProducto(codigo, nombre, precio, cantidad);
                
                tfCodigo.setText("");
                tfNombre.setText("");
                tfPrecio.setText("");
                tfCantidad.setText("");
                        
                buscarProductoPorCodigo(codigo);
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String codigo = tfCodigo.getText();
                
                if (codigo.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Por favor ingrese el codigo del producto");
                    return;
                }
                ConexionBDMarias.eliminarProducto(codigo);
                
                tfCodigo.setText("");
                tfNombre.setText("");
                tfPrecio.setText("");
                tfCantidad.setText("");
                
                listarProductos();
            }
        });

        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarProductos();
            }
        });

        
        btnabrirTransaccion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Transacción transaccion = new Transacción(ProductoGUI.this);
                transaccion.setVisible(true);
                dispose(); 
            }
        });
    }

    private void agregarComponentes() {
        
        ETitulo = new JLabel("Gestión de Productos");
        ETitulo.setBounds(220, 10, 350, 50);
        ETitulo.setFont(new Font("Arial", Font.BOLD, 24));
        ETitulo.setForeground(Color.YELLOW);
        add(ETitulo);

        JLabel labelCodigo = new JLabel("Código Producto ");
        labelCodigo.setBounds(50, 80, 150, 30);
        labelCodigo.setForeground(Color.YELLOW);
        labelCodigo.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelCodigo);

        tfCodigo = new JTextField();
        tfCodigo.setBounds(200, 80, 300, 30);
        tfCodigo.setForeground(Color.BLACK);
        tfCodigo.setFont(new Font("TIMES", Font.BOLD, 16));
        add(tfCodigo);

        JLabel labelNombre = new JLabel("Nombre Producto ");
        labelNombre.setBounds(50, 120, 150, 30);
        labelNombre.setForeground(Color.YELLOW);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelNombre);

        tfNombre = new JTextField();
        tfNombre.setBounds(200, 120, 300, 30);
        tfNombre.setForeground(Color.BLACK);
        tfNombre.setFont(new Font("Times", Font.BOLD, 16));
        add(tfNombre);

        JLabel labelPrecio = new JLabel("Precio Producto ");
        labelPrecio.setBounds(50, 160, 150, 30);
        labelPrecio.setForeground(Color.YELLOW);
        labelPrecio.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelPrecio);

        tfPrecio = new JTextField();
        tfPrecio.setBounds(200, 160, 300, 30);
        tfPrecio.setForeground(Color.BLACK);
        tfPrecio.setFont(new Font("Times", Font.BOLD, 16));
        add(tfPrecio);

        JLabel labelCantidad = new JLabel("Cantidad Producto ");
        labelCantidad.setBounds(50, 200, 150, 30);
        labelCantidad.setForeground(Color.YELLOW);
        labelCantidad.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelCantidad);

        tfCantidad = new JTextField();
        tfCantidad.setBounds(200, 200, 300, 30);
        tfCantidad.setForeground(Color.BLACK);
        tfCantidad.setFont(new Font("TIMES", Font.BOLD, 16));
        add(tfCantidad);
        
        JLabel labelbuscarCodigo = new JLabel ("Buscar por codigo");
        labelbuscarCodigo.setForeground(Color.YELLOW);
        labelbuscarCodigo.setBounds(50, 340, 150, 30);
        labelbuscarCodigo.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelbuscarCodigo);
        
        tfbuscarCodigo = new JTextField();
        tfbuscarCodigo.setBounds(220, 340, 200, 30);
        tfbuscarCodigo.setForeground(Color.BLACK);
        tfbuscarCodigo.setFont(new Font("Times", Font.BOLD, 16));
        add(tfbuscarCodigo);
        
        btnbuscarCodigo = new JButton ("Buscar");
        btnbuscarCodigo.setBounds(450, 340, 100, 30);
        add(btnbuscarCodigo);
        
        JLabel labelbuscarNombre = new JLabel ("Buscar por nombre");
        labelbuscarNombre.setBounds(50, 400, 150, 30);
        labelbuscarNombre.setForeground(Color.YELLOW);
        labelbuscarNombre.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelbuscarNombre);
        
        tfbuscarNombre = new JTextField();
        tfbuscarNombre.setBounds(220, 400, 200, 30);
        tfbuscarNombre.setForeground(Color.BLACK);
        tfbuscarNombre.setFont(new Font("Arial", Font.BOLD,16));
        add(tfbuscarNombre);
        
        btnbuscarNombre = new JButton ("Buscar");
        btnbuscarNombre.setBounds(450, 400, 100, 30);
        add(btnbuscarNombre);
        
        btnInsertar = new JButton("Insertar");
        btnInsertar.setBounds(60, 250, 100, 40);
        add(btnInsertar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(170, 250, 100, 40);
        add(btnActualizar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(280, 250, 100, 40);
        add(btnEliminar);

        btnListar = new JButton("Listar Productos");
        btnListar.setBounds(390, 250, 150, 40);
        add(btnListar);
        
        btnabrirTransaccion = new JButton("Transacción");
        btnabrirTransaccion.setBounds(505, 20, 110, 40);
        add(btnabrirTransaccion);
        
        modeloTabla = new DefaultTableModel(){
        @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
                };
        
        modeloTabla.addColumn("Codigo");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Cantidad");
        
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBounds(60, 470, 500, 300);
        add(scrollPane);
        
        
    }

    public void listarProductos() {
        modeloTabla.setRowCount(0);  
        try (Connection conn = ConexionBDMarias.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {

            while (rs.next()) {
                String codigo = rs.getString("codigoProducto");
                String nombre = rs.getString("nombreProducto");
                double precio = rs.getDouble("precioUnitario");
                int cantidad = rs.getInt("cantidadExistente");
                modeloTabla.addRow(new Object[]{codigo, nombre, precio, cantidad});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void limpiarCampos(){
       tfCodigo.setText("");
        tfNombre.setText("");
        tfPrecio.setText("");
        tfCantidad.setText("");
        tfbuscarCodigo.setText("");
        tfbuscarNombre.setText("");
    }
    
    private void buscarProductoPorCodigo(String codigo){
        modeloTabla.setRowCount(0);
        try (Connection conn = ConexionBDMarias.obtenerConexion();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM productos WHERE codigoProducto = '" + codigo + "'")){
            while (rs.next()){
                String nombre = rs.getString("nombreProducto");
                double precio = rs.getDouble("precioUnitario");
                int cantidad = rs.getInt("cantidadExistente");
                modeloTabla.addRow(new Object[]{codigo, nombre, precio, cantidad});
            }
            if (modeloTabla.getRowCount() == 0){
                    JOptionPane.showMessageDialog(null, "No se encontro ningun producto con ese codigo");
                }
            
        } catch (SQLException e){
            e.printStackTrace();
        } finally{
            tfbuscarCodigo.setText("");
        }
        
}
    
    private void buscarProductoPorNombre(String nombre){
        modeloTabla.setRowCount(0);
        try(Connection conn = ConexionBDMarias.obtenerConexion();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM productos WHERE nombreProducto LIKE '%" + nombre + "%'")) {
                
                while (rs.next()){
                    String codigo = rs.getString("codigoProducto");
                    String nombreProducto = rs.getString("nombreProducto");
                    double precio = rs.getDouble("precioUnitario");
                    int cantidad = rs.getInt("cantidadExistente");
                    modeloTabla.addRow(new Object[]{codigo, nombre, precio, cantidad});
                }
                if (modeloTabla.getRowCount() == 0){
                    JOptionPane.showMessageDialog(null, "No se encontro ningun producto con ese name");
                }
                
        }catch (SQLException e){
            e.printStackTrace();
        } finally{
            tfbuscarNombre.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductoGUI().setVisible(true); 
    }
});
}
}
