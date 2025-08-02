package com.airline;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

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

    // use access_token to get flight details
    public static String searchFlights(  String accessToken) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://test.api.amadeus.com/v2/shopping/flight-offers?" +
        "originLocationCode=PAR&" +
        "destinationLocationCode=JFK&" +
        "departureDate=2025-08-20&" +
        "adults=1"; 

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String delay(String carrierCode,String flightNumber,String date,String accessToken){
        String url = String.format(
            "https://test.api.amadeus.com/v2/schedule/flights?carrierCode=%s&flightNumber=%s&scheduledDepartureDate=%s",
            carrierCode, flightNumber, date
        );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
          
            System.out.println(e);
        }

        return response.body();
    }
  
    

}
