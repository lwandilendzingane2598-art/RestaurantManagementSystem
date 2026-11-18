package com.restaurant.frames;

import com.restaurant.dao.DatabaseConnection;
import com.restaurant.models.Employee;
import com.restaurant.utils.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;

    public LoginFrame() {
        setTitle("Restaurant Management System – Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(248, 245, 240)); // cream background
        setIconImage(loadLogo());
        initUI();
    }

    
    private Image loadLogo() {
        try {
            URL url = getClass().getResource("/images/logo.png");
            if (url != null) return new ImageIcon(url).getImage();
        } catch (Exception e) {}
        return null;
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 245, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title (logo + text)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        titlePanel.setBackground(new Color(248, 245, 240));
        JLabel logoLabel = new JLabel();
        try {
            URL imgURL = getClass().getResource("/images/logo.png");
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaled = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaled));
            } else {
                logoLabel.setText("🍽️");
                logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
            }
        } catch (Exception e) {
            logoLabel.setText("🍽️");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        }
        titlePanel.add(logoLabel);
        JLabel titleText = new JLabel("Restaurant Management System");
        titleText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleText.setForeground(new Color(25, 55, 85)); // dark blue
        titlePanel.add(titleText);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titlePanel, gbc);

        // Email
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblEmail, gbc);
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Password
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblPass, gbc);
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Login button (dark blue, white text, styled)
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(25, 55, 85)); // dark blue
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> performLogin());
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        // Status label
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(Color.RED);
        gbc.gridy = 4;
        panel.add(lblStatus, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void performLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter email and password");
            return;
        }
 String sql = "SELECT emp_id, full_name, email, role FROM employees_902012598_902012661_902011766 WHERE email=? AND password_hash=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
        
            if (rs.next()) {
                Employee emp = new Employee(
                    rs.getInt("emp_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("role")
                );
            
           
                SessionManager.setLoggedInUser(emp);
                JOptionPane.showMessageDialog(this, "Login successful!");
                new DashboardFrame().setVisible(true);
                dispose();
            } else {
                lblStatus.setText("Invalid email or password");
            }
        } catch (SQLException ex) {
            lblStatus.setText("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}



