package com.kashyyyk.chewbacca.map.lantmateriet;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import com.kashyyyk.chewbacca.map.Coordinate;
import com.kashyyyk.chewbacca.map.WaypointType;

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
            512, 512, 6396234 - 100, 318824 - 100, 6396234 + 100, 318824 + 100
        );
    }

    @Test
    public void testProcessImage() throws IOException
    {
        // This just has to not throw an exception
        var result = LantmaterietImageProcessor.processPoint(6396234, 318824);

        // For each pixel
        var path = result.waypoints;

        // Draw the path on the image
        for (var waypint : path) {
            if (!waypint.getType().equals(WaypointType.Path)) continue;
            // Check if any of the other path elements are closer than 10 pixels
            for (var other : path) {
                if (waypint != other) {
                    var coordinate = waypint.getCoordinate();
                    var otherCoordinate = other.getCoordinate();
                    
                    var distance = Math.sqrt(
                        Math.pow(coordinate.latitude - otherCoordinate.latitude, 2) +
                        Math.pow(coordinate.longitude - otherCoordinate.longitude, 2)
                    );
                    
                    if (distance > 35) {
                        continue;
                    }

                    coordinate = LantmaterietImageProcessor.getPixelPositionFromCoordinate(result.minNorth, result.minEast, coordinate);
                    otherCoordinate = LantmaterietImageProcessor.getPixelPositionFromCoordinate(result.minNorth, result.minEast, otherCoordinate);

                    // Draw a line between the two points
                    var x1 = (int)coordinate.longitude;
                    var y1 = (int)coordinate.latitude;
                    var x2 = (int)otherCoordinate.longitude;
                    var y2 = (int)otherCoordinate.latitude;

                    var dx = Math.abs(x2 - x1);
                    var dy = Math.abs(y2 - y1);
                    var sx = x1 < x2 ? 1 : -1;
                    var sy = y1 < y2 ? 1 : -1;
                    var err = dx - dy;

                    while (true) {
                        result.image.setRGB(x1, y1, 0xFF0000);

                        if (x1 == x2 && y1 == y2) {
                            break;
                        }

                        var e2 = 2 * err;

                        if (e2 > -dy) {
                            err = err - dy;
                            x1 = x1 + sx;
                        }

                        if (e2 < dx) {
                            err = err + dx;
                            y1 = y1 + sy;
                        }
                    }
                }
            }
        }

        // Save the image to disk
        ImageIO.write(result.image, "png", new java.io.File("test.png"));
    }
}
