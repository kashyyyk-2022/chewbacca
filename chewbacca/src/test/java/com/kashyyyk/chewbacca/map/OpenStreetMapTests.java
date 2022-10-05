package com.kashyyyk.chewbacca.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OpenStreetMapTests {
    @Test
    public void testDownloadData() throws Exception {
        Osm osm = OpenStreetMap.downloadData(57.67074, 11.96052, 57.67272, 11.96340);
    }

    @Test
    public void testDownloadElevation() throws Exception {
        double elevation = OpenElevation.getElevation(57.67074, 11.96052);

        assertEquals(elevation, 98.0);
    }

    @Test
    public void testDownloadRoute() throws Exception {
        OsrmResult route = OpenSourceRoutingMachine.getRoute(57.67074, 11.96052, 57.67272, 11.96340);
    }

    @Test
    public void testWaypointGenerate() throws Exception{
        double endLat;
        double endLng;

        Osm osm = OpenStreetMap.downloadData(57.692622286683225,11.966922283172607, 57.69652702997704,11.972200870513916);
        System.out.println("Lat: " + osm.node[0].lat +"Lng: " + osm.node[0].lon);
        endLat = osm.node[0].lat;
        endLng = osm.node[0].lon;

    }

    @Test
    public void testNodeTags() throws Exception{
        WaypointGenerator tmp = new WaypointGenerator();
    }

}
