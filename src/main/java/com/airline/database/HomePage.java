package com.airline.database;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.*;
class HomePage extends JFrame {
    private User currentUser;
    private UserSession currentSession;
    
    public HomePage() {
        this.currentSession = SessionManager.getCurrentSession();
        this.currentUser = currentSession.getUser();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Home - " + currentUser.getEmail());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(63, 81, 181));
        header.add(new JLabel("Welcome: " + currentUser.getEmail()));
        header.getComponent(0).setForeground(Color.WHITE);
        add(header, BorderLayout.NORTH);
        
        // Main content
        JPanel main = new JPanel(new GridLayout(2, 2, 10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton profileBtn = new JButton("My Profile");
        JButton settingsBtn = new JButton("Settings");
        JButton dataBtn = new JButton("My Data");
        JButton logoutBtn = new JButton("Logout");
        
        main.add(profileBtn);
        main.add(settingsBtn);
        main.add(dataBtn);
        main.add(logoutBtn);
        
        add(main, BorderLayout.CENTER);
        
        // Event listeners
        profileBtn.addActionListener(e -> showProfile());
        logoutBtn.addActionListener(e -> handleLogout());
        
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    
    private void showProfile() {
        JOptionPane.showMessageDialog(this, 
            "User ID: " + currentUser.getId() + "\nEmail: " + currentUser.getEmail() + 
            "\nSession: " + currentSession.getSessionId().substring(0, 8) + "...");
    }
    
    private void handleLogout() {
        if (JOptionPane.showConfirmDialog(this, "Logout?") == JOptionPane.YES_OPTION) {
            SessionManager.logout();
            this.dispose();
            new LoginGUI().setVisible(true);
        }
    }
}