package com.kashyyyk.chewbacca.map.lantmateriet;

import java.util.HashMap;

import javax.imageio.ImageIO;

import org.springframework.web.client.RestTemplate;

import com.kashyyyk.chewbacca.map.Coordinate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * This class is used to get data from Lantmäteriet.
 * 
 * The data is retrieved from the Lantmäteriet API for the "Min Karta" service.
 * It is free for non-commercial use, which this project is.
 * 
 * @author Wincent Stålbert Holm
 */
public final class Lantmateriet {
    private Lantmateriet() {
    }

    private static double atanh(double a) {
        final double mult;
        // check the sign bit of the raw representation to handle -0
        if (Double.doubleToRawLongBits(a) < 0) {
            a = Math.abs(a);
            mult = -0.5d;
        } else {
            mult = 0.5d;
        }
        return mult * Math.log((1.0d + a) / (1.0d - a));
    }

    /**
     * Convert a coordinate in the Lantmäteriet grid system to a coordinate in
     * latitude and longitude.
     * 
     * Code converted from the JavaScript found at https://latlong.mellifica.se/
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The coordinate in latitude and longitude.
     */
    public static Coordinate gridToGeodetic(double x, double y) {
        var central_meridian = 15.00;
        var scale = 0.9996;
        var false_northing = 0.0;
        var false_easting = 500000.0;

        var axis = 6378137.0; // GRS 80.
        var flattening = 1.0 / 298.257222101; // GRS 80.

        // Prepare ellipsoid-based stuff.
        var e2 = flattening * (2.0 - flattening);
        var n = flattening / (2.0 - flattening);
        var a_roof = axis / (1.0 + n) * (1.0 + n * n / 4.0 + n * n * n * n / 64.0);
        var delta1 = n / 2.0 - 2.0 * n * n / 3.0 + 37.0 * n * n * n / 96.0 - n * n * n * n / 360.0;
        var delta2 = n * n / 48.0 + n * n * n / 15.0 - 437.0 * n * n * n * n / 1440.0;
        var delta3 = 17.0 * n * n * n / 480.0 - 37 * n * n * n * n / 840.0;
        var delta4 = 4397.0 * n * n * n * n / 161280.0;

        var Astar = e2 + e2 * e2 + e2 * e2 * e2 + e2 * e2 * e2 * e2;
        var Bstar = -(7.0 * e2 * e2 + 17.0 * e2 * e2 * e2 + 30.0 * e2 * e2 * e2 * e2) / 6.0;
        var Cstar = (224.0 * e2 * e2 * e2 + 889.0 * e2 * e2 * e2 * e2) / 120.0;
        var Dstar = -(4279.0 * e2 * e2 * e2 * e2) / 1260.0;

        // Convert.
        var deg_to_rad = Math.PI / 180;
        var lambda_zero = central_meridian * deg_to_rad;
        var xi = (x - false_northing) / (scale * a_roof);
        var eta = (y - false_easting) / (scale * a_roof);
        var xi_prim = xi -
                delta1 * Math.sin(2.0 * xi) * Math.cosh(2.0 * eta) -
                delta2 * Math.sin(4.0 * xi) * Math.cosh(4.0 * eta) -
                delta3 * Math.sin(6.0 * xi) * Math.cosh(6.0 * eta) -
                delta4 * Math.sin(8.0 * xi) * Math.cosh(8.0 * eta);
        var eta_prim = eta -
                delta1 * Math.cos(2.0 * xi) * Math.sinh(2.0 * eta) -
                delta2 * Math.cos(4.0 * xi) * Math.sinh(4.0 * eta) -
                delta3 * Math.cos(6.0 * xi) * Math.sinh(6.0 * eta) -
                delta4 * Math.cos(8.0 * xi) * Math.sinh(8.0 * eta);
        var phi_star = Math.asin(Math.sin(xi_prim) / Math.cosh(eta_prim));
        var delta_lambda = Math.atan(Math.sinh(eta_prim) / Math.cos(xi_prim));
        var lon_radian = lambda_zero + delta_lambda;
        var lat_radian = phi_star + Math.sin(phi_star) * Math.cos(phi_star) *
                (Astar +
                        Bstar * Math.pow(Math.sin(phi_star), 2) +
                        Cstar * Math.pow(Math.sin(phi_star), 4) +
                        Dstar * Math.pow(Math.sin(phi_star), 6));

        return new Coordinate(lat_radian * 180.0 / Math.PI, lon_radian * 180.0 / Math.PI);
    }

