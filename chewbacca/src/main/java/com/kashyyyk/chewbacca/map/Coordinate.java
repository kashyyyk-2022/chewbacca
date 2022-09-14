package com.kashyyyk.chewbacca.map;

/**
 * A coordinate in latitude and longitude.
 * 
 * @author Wincent Stålbert Holm
 */
public class Coordinate {
    /**
     * The latitude.
     */
    public final double latitude;
    
    /**
     * The longitude.
     */
    public final double longitude;

    /**
     * Creates a new coordinate.
     * 
     * @param latitude      The latitude.
     * @param longitude     The longitude.
     */
    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the latitude.
     * 
     * @return The latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude.
     * 
     * @return The longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        return Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
