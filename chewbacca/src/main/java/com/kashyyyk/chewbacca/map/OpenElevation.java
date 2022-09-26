package com.kashyyyk.chewbacca.map;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Open Elevation API
 */
public class OpenElevation {
    private OpenElevation() {};

    private static final String ENDPOINT = "https://api.open-elevation.com/api/v1/lookup";

    private static class Elevation {
        public double latitude;
        public double longitude;
        public double elevation;
    }

    private static class Result {
        public Elevation[] results;
    }

    /**
     * Get elevation for a given coordinate
     * 
     * @param lat               Latitude
     * @param lon               Longitude
     * @return                  Elevation
     * @throws IOException      If the GET request fails
     */
    public static double getElevation(double lat, double lon) throws IOException {
        String url = ENDPOINT + "?locations=" + lat + "," + lon;
        
        // Download data from url
        InputStream stream = new java.net.URL(url).openStream();
        BufferedInputStream reader = new BufferedInputStream(stream);
        
        // Parse data
        JsonMapper mapper = new JsonMapper();
        // The result is an array of Elevation objects
        Result result = mapper.readValue(reader, Result.class);

        return result.results[0].elevation;
    }
}
