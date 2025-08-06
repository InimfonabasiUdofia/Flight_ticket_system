package com.airline;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.airline.database.Home;
import com.airline.database.LoginGUI;
import com.airline.database.MybookingFrame;

import java.awt.*;

public class AboutFrame {
    private Color primaryColor = new Color(0x074C83);
    private Color secondaryColor = new Color(0x0A6FC4);
    
    public AboutFrame() {
        // Create the frame
        JFrame frame = new JFrame(" PILOT AIR-About");
        frame.setSize(1000, 700);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Header Panel (reuse your existing header style)
        JPanel headerPanel = createHeaderPanel();
        frame.add(headerPanel, BorderLayout.NORTH);

        // Navigation Panel (reuse your existing nav)
        JPanel navPanel = createNavPanel(frame);
        frame.add(navPanel, BorderLayout.WEST);

        // Main Content Panel
        JPanel contentPanel = createContentPanel();
        frame.add(contentPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(primaryColor);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("PILOT AIR");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userPanel.add(userLabel);
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(userPanel, BorderLayout.EAST);
        
        return panel;
    }

    private JPanel createNavPanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 0, 10));
        panel.setPreferredSize(new Dimension(180, 0));
        panel.setBackground(primaryColor);
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Navigation buttons (reuse your existing button style)
        panel.add(createNavButton("Home", () -> {
            frame.dispose();
            new Home();
        }));
        
        panel.add(createNavButton("Search Flights", () -> {
            frame.dispose();
            new SearchFrame();
        }));
        
        panel.add(createNavButton("My Bookings", () -> {
            frame.dispose();
            new MybookingFrame();
        }));
        
        panel.add(createNavButton("About", () -> {
           
        }));
        
        panel.add(createNavButton("Contact", () -> {
            frame.dispose();
            new ContactUsFrame();
        }));
        panel.add(createNavButton("Logout", () -> {
            frame.dispose();
            new LoginGUI();
        }));
        
        
        return panel;
    }

    private JButton createNavButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 15, 10, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> action.run());
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(secondaryColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor);
            }
        });
        
        return button;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        panel.setBackground(Color.WHITE);

        // Logo and Title
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            ImageIcon originalLogo = new ImageIcon(getClass().getResource("/logo.png"));
            Image scaledLogo = originalLogo.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            JLabel logoPlaceholder = new JLabel("PILOT AIR LOGO");
            logoPlaceholder.setFont(new Font("Segoe UI", Font.BOLD, 24));
            logoPlaceholder.setForeground(primaryColor);
            logoPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoPanel.add(logoPlaceholder);
        }

        logoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // About Text
        JTextArea aboutText = new JTextArea(
            "PILOT AIR - Your Trusted Airline\n\n" +
            "Founded in 2025, PILOT AIR has quickly become a leader in Paris ,comfortable and affordable air travel.\n\n" +
            " we're committed to providing exceptional service with\n" +
            "modern aircraft and professional crew.\n\n" +
            "Safety Ratings: ★★★★★\n" +
            "Customer Satisfaction: 98%\n\n" +
            "© 2025 PILOT AIR. All rights reserved."
        );
        aboutText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aboutText.setForeground(new Color(80, 80, 80));
        aboutText.setEditable(false);
        aboutText.setOpaque(false);
        aboutText.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutText.setBorder(new EmptyBorder(20, 40, 20, 40));

        logoPanel.add(aboutText);
        panel.add(logoPanel, BorderLayout.CENTER);

        return panel;
    }

  
}