    /**
     * Convert a coordinate in latitude and longitude to a coordinate in the
     * Lantmäteriet grid system.
     * 
     * Code converted from the JavaScript found at https://latlong.mellifica.se/
     * 
     * @param latitude  The latitude.
     * @param longitude The longitude.
     * @return The coordinate in the Lantmäteriet grid system.
     */
    public static Coordinate geodeticToGrid(double latitude, double longitude) {
        var central_meridian = 15.00;
        var scale = 0.9996;
        var false_northing = 0.0;
        var false_easting = 500000.0;

        var axis = 6378137.0; // GRS 80.
        var flattening = 1.0 / 298.257222101; // GRS 80.

        // Prepare ellipsoid-based stuff.
        var e2 = flattening * (2.0 - flattening);
        var n = flattening / (2.0 - flattening);
        var a_roof = axis / (1.0 + n) * (1.0 + n * n / 4.0 + n * n * n * n / 64.0);
        var A = e2;
        var B = (5.0 * e2 * e2 - e2 * e2 * e2) / 6.0;
        var C = (104.0 * e2 * e2 * e2 - 45.0 * e2 * e2 * e2 * e2) / 120.0;
        var D = (1237.0 * e2 * e2 * e2 * e2) / 1260.0;
        var beta1 = n / 2.0 - 2.0 * n * n / 3.0 + 5.0 * n * n * n / 16.0 + 41.0 * n * n * n * n / 180.0;
        var beta2 = 13.0 * n * n / 48.0 - 3.0 * n * n * n / 5.0 + 557.0 * n * n * n * n / 1440.0;
        var beta3 = 61.0 * n * n * n / 240.0 - 103.0 * n * n * n * n / 140.0;
        var beta4 = 49561.0 * n * n * n * n / 161280.0;

        // Convert.
        var deg_to_rad = Math.PI / 180.0;
        var phi = latitude * deg_to_rad;
        var lambda = longitude * deg_to_rad;
        var lambda_zero = central_meridian * deg_to_rad;

        var phi_star = phi - Math.sin(phi) * Math.cos(phi) * (A +
                B * Math.pow(Math.sin(phi), 2) +
                C * Math.pow(Math.sin(phi), 4) +
                D * Math.pow(Math.sin(phi), 6));
        var delta_lambda = lambda - lambda_zero;
        var xi_prim = Math.atan(Math.tan(phi_star) / Math.cos(delta_lambda));
        var eta_prim = atanh(Math.cos(phi_star) * Math.sin(delta_lambda));
        var x = scale * a_roof * (xi_prim +
                beta1 * Math.sin(2.0 * xi_prim) * Math.cosh(2.0 * eta_prim) +
                beta2 * Math.sin(4.0 * xi_prim) * Math.cosh(4.0 * eta_prim) +
                beta3 * Math.sin(6.0 * xi_prim) * Math.cosh(6.0 * eta_prim) +
                beta4 * Math.sin(8.0 * xi_prim) * Math.cosh(8.0 * eta_prim)) +
                false_northing;
        var y = scale * a_roof * (eta_prim +
                beta1 * Math.cos(2.0 * xi_prim) * Math.sinh(2.0 * eta_prim) +
                beta2 * Math.cos(4.0 * xi_prim) * Math.sinh(4.0 * eta_prim) +
                beta3 * Math.cos(6.0 * xi_prim) * Math.sinh(6.0 * eta_prim) +
                beta4 * Math.cos(8.0 * xi_prim) * Math.sinh(8.0 * eta_prim)) +
                false_easting;

        return new Coordinate(x, y);
    }

    private static HashMap<Coordinate, Double> elevationCache = new HashMap<Coordinate, Double>();

    /**
     * Get the elevation of a coordinate.
     * 
     * This uses the lantmäteriets API to get the elevation of a coordinate.
     * 
     * Results are cached, subsequent calls to this method with the same coordinate
     * will return the cached value.
     * 
     * @param coordinate The coordinate in longitude and latitude (WGS84).
     * @return The elevation.
     */
    public static double getElevation(Coordinate coordinate) {
        if (elevationCache.containsKey(coordinate)) {
            return elevationCache.get(coordinate);
        }

        var url = "https://minkarta.lantmateriet.se/api/positionsinformation/positionsinformation/v1/hojd?transactionId=a&east={1}&north={2}";
        var gridCoordinate = geodeticToGrid(coordinate.latitude, coordinate.longitude);

        url = url.replace("{1}", String.valueOf(gridCoordinate.getLongitude()));
        url = url.replace("{2}", String.valueOf(gridCoordinate.getLatitude()));

        RestTemplate restTemplate = new RestTemplate();
        PositionInformationResponseJson result = restTemplate.getForObject(url, PositionInformationResponseJson.class);

        var elevation = result.getHojd();

        elevationCache.put(coordinate, elevation);

        return elevation;
    }

    /**
     * Render an image of the map.
     * 
     * @param width    The width of the image.
     * @param height   The height of the image.
     * @param eastMin  The minimum east coordinate.
     * @param northMin The minimum north coordinate.
     * @param eastMax  The maximum east coordinate.
     * @param northMax The maximum north coordinate.
     * @return The image.
     * @throws IOException If the image could not be rendered.
     */
    public static BufferedImage renderImage(int width, int height, int northMin, int eastMin, int northMax, int eastMax)
            throws IOException {
        final var service = "WMS";
        final var version = "1.1.1";
        final var request = "GetMap";
        final var format = "image%2Fpng";
        final var transparent = "false";
        final var layers = "topowebbkartan";
        final var tiled = "true";
        final var styles = "";
        final var srs = "EPSG%3A3006";

        final var bbox = String.format("%d,%d,%d,%d", (int) eastMin, (int) northMin, (int) eastMax, (int) northMax);

        // "https://minkarta.lantmateriet.se/map/topowebb/?SERVICE={service}&VERSION={version}&REQUEST={request}&FORMAT={format}&TRANSPARENT={transparent}&LAYERS={layers}&TILED={tiled}&STYLES={styles}&WIDTH={width}&HEIGHT={height}&SRS={srs}&BBOX={bbox}";
        final var url = String.format(
                "https://minkarta.lantmateriet.se/map/topowebb/?SERVICE=%s&VERSION=%s&REQUEST=%s&FORMAT=%s&TRANSPARENT=%s&LAYERS=%s&TILED=%s&STYLES=%s&WIDTH=%d&HEIGHT=%d&SRS=%s&BBOX=%s",
                service,
                version,
                request,
                format,
                transparent,
                layers,
                tiled,
                styles,
                width,
                height,
                srs,
                bbox);

        final var image = ImageIO.read(new URL(url));

        return image;
    }
}
