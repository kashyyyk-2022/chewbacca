package com.kashyyyk.chewbacca.map;

import java.io.BufferedInputStream;
import java.io.InputStream;

import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Open Source Routing Machine API
 */
public final class OpenSourceRoutingMachine {
    private OpenSourceRoutingMachine() {};
    
    private static final String ENDPOINT = "https://routing.openstreetmap.de/routed-foot/route/v1/driving/%s,%s;%s,%s?overview=false&geometries=polyline&steps=true";
   
    /**
     * Get route between two coordinates
     * 
     * @param startLat      Start latitude
     * @param startLon      Start longitude
     * @param endLat        End latitude
     * @param endLon        End longitude
     * @return              Route
     * @throws Exception    If the GET request fails
     */
    public static OsrmResult getRoute(double startLat, double startLon, double endLat, double endLon) throws Exception {
        String url = String.format(ENDPOINT, startLon, startLat, endLon, endLat);

        // Download data from url
        InputStream stream = new java.net.URL(url).openStream();
        BufferedInputStream reader = new BufferedInputStream(stream);
        
        // Parse data
        JsonMapper mapper = new JsonMapper();
        OsrmResult route = mapper.readValue(reader, OsrmResult.class);

        return route;
    }
}
