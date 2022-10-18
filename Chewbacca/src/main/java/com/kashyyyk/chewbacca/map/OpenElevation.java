package com.kashyyyk.chewbacca.map;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    /**
     * Get elevation for a set of given coordinates
     * 
     * @param latlon                    Array of latitude and longitude pairs (lat, lon, lat, lon, ...)
     * @return                          Array of elevations
     * @throws IOException              If the GET request fails
     * @throws IllegalArgumentException If the length of latlon is not even
     */
    public static double[] getElevation(double... latlon) throws IOException {
        if (latlon.length % 2 != 0) {
            throw new IllegalArgumentException("latlon must have an even number of elements");
        }

        String url = ENDPOINT + "?locations=" + latlon[0] + "," + latlon[1];
        for (int i = 2; i < latlon.length; i += 2) {
            url += "|" + latlon[i] + "," + latlon[i + 1];
        }
        
        // Download data from url
        InputStream stream = new java.net.URL(url).openStream();
        BufferedInputStream reader = new BufferedInputStream(stream);
        
        // Parse data
        JsonMapper mapper = new JsonMapper();
        // The result is an array of Elevation objects
        Result result = mapper.readValue(reader, Result.class);

        double[] elevations = new double[result.results.length];
        for (int i = 0; i < result.results.length; i++) {
            elevations[i] = result.results[i].elevation;
        }

        return elevations;
    }

    /**
     * Get elevation for a set of given coordinates
     * 
     * @param latlon                    Array of latitude and longitude pairs (lat, lon, lat, lon, ...)
     * @return                          Array of elevations
     * @throws IOException              If the GET request fails
     * @throws IllegalArgumentException If the length of latlon is not even
     */
    public static double[] getElevation(List<Double> latlon) throws IOException {
        if (latlon.size() % 2 != 0) {
            throw new IllegalArgumentException("latlon must have an even number of elements");
        }

        String url = ENDPOINT + "?locations=" + latlon.get(0) + "," + latlon.get(1);
        for (int i = 2; i < latlon.size(); i += 2) {
            url += "|" + latlon.get(i) + "," + latlon.get(i + 1);
        }

        // Download data from url
        InputStream stream = new java.net.URL(url).openStream();
        BufferedInputStream reader = new BufferedInputStream(stream);
        
        // Parse data
        JsonMapper mapper = new JsonMapper();
        // The result is an array of Elevation objects
        Result result = mapper.readValue(reader, Result.class);

        double[] elevations = new double[result.results.length];
        for (int i = 0; i < result.results.length; i++) {
            elevations[i] = result.results[i].elevation;
        }

        return elevations;
    }
}
