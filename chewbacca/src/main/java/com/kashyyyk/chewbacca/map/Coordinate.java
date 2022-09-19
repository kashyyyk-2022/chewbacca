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

    /**
     * Get the distance in units between two coordinates, not meters. Does not take elevation into account.
     * 
     * @param coordinate    The coordinate to calculate the distance to.
     * @return              The distance in units between the two coordinates.
     */
    public int distanceTo(Coordinate coordinate) {
        return (int) Math.sqrt(Math.pow(this.latitude - coordinate.latitude, 2) + Math.pow(this.longitude - coordinate.longitude, 2));
    }

    /**
     * Get the distance in meters between two coordinates. Does not take elevation into account.
     * 
     * @param coordinate    The coordinate to calculate the distance to.
     * @return              The distance in meters between the two coordinates.
     */
    public double distanceToInMeters(Coordinate coordinate) {
        final double R = 6378.137; // Radius of earth in KM

        double φ1 = Math.toRadians(this.latitude);
        double φ2 = Math.toRadians(coordinate.latitude);
        double Δφ = Math.toRadians(coordinate.latitude-this.latitude);
        double Δλ = Math.toRadians(coordinate.longitude-this.longitude);

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;

        return d * 1000;
    }

    /**
     * Take the average of two coordinates.
     * 
     * @param coordinate    The coordinate to average with.
     * @return              The average of the two coordinates.
     */
    public Coordinate average(Coordinate coordinate) {
        return new Coordinate((this.latitude + coordinate.latitude) / 2, (this.longitude + coordinate.longitude) / 2);
    }
}
