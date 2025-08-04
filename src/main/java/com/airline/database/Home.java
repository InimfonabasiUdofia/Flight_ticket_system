package com.airline.database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.w3c.dom.events.MouseEvent;

import com.airline.AboutFrame;
import com.airline.ContactUsFrame;
import com.airline.MybookingFrame;
import com.airline.SearchFrame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Home {
    JFrame frame;
    private static JPanel contentPanel;
    private Color primaryColor = new Color(0x074C83);
    private Color secondaryColor = new Color(0x0A6FC4);
    private Color accentColor = new Color(0xFF6B00);
    File selectedFile ;
    String image="C:\\Users\\INIMFON-ABASI\\Documents\\IMG20250708114027.jpg";
     private static final String DB_URL = "jdbc:postgresql://localhost:5432/flight_ticket";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Ini123@@@";
    static String useremail;
    private com.airline.database.User currentUser;
    private UserSession currentSession;
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
    

    public Home() {
       
       
        // this.currentSession = SessionManager.getCurrentSession();
        // this.currentUser = currentSession.getUser();
        // useremail= currentUser.getEmail();
        useremail= null;
        letgo();
        createFrame();
        createNavPanel();
        createHomeContent();
        frame.setVisible(true);
        System.out.println(useremail);
    }
    static byte[]  getimg(){
        Connection conn = null;
        PreparedStatement selectStmt = null;
        ResultSet rs = null;
            try {
                conn = getConnection();
                String selectQuery = "SELECT * FROM images WHERE email = ?";
                selectStmt = conn.prepareStatement(selectQuery);
                selectStmt.setString(1, useremail);
                rs = selectStmt.executeQuery();
                if (rs.next()) {
                   
                    return rs.getBytes("image_data");
                   
                } 
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                // Properly close resources
                try { if (rs != null) rs.close(); } catch (SQLException e) { /* log */ }
                try { if (selectStmt != null) selectStmt.close(); } catch (SQLException e) { /* log */ }
                try { if (conn != null) conn.close(); } catch (SQLException e) { /* log */ }
            }
           
                return new byte[0];
      }


    private void createFrame() {
        frame = new JFrame("Airline Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);
        
        // Add header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 60)); // Fixed 60px height
        
        // App Title
        JLabel titleLabel = new JLabel("PILOT AIR");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        
        // User Panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);
        
        // Circular Image Avatar
      



        JButton avatarLabel = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                // Load your image (replace with actual path)
                byte[] imageData = getimg();
                ImageIcon icon ;
                if (imageData == null) {
                    // Explicit null case - use a default placeholder image
                     icon = new ImageIcon( "logo.png");
                   
                } else if (imageData.length == 0) {
                    // Empty byte array case - use logo
                     icon = new ImageIcon( "logo.png");
                } else {
                    // Valid image data case
                     icon = new ImageIcon(imageData);
                 
                }
              
                Image img = icon.getImage();
                
                // Create circular clip
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create circle
                int diameter = Math.min(getWidth(), getHeight());
                Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter, diameter);
                g2.setClip(circle);
                
                // Draw image centered
                int x = (getWidth() - img.getWidth(null)) / 2;
                int y = (getHeight() - img.getHeight(null)) / 2;
                g2.drawImage(img, x, y, null);
                
                // Optional: Add white border
                g2.setClip(null);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(0, 0, diameter, diameter);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40); // Fixed avatar size
            }
        };
        
        // Ensure avatar stays centered vertically
        JPanel avatarContainer = new JPanel(new GridBagLayout());
        avatarContainer.setOpaque(false);
        avatarContainer.add(avatarLabel);
        
        userPanel.add(avatarContainer);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        frame.add(headerPanel, BorderLayout.NORTH);
        
        avatarLabel.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image Files", "jpg", "jpeg", "png", "gif");
            fileChooser.setFileFilter(filter);
    
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
    
                try {
                    byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
                    System.out.println("Image size: " + imageBytes.length + " bytes");
    
                    Connection conn = getConnection();
                    
                    // Check if user already has an image
                    String selectQuery = "SELECT * FROM images WHERE email = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                    selectStmt.setString(1, useremail);
                    ResultSet rs = selectStmt.executeQuery();
    
                    if (rs.next()) {
                        // Update existing image
                        String updateQuery = "UPDATE images SET image_data = ? WHERE email = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                        updateStmt.setBytes(1, imageBytes);
                        updateStmt.setString(2, useremail);
                        updateStmt.executeUpdate();
                        System.out.println("Image updated successfully.");
                    } else {
                        // Insert new image
                        String insertQuery = "INSERT INTO images (email, image_data) VALUES (?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                        insertStmt.setString(1, useremail);
                        insertStmt.setBytes(2, imageBytes);
                        insertStmt.executeUpdate();
                        System.out.println("Image inserted successfully.");
                    }
    
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });
        
    }
    
 
  public void letgo(){
  
     try{
         Connection conn=getConnection();
         String imagedb="""
             CREATE TABLE IF NOT EXISTS images(
             image_data BYTEA ,
             email VARCHAR(225) UNIQUE NOT NULL
             )
         """;
         PreparedStatement preparedStatement=conn.prepareStatement(imagedb);
         preparedStatement.execute();
         System.out.println("Database images created successfully");
     }catch(SQLException e){
        System.out.println(e);
     }
  }
 

    private void createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(5, 1, 0, 10));
        navPanel.setPreferredSize(new Dimension(180, 0));
        navPanel.setBackground(primaryColor);
        navPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Buttons with icons
        JButton homeBtn = createNavButton("Home");
        JButton searchBtn = createNavButton("Search Flights");
        JButton bookingsBtn = createNavButton("My Bookings");
        JButton aboutBtn = createNavButton("About");
        JButton contactBtn = createNavButton("Contact");

        searchBtn.addActionListener(e -> {
            frame.dispose();
            SearchFrame contact = new SearchFrame();
        });
        bookingsBtn.addActionListener(e -> {
            frame.dispose();
            MybookingFrame contact = new MybookingFrame();
        });
        aboutBtn.addActionListener(e -> {
            frame.dispose();
            AboutFrame contact = new AboutFrame();
        });
        contactBtn.addActionListener(e -> {
            frame.dispose();
            ContactUsFrame contact = new ContactUsFrame();
        });
        
        navPanel.add(homeBtn);
        navPanel.add(searchBtn);
        navPanel.add(bookingsBtn);
        navPanel.add(aboutBtn);
        navPanel.add(contactBtn);

        frame.add(navPanel, BorderLayout.WEST);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton( text);
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

    private void createHomeContent() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Hero section
        JPanel heroPanel = new JPanel(new BorderLayout());
        heroPanel.setBackground(new Color(240, 245, 250));
        heroPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        ImageIcon logIcon = new ImageIcon(getClass().getResource("/logo.png"));
        Image resizedImage = logIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedlogo = new ImageIcon(resizedImage);
      
    
        JLabel welcomeLabel = new JLabel("Welcome to SkyWings Airlines", resizedlogo, SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(primaryColor);
        
        JLabel subLabel = new JLabel("Find the best flight deals to destinations around the world");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subLabel.setForeground(new Color(80, 80, 80));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.setOpaque(false);
        textPanel.add(welcomeLabel);
        textPanel.add(subLabel);
        
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/plane.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(350, 250, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        heroPanel.add(textPanel, BorderLayout.WEST);
        heroPanel.add(imageLabel, BorderLayout.EAST);
        
        // Quick search panel
        JPanel quickSearchPanel = new JPanel();
        quickSearchPanel.setLayout(new BoxLayout(quickSearchPanel, BoxLayout.X_AXIS));
        quickSearchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        quickSearchPanel.setBackground(Color.WHITE);
        quickSearchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField searchField = new JTextField(20);
        searchField.setMaximumSize(new Dimension(300, 40));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JButton searchButton = new JButton("Search Flights");
        styleActionButton(searchButton);
        
        quickSearchPanel.add(searchField);
        quickSearchPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        quickSearchPanel.add(searchButton);
        
        // Special offers section
        JPanel offersPanel = createOffersSection();
        
        // Add components to content panel
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        
        mainContent.add(heroPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContent.add(quickSearchPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContent.add(offersPanel);
        
        contentPanel.add(mainContent, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createOffersSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel title = new JLabel("Special Offers");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(primaryColor);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Sample offer cards
        String[] destinations = {"Paris", "Tokyo", "New York", "Bali"};
        String[] prices = {"$499", "$799", "$649", "$599"};
        String[] dates = {"May 15-30, 2023", "Jun 1-15, 2023", "Jul 10-25, 2023", "Aug 5-20, 2023"};
        
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.X_AXIS));
        cardsPanel.setBackground(Color.WHITE);
        
        for (int i = 0; i < destinations.length; i++) {
            JPanel card = createOfferCard(destinations[i], prices[i], dates[i]);
            cardsPanel.add(card);
            if (i < destinations.length - 1) {
                cardsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
            }
        }
        
        panel.add(cardsPanel);
        return panel;
    }

    private JPanel createOfferCard(String destination, String price, String dates) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(200, 200));
        
        JLabel destLabel = new JLabel(destination);
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        destLabel.setForeground(primaryColor);
        
        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        priceLabel.setForeground(accentColor);
        
        JLabel dateLabel = new JLabel(dates);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        JButton bookButton = new JButton("Book Now");
        styleCardButton(bookButton);
        
        card.add(destLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(priceLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(dateLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(bookButton);
        
        return card;
    }

    private void styleActionButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(accentColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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

    private void styleCardButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(primaryColor);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
    }

}
   
