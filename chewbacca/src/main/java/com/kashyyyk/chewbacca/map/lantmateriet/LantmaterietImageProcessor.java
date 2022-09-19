package com.kashyyyk.chewbacca.map.lantmateriet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.Color;

import com.kashyyyk.chewbacca.map.Coordinate;
import com.kashyyyk.chewbacca.map.Waypoint;
import com.kashyyyk.chewbacca.map.WaypointType;

/**
 * Lantmatertiet image processor.
 * 
 * Uses the Lantmäteriet API to process an image and extract waypoints.
 */
public final class LantmaterietImageProcessor {
    private LantmaterietImageProcessor() {}

    /**
     * The tile size.
     */
    public static final int TILE_SIZE = 256;

    /**
     * The image resolution.
     */
    public static final int TILE_RESOLUTION = 1000;

    /**
     * Process an image.
     * 
     * @param minNorth      The minimum north coordinate.
     * @param minEast       The minimum east coordinate.
     * @param image         The image to process.
     * @return              The extracted waypoints.
     */
    public static Waypoint[] processImage(int minNorth, int minEast, BufferedImage image) {
        var points = new LinkedList<Waypoint>();
        var rasio = (double) TILE_RESOLUTION / (TILE_SIZE * 2);

        assert image.getWidth() == TILE_RESOLUTION;

        for (int e = 0; e < image.getWidth(); e++) {
            for (int n = 0; n < image.getHeight(); n++) {
                var color = image.getRGB(e, n);
                var r = (color >> 16) & 0xFF;
                var g = (color >> 8) & 0xFF;
                var b = (color >> 0) & 0xFF;
                final Color c = new Color(r, g, b);

                var north = minNorth + (int) (n * rasio);
                var east = minEast + (int) (e * rasio);
                
                if (WaypointType.Path.ContainsColor(c)) {
                    points.add(new Waypoint(new Coordinate(north, east), 0.0, WaypointType.Path, 1));
                } else if (WaypointType.Water.ContainsColor(c)) {
                    points.add(new Waypoint(new Coordinate(north, east), 0.0, WaypointType.Water, 1));
                }
            }
        }

        // Combine all points of the same type within a 10m radius
        var combinedPoints = new LinkedList<Waypoint>();

        for (var point : points) {
            var combined = false;

            for (var combinedPoint : combinedPoints) {
                if (combinedPoint.getType() == point.getType() && combinedPoint.getCoordinate().distanceTo(point.getCoordinate()) < 10) {
                    combinedPoint.setCoordinate(combinedPoint.getCoordinate().average(point.getCoordinate()));
                    combinedPoint.setSize(combinedPoint.getSize() + 1);
                    combined = true;
                    break;
                }
            }

            if (!combined) {
                combinedPoints.add(point);
            }
        }

        return combinedPoints.toArray(new Waypoint[0]);
    }

    /**
     * Calculate the pixel position from a coordinate.
     * 
     * @param minNorth      The minimum north coordinate.
     * @param minEast       The minimum east coordinate.
     * @param coordinate    The coordinate.
     * @return              The pixel position.
     */
    public static Coordinate getPixelPositionFromCoordinate(int minNorth, int minEast, Coordinate coordinate) {
        var rasio = (double) TILE_RESOLUTION / (TILE_SIZE * 2);

        var north = coordinate.getLatitude() - minNorth;
        var east = coordinate.getLongitude() - minEast;

        return new Coordinate(north / rasio, east / rasio);
    }

    /**
     * Process a point on the Lantmäteriet map. Fetches the image and extracts the waypoints.
     * 
     * @param north         The north coordinate. (center)
     * @param east          The east coordinate. (center)
     * @return              The extracted waypoints.
     * @throws IOException  If an error occurs while fetching the image.
     */
    public static ImageProcessResult processPoint(int north, int east) throws IOException {
        var image = Lantmateriet.renderImage(
            TILE_RESOLUTION, TILE_RESOLUTION,
            north - TILE_SIZE, east - TILE_SIZE,
            north + TILE_SIZE, east + TILE_SIZE
        );

        return new ImageProcessResult(image, north - TILE_SIZE, east - TILE_SIZE, processImage(north - TILE_SIZE, east - TILE_SIZE, image));
    }
}
