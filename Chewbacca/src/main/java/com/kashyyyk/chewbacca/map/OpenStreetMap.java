package com.kashyyyk.chewbacca.map;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Open Street Map API
 */
public final class OpenStreetMap {
    private OpenStreetMap() {};

    private static final String ENDPOINT = "https://api.openstreetmap.org/api/0.6/";

    /**
     * Download data from Open Street Map
     * 
     * @param bottomLeftLat             Bottom left latitude
     * @param bottomLeftLon             Bottom left longitude
     * @param topRightLat               Top right latitude
     * @param topRightLon               Top right longitude
     * @return                          OSM data
     * @throws MalformedURLException    If the URL is malformed
     * @throws IOException              If the GET request fails
     */
    public static Osm downloadData(double bottomLeftLat, double bottomLeftLon, double topRightLat, double topRightLon) throws MalformedURLException, IOException {
        String url = ENDPOINT + "map?bbox=" + bottomLeftLon + "," + bottomLeftLat + "," + topRightLon + "," + topRightLat;
        
        // Download data from url
        InputStream stream = new java.net.URL(url).openStream();
        BufferedInputStream reader = new BufferedInputStream(stream);
        
        // Parse data
        XmlMapper mapper = new XmlMapper();
        Osm osm = mapper.readValue(reader, Osm.class);

        return osm;
    }
}
