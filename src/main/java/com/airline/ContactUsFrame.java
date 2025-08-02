package com.airline;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.awt.*;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;

import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;


public class ContactUsFrame {
    private Color primaryColor = new Color(0x074C83);
    private Color secondaryColor = new Color(0x0A6FC4);
    private Color accentColor = new Color(0xFF6B00);
    static JTextField nameField;
    static JTextField emailField;
    static  JTextArea messageArea;


    static JPanel formPanel;
            JFrame frame;
        
            public ContactUsFrame() {
                // Create the main frame
                frame = new JFrame("Contact Us - PILOT AIR");
                frame.setSize(1000, 700);
                frame.setMinimumSize(new Dimension(900, 600));
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.getContentPane().setBackground(Color.WHITE);
        
                // Header Panel
                createHeaderPanel();
                
                // Navigation Panel
                createNavPanel();
        
                // Main Content Panel
                createContentPanel();
        
                frame.setVisible(true);
            }
        
            private void createHeaderPanel() {
                JPanel headerPanel = new JPanel();
                headerPanel.setBackground(primaryColor);
                headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                headerPanel.setLayout(new BorderLayout());
                
                JLabel titleLabel = new JLabel("PILOT AIR");
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
                
                JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                userPanel.setOpaque(false);
                JLabel userLabel = new JLabel("Welcome, Guest");
                userLabel.setForeground(Color.WHITE);
                userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                userPanel.add(userLabel);
                
                headerPanel.add(titleLabel, BorderLayout.WEST);
                headerPanel.add(userPanel, BorderLayout.EAST);
                
                frame.add(headerPanel, BorderLayout.NORTH);
            }
        
            private void createNavPanel() {
                JPanel navPanel = new JPanel();
                navPanel.setLayout(new GridLayout(5, 1, 0, 10));
                navPanel.setPreferredSize(new Dimension(180, 0));
                navPanel.setBackground(primaryColor);
                navPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
        
                // Navigation buttons
                JButton homeBtn = createNavButton("Home");
                JButton searchBtn = createNavButton("Search Flights");
                JButton bookingsBtn = createNavButton("My Bookings");
                JButton aboutBtn = createNavButton("About");
                JButton contactBtn = createNavButton("Contact");
        
                // Add action listeners
                homeBtn.addActionListener(e -> {
                    frame.dispose();
                    new Home();
                });
                searchBtn.addActionListener(e -> {
                    frame.dispose();
                    new SearchFrame();
                });
                bookingsBtn.addActionListener(e -> {
                    frame.dispose();
                    new MybookingFrame();
                });
                aboutBtn.addActionListener(e -> {
                    frame.dispose();
                    new AboutFrame();
                });
            
                
                navPanel.add(homeBtn);
                navPanel.add(searchBtn);
                navPanel.add(bookingsBtn);
                navPanel.add(aboutBtn);
                navPanel.add(contactBtn);
        
                frame.add(navPanel, BorderLayout.WEST);
            }
        
            private void createContentPanel() {
                // Main panel with padding
                JPanel mainPanel = new JPanel(new BorderLayout());
                mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
                mainPanel.setBackground(Color.WHITE);
        
                // Contact form panel with card styling
                 formPanel = new JPanel();
                formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
                formPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(170, 200, 230)),
                    BorderFactory.createEmptyBorder(40, 40, 40, 40)
                ));
                formPanel.setBackground(new Color(250, 250, 250));
                formPanel.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));
        
                // Form title
                JLabel title = new JLabel("Contact Us");
                title.setFont(new Font("Segoe UI", Font.BOLD, 28));
                title.setForeground(primaryColor);
                title.setAlignmentX(Component.LEFT_ALIGNMENT);
                formPanel.add(title);
                formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
                // Name field
          
                formPanel.add(createFormLabel("Full Name:"));
                formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                 nameField = createFormTextField();
                formPanel.add(nameField);
                formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
                // Email field
                formPanel.add(createFormLabel("Email Address:"));
                formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                emailField = createFormTextField();
                formPanel.add(emailField);
                formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
                // Message field
                formPanel.add(createFormLabel("Your Message:"));
                formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                 messageArea = new JTextArea(6, 30);
                messageArea.setLineWrap(true);
                messageArea.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(messageArea);
                scrollPane.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                formPanel.add(scrollPane);
                formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
                // Submit button
                JButton submitButton = new JButton("Send Message");
                styleSubmitButton(submitButton);
                submitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                formPanel.add(submitButton);
                submitButton.addActionListener(e -> {
                    sendemail();
                });
            
        
                // Center the form panel
                JPanel centerPanel = new JPanel(new GridBagLayout());
                centerPanel.setBackground(Color.WHITE);
                centerPanel.add(formPanel);
                mainPanel.add(centerPanel, BorderLayout.CENTER);
        
                frame.add(mainPanel, BorderLayout.CENTER);
            }
        
            private JLabel createFormLabel(String text) {
                JLabel label = new JLabel(text);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setForeground(new Color(80, 80, 80));
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                return label;
            }
        
            private JTextField createFormTextField() {
                JTextField textField = new JTextField();
                textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                textField.setAlignmentX(Component.LEFT_ALIGNMENT);
                return textField;
            }
        
            private JButton createNavButton(String text) {
                JButton button = new JButton(text);
                button.setFocusPainted(false);
                button.setBackground(primaryColor);
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                button.setHorizontalAlignment(SwingConstants.LEFT);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
                
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
        
            private void styleSubmitButton(JButton button) {
                button.setFocusPainted(false);
                button.setBackground(accentColor);
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Segoe UI", Font.BOLD, 14));
                button.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        button.setBackground(new Color(0xFF8C00));
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        button.setBackground(accentColor);
                    }
                });
            }
            
        
            static void sendemail(){
                String nameinput=nameField.getText();
                String emailinput=emailField.getText();
                String messageinput=messageArea.getText();

                if (nameinput.isEmpty() || emailinput.isEmpty() || messageinput.isEmpty()||!emailinput.matches("^[\\w.-]+@[\\w-]+\\.com$")) {
                    formPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(128, 0, 32),2),
                        BorderFactory.createEmptyBorder(40, 40, 40, 40)
                    ));
                  
                    return;
                }
               

        
                Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host
            props.put("mail.smtp.port", "587"); // TLS Port
            props.put("mail.smtp.auth", "true"); // Enable authentication
            props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
    
            // Replace with your email credentials
            final String username = "inimfonabasi2323@gmail.com";
            final String password = "ojpfuyksnwkfnsox";
    
            // Create session
            Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    
            try {
                // Create message
                Message emailMessage = new MimeMessage(session);
                emailMessage.setFrom(new InternetAddress(username));
                emailMessage.setRecipients(Message.RecipientType.TO, 
                 InternetAddress.parse("inimfonabasi2323@gmail.com")); // Where to send
                emailMessage.setSubject("New Contact Message from " + nameinput);
                
                String emailContent = "Name: " + nameinput + "\n" +
                                    "Email: " + emailinput + "\n\n" +
                                    "Message:\n" + messageinput;
                
                emailMessage.setText(emailContent);
    
                // Send message
                Transport.send(emailMessage);
    
              
         
              formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34),2),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
            // Clear fields after sending
            nameField.setText("");
            emailField.setText("");
            messageArea.setText("");
            
        } catch (MessagingException e) {
          System.out.println(e);
        }
    }
            
    }
