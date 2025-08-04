package com.airline.database;

import javax.swing.*;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class LoginGUI extends JFrame {
    private OTPService otpService = new OTPService();
    private EmailService emailService = new EmailService("inimfonabasi2323@gmail.com", "ojpfuyksnwkfnsox");
    
    private JTextField emailField;
    private JTextField otpField;
    private JButton sendOTPButton;
    private JButton verifyButton;
    private JLabel statusLabel;
    private JPanel otpPanel;
    public String currentEmail;
    
    public LoginGUI() {
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Login with Email OTP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create main panel with border and layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Email input
        mainPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);
        
        // Send OTP button
        sendOTPButton = new JButton("Send OTP");
        gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(sendOTPButton, gbc);
        
        // OTP panel
        otpPanel = new JPanel(new FlowLayout());
        otpPanel.add(new JLabel("Enter OTP:"));
        otpField = new JTextField(10);
        otpPanel.add(otpField);
        verifyButton = new JButton("Verify & Login");
        otpPanel.add(verifyButton);
        otpPanel.setVisible(false);
        
        gbc.gridy = 2; gbc.gridwidth = 2; gbc.gridx = 0;
        mainPanel.add(otpPanel, gbc);
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        mainPanel.add(statusLabel, gbc);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Event listeners
        sendOTPButton.addActionListener(e -> handleSendOTP());
        verifyButton.addActionListener(e -> handleVerifyOTP());
        
        pack();
        setLocationRelativeTo(null);
    }
        
        // Event listeners
   
    
    
    private void handleSendOTP() {
        String email = emailField.getText().trim();
        if (email.isEmpty() || !email.contains("@")) {
            showStatus("Please enter a valid email", Color.RED);
            return;
        }
        
        currentEmail = email;
        sendOTPButton.setEnabled(false);
        showStatus("Sending OTP...", Color.BLUE);
        
        CompletableFuture.supplyAsync(() -> {
            String otp = otpService.generateOTP(email);
            return emailService.sendOTP(email, otp);
        }).thenAccept(success -> {
            SwingUtilities.invokeLater(() -> {
                if (success) {
                    showStatus("OTP sent to " + email, Color.GREEN);
                    otpPanel.setVisible(true);
                    pack();
                } else {
                    showStatus("Failed to send OTP", Color.RED);
                }
                sendOTPButton.setEnabled(true);
            });
        });
    }
    
    private void handleVerifyOTP() {
        String otp = otpField.getText().trim();
        if (otp.isEmpty()) {
            showStatus("Please enter the OTP", Color.RED);
            return;
        }
        
        if (otpService.validateOTP(currentEmail, otp)) {
            showStatus("Login successful!", Color.GREEN);
            
            User user = DatabaseManager.getOrCreateUser(currentEmail);
            if (user != null) {
                UserSession session = DatabaseManager.createSession(user);
                if (session != null) {
                    SessionManager.setCurrentSession(session);
                    
                    Timer timer = new Timer(1000, e -> {
                        new Home();
                        this.dispose();
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        } else {
            showStatus("Invalid or expired OTP", Color.RED);
            otpField.setText("");
        }
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseManager.initializeTables();
            new LoginGUI().setVisible(true);
        });
    }
}
