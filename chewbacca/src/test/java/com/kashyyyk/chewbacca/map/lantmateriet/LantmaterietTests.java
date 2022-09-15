package com.kashyyyk.chewbacca.map.lantmateriet;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kashyyyk.chewbacca.map.Coordinate;

public class LantmaterietTests {

    @Test
    public void testGridToGeodetic() {
        var north = 6397317.25;
        var east = 319632.5;

        var result = Lantmateriet.gridToGeodetic(north, east);

        assertEquals(57.681861, result.latitude, 0.0001);
        assertEquals(11.974749, result.longitude, 0.0001);

        var reversed = Lantmateriet.geodeticToGrid(result.latitude, result.longitude);

        assertEquals(north, reversed.latitude, 0.0001);
        assertEquals(east, reversed.longitude, 0.0001);
    }

    @Test
    public void testElevation() {
        // Start a timer
        var start = System.currentTimeMillis();

        var result = Lantmateriet.getElevation(new Coordinate(57.681861, 11.974749));

        assertEquals(82.28, result, 0.0001);

        // Stop the timer
        var stop = System.currentTimeMillis();

        // Run it again and make sure it's faster due to caching
        var result2 = Lantmateriet.getElevation(new Coordinate(57.681861, 11.974749));

        assertEquals(82.28, result2, 0.0001);

        // Stop the timer
        var stop2 = System.currentTimeMillis();

        // Make sure the second run was faster
        assertTrue(stop2 - stop < stop - start);
    }

    @Test
    public void testRenderImage() throws IOException
    {
        // This just has to not throw an exception
        var result = Lantmateriet.renderImage(
            512, 512, 6400119 - 500, 317623 - 500, 6400119 + 500, 317623 + 500
        );
    }
}
