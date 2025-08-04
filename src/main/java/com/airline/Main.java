package com.airline;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.airline.database.Home;

public class Main extends Token{
   
   
    public static void main(String[] args) throws Exception {
       System.out.println( getAccessToken());
       System.out.println( searchFlights( getAccessToken()));
       String data = searchFlights( getAccessToken());
   
    //    String carrierCode = "AF";
    //    String flightNumber = "6"; // Active flight number
    //    String date = "2025-08-10"; // Current date
    //   System.out.println( delay(carrierCode, flightNumber, date,getAccessToken()));
    // Home myhome=new Home();
    new SearchFrame();
    }
       
        // myhome.displayFlightResultsInPanel(data);
    
    

    
    
}