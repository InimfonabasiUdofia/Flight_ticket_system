package com.airline;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.json.JSONObject;

public class Token {
    private static  String CLIENT_ID = "lAMXj3Gh3YDnAn6P60HNlp7OSpPyfxke";
    private static  String CLIENT_SECRET = "NTlJlfLVfVQ2iaoA";
    private static  String AUTH_URL = "https://test.api.amadeus.com/v1/security/oauth2/token";

    // get access_token method
    public static String getAccessToken() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        String formData = "grant_type=client_credentials" +
                          "&client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8) +
                          "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, StandardCharsets.UTF_8);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AUTH_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Parse JSON to extract "access_token"
       
        JSONObject json = new JSONObject(response.body());
        return json.getString("access_token");
    }
    public static String getIataCode(String cityName) {
            switch(cityName) {
                // Europe
                case "Paris(Charles de Gaulle)": return "CDG";
                case "London (Heathrow)": return "LHR";
                case "Frankfurt": return "FRA";
                case "Amsterdam": return "AMS";
                case "Madrid": return "MAD";
                case "Barcelona": return "BCN";
                case "Rome": return "FCO";
                case "Brussels": return "BRU";
                case "Zurich": return "ZRH";
                case "Vienna": return "VIE";
        
                // North America
                case "New York": return "JFK";
                case "Los Angeles": return "LAX";
                case "Chicago": return "ORD";
                case "Toronto": return "YYZ";
                case "Montreal": return "YUL";
                case "Miami": return "MIA";
                case "San Francisco": return "SFO";
                case "Dallas": return "DFW";
        
                // Other regions
                case "Dubai": return "DXB";
                case "Hong Kong": return "HKG";
                case "Singapore": return "SIN";
                case "Tokyo": return "NRT";
                case "Sydney": return "SYD";
                case "Paris (Orly)": return "ORY";
                case "Dublin": return "DUB";
                case "Warsaw (Chopin)": return "WAW";
                case "Belgrade (Nikola Tesla)": return "BEG";
                case "Casablanca (Mohammed V)": return "CMN";
                case "Istanbul": return "IST";
                case "Turkish Airlines Flight": return "TK";
                case "Reykjavik (Keflavik)": return "KEF";
                case "TAP Portugal Flight": return "TP";
                case "London (Gatwick)": return "LGW";
                case "Nice (Côte d'Azur)": return "NCE";
        
                default: return cityName; // Return input if code not found
            }
        }
    
        // use access_token to get flight details
        public static String searchFlights(  String accessToken, JComboBox<String> destinationCombo, JTextField dateField) throws Exception {
            HttpClient client = HttpClient.newHttpClient();
            String destination = (String) destinationCombo.getSelectedItem(); // get selected destination
            String date = dateField.getText().trim();
            System.out.println(destination);
            System.out.println(date);
            String url = "https://test.api.amadeus.com/v2/shopping/flight-offers" +
            "?originLocationCode=PAR" +
            "&destinationLocationCode=" + getIataCode(destination) +
        "&departureDate=" + date +
        "&adults=1" +
        "&nonStop=true";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    // public static String delay(String carrierCode,String flightNumber,String date,String accessToken){
    //     String url = String.format(
    //         "https://test.api.amadeus.com/v2/schedule/flights?carrierCode=%s&flightNumber=%s&scheduledDepartureDate=%s",
    //         carrierCode, flightNumber, date
    //     );

    //     HttpRequest request = HttpRequest.newBuilder()
    //         .uri(URI.create(url))
    //         .header("Authorization", "Bearer " + accessToken)
    //         .GET()
    //         .build();

    //     HttpResponse<String> response = null;
    //     try {
    //         response = HttpClient.newHttpClient()
    //             .send(request, HttpResponse.BodyHandlers.ofString());
    //     } catch (IOException | InterruptedException e) {
          
    //         System.out.println(e);
    //     }

    //     return response.body();
    // }
  
    

}
