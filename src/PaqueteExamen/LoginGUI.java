package PaqueteExamen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

public class LoginGUI extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonLogin;
    private JLabel pie;
    private JLabel ETitulo;

    public LoginGUI() {
        setTitle("Login - Marías");
        setBounds(730, 250, 500, 600); // Tamaño y posición de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        // Configuración del ícono de la ventana
        Image icon = Toolkit.getDefaultToolkit().getImage("logo1.jpg");
        setIconImage(icon);
        getContentPane().setBackground(Color.BLACK);

        agregarComponentes();
    }

    private void agregarComponentes() {
        // Título de la ventana
        ETitulo = new JLabel("INICIO DE SESIÓN - TIENDA 'MARIAS'");
        ETitulo.setBounds(80, 10, 500, 50);
        ETitulo.setFont(new Font("Times", Font.BOLD, 18));
        ETitulo.setForeground(Color.YELLOW);
        add(ETitulo);

        // Imagen del logo
        ImageIcon Logo = new ImageIcon("logo1.jpg");
        Image Original = Logo.getImage();
        Image imagenDimensionada = Original.getScaledInstance(370, 270, Image.SCALE_SMOOTH);
        ImageIcon logoDimensionado = new ImageIcon(imagenDimensionada);

        JLabel logoLabel = new JLabel(logoDimensionado);
        logoLabel.setBounds(50, 90, 400, 200);
        add(logoLabel);

        
        JLabel etiquetaUsuario = new JLabel("Usuario:");
        etiquetaUsuario.setBounds(60, 320, 100, 20);
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaUsuario.setForeground(Color.YELLOW);
        add(etiquetaUsuario);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(60, 350, 380, 35);
        campoUsuario.setFont(new Font("Times", Font.PLAIN, 16));
        add(campoUsuario);

        JLabel etiquetaContrasena = new JLabel("Contraseña:");
        etiquetaContrasena.setBounds(60, 405, 100, 20);
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaContrasena.setForeground(Color.YELLOW);
        add(etiquetaContrasena);

        campoContrasena = new JPasswordField();
        campoContrasena.setBounds(60, 430, 380, 35);
        campoContrasena.setFont(new Font("Times", Font.PLAIN, 16));
        add(campoContrasena);

        botonLogin = new JButton("Ingresar");
        botonLogin.setBounds(60, 480, 100, 30);
        botonLogin.setBackground(Color.YELLOW);
        botonLogin.setForeground(Color.BLACK);
        add(botonLogin);

        pie = new JLabel("Odalis Cajbón");
        pie.setBounds(215, 530, 400, 20);
        pie.setForeground(Color.YELLOW);
        add(pie);

        // Acción del botón de inicio de sesión
        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                   {
                    JOptionPane.showMessageDialog(null, "Login exitoso");
                    new ProductoGUI().setVisible(true);
                    dispose();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}

