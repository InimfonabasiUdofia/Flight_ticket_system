package com.airline;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.json.*;
import java.awt.*;

public class SearchFrame extends Token {
    JFrame frame = new JFrame("");
    JPanel mainPanel = new JPanel();
    private Color primaryColor = new Color(0x074C83);
    private Color secondaryColor = new Color(0x0A6FC4);
    private Color accentColor = new Color(0xFF6B00);
    
    SearchFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);
        
        // Set up scrollable main panel with grid layout for rows
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        frame.setVisible(true);
        
        try {
            String flightData = searchFlights(getAccessToken());
            displayFlightSegments(flightData);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(frame, "Error loading flights: " + e.getMessage(), 
                                       "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        createNavPanel();
    }

    public void displayFlightSegments(String json) {
        mainPanel.removeAll(); // Clear previous results
        
        try {
            JSONObject response = new JSONObject(json);
            JSONArray data = response.getJSONArray("data");
            
            if (data.length() == 0) {
                mainPanel.add(new JLabel("No flights found", SwingConstants.CENTER));
                return;
            }
            
            // Create a panel to hold rows of flight panels
            JPanel rowsPanel = new JPanel();
            rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
            rowsPanel.setBackground(Color.WHITE);
            
            // Temporary panel to hold two flights per row
            JPanel currentRowPanel = null;
            
            int segmentCount = 0;
            
            for (int i = 0; i < data.length(); i++) {
                JSONObject offer = data.getJSONObject(i);
                JSONArray itineraries = offer.getJSONArray("itineraries");
                
                for (int j = 0; j < itineraries.length(); j++) {
                    JSONObject itinerary = itineraries.getJSONObject(j);
                    JSONArray segments = itinerary.getJSONArray("segments");
                    
                    for (int k = 0; k < segments.length(); k++) {
                        JSONObject segment = segments.getJSONObject(k);
                        
                        // Create new row panel for every two segments
                        if (segmentCount % 2 == 0) {
                            currentRowPanel = new JPanel();
                            currentRowPanel.setLayout(new BoxLayout(currentRowPanel, BoxLayout.X_AXIS));
                            currentRowPanel.setBackground(Color.WHITE);
                            currentRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                            rowsPanel.add(currentRowPanel);
                            
                            // Add vertical spacing between rows
                            if (segmentCount > 0) {
                                rowsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            }
                        }
                        
                        // Create segment panel (your original design)
                        JPanel flightPanel = createSegmentPanel(segment);
                        
                        // Add to current row
                        if (currentRowPanel != null) {
                            currentRowPanel.add(flightPanel);
                            
                            // Add horizontal spacing between flight panels
                            if (segmentCount % 2 == 0) {
                                currentRowPanel.add(Box.createRigidArea(new Dimension(10, 0)));
                            }
                        }
                        
                        segmentCount++;
                    }
                }
            }
            
            mainPanel.add(rowsPanel);
            frame.revalidate();
            frame.repaint();
            
        } catch (JSONException e) {
            JOptionPane.showMessageDialog(frame, "Error parsing flight data", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
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
    
    private JPanel createSegmentPanel(JSONObject segment) throws JSONException {
        JSONObject departure = segment.getJSONObject("departure");
        JSONObject arrival = segment.getJSONObject("arrival");
        
        String from = departure.getString("iataCode");
        String to = arrival.getString("iataCode");
        String depTime = departure.getString("at");
        String arrTime = arrival.getString("at");
        String carrier = segment.getString("carrierCode");
        String flightNumber = segment.getString("number");
        
        // Your original panel design
        JPanel flightPanel = new JPanel();
        flightPanel.setLayout(new BoxLayout(flightPanel, BoxLayout.Y_AXIS));
        flightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        flightPanel.setBackground(Color.WHITE);
        flightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        flightPanel.setMaximumSize(new Dimension(450, 250)); // Adjusted width for side-by-side display
        
        flightPanel.add(new JLabel("✈️ Flight: " + carrier + " " + flightNumber));
        flightPanel.add(new JLabel("From: "  + getCityName(from)+ " → To: "  +  getCityName(to)));
        flightPanel.add(new JLabel("🕒 Departure: " + depTime));
        flightPanel.add(new JLabel("🕒 Arrival: " + arrTime));
        
        return flightPanel;
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

}