package com.kashyyyk.chewbacca.map;


/**
 * This class represents a point on the earth in the form of a latitude-longitude-pair value
 */
public class Point {

    /**
     * A value representing the latitude of a position in degrees.
     */
    private final double latitude;
    /**
     * A value representing the longitude of a position in degrees.
     */
    private final double longitude;

    /**
     * An approximation of the radius of the earth in kilometers.
     */
    private static final double R_EARTH = 6371.0;

    /**
     * Creates a point representing the latitude and longitude of a position in degrees.
     * @param latitude The latitude of a position in degrees.
     * @param longitude The longitude of a position in degrees.
     */
    public Point(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     *
     * @return The latitude in degrees of a position.
     */
    public double getLatitude() { return this.latitude; }

    /**
     *
     * @return The longitude in degrees of a position.
     */
    public double getLongitude() { return this.longitude; }

    /**
     *
     * @param dx Displacement along the x-axis in kilometers.
     * @param dy Displacement along the y-axis in kilometers.
     * @return A new point with latitude and longitude after moving dx and dy
     */
    public Point move(double dx, double dy){
        return new Point(this.latitude + (dy/R_EARTH)*(180/Math.PI),
                this.longitude + (dx/R_EARTH)*((180/Math.PI)/Math.cos(this.latitude*(Math.PI/180))));
    }


}
