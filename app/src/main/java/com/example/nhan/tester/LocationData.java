package com.example.nhan.tester;

/**
 * Created by nhan on 3/14/15.
 */
public class LocationData {
    public GeometryInfo geometry;
    public String formatted_address;

    public String displayResult(){
        String result = "";
        result += "Formatted Address: " + formatted_address;
        result += "\n";
        result += "Lat Long: " + geometry.location.lat + " " + geometry.location.lng;

        return result;
    }
}
