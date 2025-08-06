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
        setSize(400, 300);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Welcome - Secure Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0x074C83));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        sendOTPButton = new JButton("Send OTP");
        formPanel.add(sendOTPButton, gbc);

        // OTP panel
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        otpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        otpPanel.add(new JLabel("Enter OTP:"));
        otpField = new JTextField(10);
        otpPanel.add(otpField);
        verifyButton = new JButton("Verify & Login");
        otpPanel.add(verifyButton);
        otpPanel.setVisible(false);
        formPanel.add(otpPanel, gbc);

        // Status label
        gbc.gridy = 3; gbc.gridwidth = 2;
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.GRAY);
        formPanel.add(statusLabel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        sendOTPButton.addActionListener(e -> handleSendOTP());
        verifyButton.addActionListener(e -> handleVerifyOTP());

        setLocationRelativeTo(null);
    }

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
