package com.airline.database;

import javax.swing.*;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.airline.SearchFrame;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentUI {
    String from;
    String to;
    String carrier;
    String flightNumber;
      private static final String DB_URL = "jdbc:postgresql://localhost:5432/flight_ticket";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Ini123@@@";
       private com.airline.database.User currentUser;
        private UserSession currentSession;
        static String useremail;
        String price;
    
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
    
    public PaymentUI(String from,String to,String carrier,String flightNumber,String price){
        this.from=from;
        this.to=to;
        this.carrier=carrier;
        this.flightNumber=flightNumber;
        this.price=price;
        // this.currentSession = SessionManager.getCurrentSession();
        // this.currentUser = currentSession.getUser();
        //   // useremail= currentUser.getEmail();
        useremail= "inimfonabasi2323@gmail.com";

        JFrame frame = new JFrame("Flight Payment");
        frame.setSize(400, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // center on screen

        Color primaryColor = new Color(0x074C83);
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Confirm Your Payment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // Route & Price Labels
        JLabel routeLabel = new JLabel("✈️ Route:  "+from+"→ "+to);
        routeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(routeLabel);

        JLabel priceLabel = new JLabel("💰 Price: $540");
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(priceLabel);

        panel.add(Box.createVerticalStrut(20));

        // Card Number
        JLabel cardLabel = new JLabel("Card Number:");
        JTextField cardField = new JTextField();
        cardField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Expiry
        JLabel expiryLabel = new JLabel("Expiry (MM/YY):");
        JTextField expiryField = new JTextField();
        expiryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // CVV
        JLabel cvvLabel = new JLabel("CVV:");
        JPasswordField cvvField = new JPasswordField();
        cvvField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Pay Button
        JButton payButton = new JButton("Pay Now");
        payButton.setBackground(primaryColor);
        payButton.setForeground(Color.WHITE);
        payButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        payButton.setFocusPainted(false);
        payButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        payButton.setMaximumSize(new Dimension(150, 40));
        

        payButton.addActionListener(e -> {
           
            try{
                Connection conn=getConnection();
              
                String imagedb = """
                    CREATE TABLE IF NOT EXISTS flight_booking(
                        id SERIAL PRIMARY KEY,
                        depart VARCHAR(225) NOT NULL ,
                        reach VARCHAR(225) NOT NULL ,
                        carrier VARCHAR(225) NOT NULL ,
                        flightNumber VARCHAR(225) NOT NULL ,
                       email VARCHAR(225) UNIQUE NOT NULL)
                    """;
                
                PreparedStatement preparedStatement=conn.prepareStatement(imagedb);
                preparedStatement.execute();
                System.out.println("Database flight_booking created successfully");
            }catch(SQLException erorrs){
               System.out.println(erorrs);
            }
            if (cardField.getText().isEmpty() || expiryField.getText().isEmpty() || new String(cvvField.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all payment fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "✅ Payment of $540 completed!\nThanks for flying with us!", "Success", JOptionPane.INFORMATION_MESSAGE);
                 
             try {
                Connection conn=getConnection();
                String insertQuery = "INSERT INTO images (depart, reach,carrier,flightNumber,email) VALUES (?, ?,?,?,?) ";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(0, from);
                insertStmt.setString(1, to);
                insertStmt.setString(2, carrier);
                insertStmt.setString(3, flightNumber);
                insertStmt.setString(4, useremail);
               
                insertStmt.executeUpdate();
                System.out.println("Image inserted successfully.");
             } catch (Exception error) {
               System.out.println(e);
             }
            }
        });
       

        // Back Button
        JButton backButton = new JButton("← Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(100, 35));
        backButton.addActionListener(e -> {
            frame.dispose();
           new SearchFrame();
        });

        // Add everything
        panel.add(cardLabel);
        panel.add(cardField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(expiryLabel);
        panel.add(expiryField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(cvvLabel);
        panel.add(cvvField);
        panel.add(Box.createVerticalStrut(20));

        panel.add(payButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}
