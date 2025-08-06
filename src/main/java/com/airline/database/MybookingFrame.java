package com.airline.database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.airline.AboutFrame;
import com.airline.ContactUsFrame;
import com.airline.SearchFrame;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class MybookingFrame {
    JFrame frame = new JFrame();
    private Color primaryColor = new Color(0x074C83);
    private Color secondaryColor = new Color(0x0A6FC4);
    static String deptime ;
    static String arrtime;
    static String carrier;
     static String flightNumber;
    

    public MybookingFrame() {
        frame = new JFrame("My Bookings - PILOT AIR");
        frame.setSize(1000, 700);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        createNavPanel();
        createHeaderPanel();
        createMainPanel();

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
        JLabel userLabel = new JLabel("");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userPanel.add(userLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);
    }

    private void createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(6, 1, 0, 10));
        navPanel.setPreferredSize(new Dimension(180, 0));
        navPanel.setBackground(primaryColor);
        navPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Assuming you have a frame reference called 'frame'

JButton homeButton = createNavButton("Home");
homeButton.addActionListener(e -> {
    frame.dispose();
   new Home();
});
navPanel.add(homeButton);

JButton searchFlightsButton = createNavButton("Search Flights");
searchFlightsButton.addActionListener(e -> {
    frame.dispose();
    new SearchFrame();
});
navPanel.add(searchFlightsButton);

JButton myBookingsButton = createNavButton("My Bookings");
myBookingsButton.addActionListener(e -> {
    
});
navPanel.add(myBookingsButton);

JButton aboutButton = createNavButton("About");
aboutButton.addActionListener(e -> {
    frame.dispose();
    new AboutFrame();
});
navPanel.add(aboutButton);

JButton contactButton = createNavButton("Contact");
contactButton.addActionListener(e -> {
    frame.dispose();
    new ContactUsFrame();
});
navPanel.add(contactButton);
JButton logout = createNavButton("Logout");
logout.addActionListener(e -> {
    frame.dispose();
    new LoginGUI();
});
navPanel.add(logout);


        frame.add(navPanel, BorderLayout.WEST);
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
    public String getCityName(String iataCode) {
        switch(iataCode) {
            // Europe
            case "CDG": return "Paris(Charles de Gaulle)";
            case "LHR": return "London (Heathrow)";
            case "FRA": return "Frankfurt";
            case "AMS": return "Amsterdam";
            case "MAD": return "Madrid";
            case "BCN": return "Barcelona";
            case "FCO": return "Rome";
            case "BRU": return "Brussels";
            case "ZRH": return "Zurich";
            case "VIE": return "Vienna";
            
            // North America
            case "JFK": return "New York";
            case "LAX": return "Los Angeles";
            case "ORD": return "Chicago";
            case "YYZ": return "Toronto";
            case "YUL": return "Montreal";
            case "MIA": return "Miami";
            case "SFO": return "San Francisco";
            case "DFW": return "Dallas";
            
            // Other regions
            case "DXB": return "Dubai";
            case "HKG": return "Hong Kong";
            case "SIN": return "Singapore";
            case "NRT": return "Tokyo";
            case "SYD": return "Sydney";
            case "ORY": return "Paris (Orly)";
            case "DUB": return "Dublin";   
            case "WAW": return "Warsaw (Chopin)";
            case "BEG": return "Belgrade (Nikola Tesla)";
            case "CMN": return "Casablanca (Mohammed V)";
            case "IST": return "Istanbul";
            case "TK": return "Turkish Airlines Flight"; // For codes like TK11
            case "KEF": return "Reykjavik (Keflavik)";
            case "TP": return "TAP Portugal Flight"; // For codes like TP209
            case "LGW": return "London (Gatwick)";
            case "NCE": return "Nice (Côte d'Azur)";
            // Add more as needed...
            
            default: return iataCode; // Return the code if city not found
        }
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 20, 20, 20));

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/flight_ticket", "postgres", "Ini123@@@");
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM flight_booking";
            ResultSet rs = stmt.executeQuery(query);

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String from = rs.getString("depart");
                String to = rs.getString("reach");
                String price = rs.getString("price");
                String email = rs.getString("email");
                deptime = rs.getString("deptime");
                arrtime = rs.getString("arrtime");
                 carrier = rs.getString("carrier");
                 flightNumber = rs.getString("flightNumber");

                JPanel bookingCard = createBookingCard( getCityName(from) + " → " + getCityName(to), price, email);
                mainPanel.add(bookingCard);
                mainPanel.add(Box.createVerticalStrut(20));
            }

            if (!hasResults) {
                JLabel noBookings = new JLabel("No bookings found.");
                noBookings.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                noBookings.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(noBookings);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JLabel error = new JLabel("Error loading bookings.");
            error.setForeground(Color.RED);
            error.setFont(new Font("Segoe UI", Font.BOLD, 14));
            error.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(error);
        }

       

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        frame.add(scrollPane, BorderLayout.CENTER);
    }
     public static String convertToHumanTime(String isoTime) {
        try {
            // Input format (ISO 8601)
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));  // If time is in UTC
            
            // Parse the input time
            Date date = isoFormat.parse(isoTime);
            
            // Output format (human readable)
            SimpleDateFormat humanFormat = new SimpleDateFormat("EEE, MMM dd yyyy 'at' hh:mm a");
            humanFormat.setTimeZone(TimeZone.getDefault());  // Convert to local timezone
            
            return humanFormat.format(date);
        } catch (Exception e) {
            return isoTime; // Return original if parsing fails
        }
    }
    static  String carriername(String code){
        switch (code) {
            case "AF": return "Air France";
            case "DL": return "Delta Airlines";
            case "AA": return "American Airlines";
            case "VS": return "Virgin Atlantic";
            case "IB": return "Iberia";
            case "B6": return "JetBlue Airways";
            case "N0": return "Norse Atlantic Airways";
            default:return code;
        }
    }
    
    private JPanel createBookingCard(String location, String price, String email) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(700, 160));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel locationLabel = new JLabel("Route: " + location);
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        locationLabel.setForeground(primaryColor);
        JLabel flightInfo = new JLabel("Flight: " + carriername(carrier) + " " + flightNumber);
        flightInfo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        flightInfo.setForeground(Color.DARK_GRAY);
        JLabel priceLabel = new JLabel("Price: €" + price);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        priceLabel.setForeground(Color.DARK_GRAY);

        JLabel emailLabel = new JLabel("Email: " + email);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(Color.GRAY);

        JLabel timelabel = new JLabel(" Departure time: " +convertToHumanTime(deptime) +" Arrival time: " +convertToHumanTime(arrtime) );
        timelabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timelabel.setForeground(Color.GRAY);

     

        card.add(locationLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(flightInfo);
        card.add(Box.createVerticalStrut(5));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(timelabel);
   

        return card;
    }
    

}
