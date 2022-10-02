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
}
