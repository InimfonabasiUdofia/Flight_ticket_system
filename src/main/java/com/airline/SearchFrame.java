package com.airline;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.json.*;

import com.airline.database.Home;
import com.airline.database.LoginGUI;
import com.airline.database.MybookingFrame;
import com.airline.database.PaymentUI;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;


public class SearchFrame extends Token {
    JFrame frame = new JFrame("PILOT AIR- Book Flight");
    JPanel mainPanel = new JPanel();
    private Color primaryColor = new Color(0x074C83);
    private Color secondaryColor = new Color(0x0A6FC4);
    private Color accentColor = new Color(0xFF6B00);
 
    private JComboBox<String> destinationCombo;
    private JTextField dateField;
    static JButton searchButton;
    
    public SearchFrame() {
      // Keep all your existing frame setup code exactly as is
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setSize(1000, 700);
frame.setMinimumSize(new Dimension(900, 600));
frame.setLayout(new BorderLayout());
frame.getContentPane().setBackground(Color.WHITE);
frame.setVisible(true);

// Main panel setup (unchanged)
mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
mainPanel.setBackground(Color.WHITE);

// Create a wrapper panel to center the cards
JPanel centerWrapper = new JPanel(new GridBagLayout());
centerWrapper.setBackground(Color.WHITE);
centerWrapper.add(mainPanel);

JScrollPane scrollPane = new JScrollPane(centerWrapper);  // Changed to wrap center panel
scrollPane.setBorder(BorderFactory.createEmptyBorder());
scrollPane.getVerticalScrollBar().setUnitIncrement(16);
frame.add(scrollPane, BorderLayout.CENTER);

// Keep your existing header code exactly as is
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

JComboBox<String> destinationCombo = new JComboBox<>(new String[] {
    "JFK", "LAX", "PAR", "LHR", "DXB", 
    "CDG", "FRA", "AMS", "MAD", "BCN", 
    "FCO", "BRU", "ZRH", "VIE", "ORD", 
    "YYZ", "YUL", "MIA", "SFO", "DFW", 
    "HKG", "SIN", "NRT", "SYD", "ORY", 
    "DUB", "WAW", "BEG", "CMN",
});
for (int i = 0; i < destinationCombo.getItemCount(); i++) {
    String code = destinationCombo.getItemAt(i);
    destinationCombo.insertItemAt(getCityName(code), i);
    destinationCombo.removeItemAt(i + 1); // Remove the original code
}


dateField = new JTextField(1);
dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
searchButton = new JButton("Search Flights");

// Simple date picker button
JButton datePickerBtn = new JButton("📅");
datePickerBtn.addActionListener(e -> {
    String selectedDate = (String) JOptionPane.showInputDialog(datePickerBtn, this, "Enter date (YYYY-MM-DD):", 0, null, null, dateField.getText());
    if (selectedDate != null) {
        dateField.setText(selectedDate);
    }
});
JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); // spacing between items
centerPanel.add(new JLabel("Destination:"));
centerPanel.add(destinationCombo);
centerPanel.add(new JLabel("Date:"));
centerPanel.add(datePickerBtn);
centerPanel.add(searchButton);

searchButton.addActionListener(e->{
    try {
        String flightData = searchFlights(getAccessToken(),destinationCombo,dateField);
        displayFlightSegments(flightData);
    } catch (Exception error) {
        System.out.println(error);
        JOptionPane.showMessageDialog(frame, "Error loading flights: " + error.getMessage(), 
                                   "Error", JOptionPane.ERROR_MESSAGE);
    }
});

 
       

headerPanel.add(titleLabel, BorderLayout.WEST);
headerPanel.add(userPanel, BorderLayout.EAST);
headerPanel.add(centerPanel, BorderLayout.SOUTH);


frame.add(headerPanel, BorderLayout.NORTH);
        createNavPanel();
        
       
    }

    public void displayFlightSegments(String json) {
        mainPanel.removeAll(); 
        
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
    
    // Extract price (assuming structure: "price": {"total": "200.00", "currency": "EUR"})
    JSONObject priceObj = offer.getJSONObject("price");
    String totalPrice = priceObj.getString("total");
   
    
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
            
            // Pass price to createSegmentPanel (modify this method to display price)
            JPanel flightPanel = createSegmentPanel(segment, totalPrice);
            
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
    
    private JPanel createSegmentPanel(JSONObject segment ,String price) throws JSONException {
        JSONObject departure = segment.getJSONObject("departure");
        JSONObject arrival = segment.getJSONObject("arrival");
        
        String from = departure.getString("iataCode");
        String to = arrival.getString("iataCode");
        String depTime = departure.getString("at");
        String arrTime = arrival.getString("at");
        String carrier = segment.getString("carrierCode");
        String flightNumber = segment.getString("number");
    
        Color cardBackground = new Color(0xF9F9F9);
        Color borderColor = new Color(0xCCCCCC);
        Color primaryColor = new Color(0x074C83);
    
        // === Outer wrapper for margin spacing ===
        JPanel outerWrapper = new JPanel();
        outerWrapper.setLayout(new BorderLayout());
        outerWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // outer margin
        outerWrapper.setOpaque(false);  // transparent so background shows
    
        // === Inner flight card ===
        JPanel flightPanel = new JPanel();
        flightPanel.setLayout(new BoxLayout(flightPanel, BoxLayout.Y_AXIS));
        flightPanel.setBackground(cardBackground);
        flightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)  // inner padding
        ));
        flightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        flightPanel.setMaximumSize(new Dimension(450, 250));

        
       
        // === Labels ===
        JLabel flightInfo = new JLabel("Flight: " + carriername(carrier) + " " + flightNumber);
        flightInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel route = new JLabel("From: " + getCityName(from) + " → To: " + getCityName(to));
        route.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel departureTime = new JLabel("Departure: " +  convertToHumanTime(depTime));
        departureTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel arrivalTime = new JLabel("Arrival: " + convertToHumanTime(arrTime) );
        arrivalTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel money = new JLabel("€" + price);
        money.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
        // === Booking button ===
        JButton bookButton = new JButton("Book Now");
        bookButton.setBackground(primaryColor);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookButton.setFocusPainted(false);
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookButton.setMaximumSize(new Dimension(140, 35));

        bookButton.addActionListener(e->{
            frame.dispose();
            new PaymentUI(from, to, carrier,flightNumber ,price,depTime,arrTime);
        });
    
        // === Add components to flight panel ===
        flightPanel.add(flightInfo);
        flightPanel.add(Box.createVerticalStrut(8));
        flightPanel.add(route);
        flightPanel.add(Box.createVerticalStrut(4));
        flightPanel.add(departureTime);
        flightPanel.add(arrivalTime);
        flightPanel.add(Box.createVerticalStrut(12));
        flightPanel.add(money);
        flightPanel.add(Box.createVerticalStrut(12));
        flightPanel.add(bookButton);
        
    
    
        // Add card to outer wrapper
        outerWrapper.add(flightPanel, BorderLayout.CENTER);
        return outerWrapper;
    }
     private void createNavPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(6, 1, 0, 10));
        navPanel.setPreferredSize(new Dimension(180, 0));
        navPanel.setBackground(primaryColor);
        navPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Buttons with icons
        JButton homeBtn = createNavButton("Home");
        JButton searchBtn = createNavButton("Search Flights");
        JButton bookingsBtn = createNavButton("My Bookings");
        JButton aboutBtn = createNavButton("About");
        JButton contactBtn = createNavButton("Contact");
        JButton logout = createNavButton("Logout");

        homeBtn.addActionListener(e -> {
            frame.dispose();
            Home contact = new Home();
        });
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
        logout.addActionListener(e -> {
            frame.dispose();
            LoginGUI contact = new LoginGUI();
        });
        
        navPanel.add(homeBtn);
        navPanel.add(searchBtn);
        navPanel.add(bookingsBtn);
        navPanel.add(aboutBtn);
        navPanel.add(contactBtn);
        navPanel.add(logout);

        frame.add(navPanel, BorderLayout.WEST);
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


}