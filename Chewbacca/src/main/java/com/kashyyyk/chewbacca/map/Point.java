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

    /**
     * Calculates the distance between two points in kilometers.
     * 
     * @param p1 The first point.
     * @param p2 The second point.
     * @return The distance between the two points in kilometers.
     */
    public static double distance(Point p1, Point p2){
        double lat1 = p1.getLatitude();
        double lat2 = p2.getLatitude();
        double lon1 = p1.getLongitude();
        double lon2 = p2.getLongitude();
        double dLat = (lat2-lat1)*(Math.PI/180);
        double dLon = (lon2-lon1)*(Math.PI/180);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1*(Math.PI/180)) * Math.cos(lat2*(Math.PI/180)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R_EARTH * c;
    }

    /**
     * Get a comparable distance between two points. This method is a lot
     * faster than distance(Point p1, Point p2) but doesn't give a value which
     * represents any tangible distance, just a value which can be used to
     * compare distances between two points.
     * 
     * @param p1 The first point.
     * @param p2 The second point.
     * @return A comparable distance between the two points.
     */
    public static double comparableDistance(Point p1, Point p2){
        return Math.abs(p1.getLatitude() - p2.getLatitude()) + Math.abs(p1.getLongitude() - p2.getLongitude());
    }

    /**
     * Equals method for Point.
     * 
     * @param o The object to compare to.
     * @return True if the object is a point and has the same latitude and longitude as this point.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Point){
            Point p = (Point) o;
            return this.latitude == p.getLatitude() && this.longitude == p.getLongitude();
        }
        return false;
    }

    /**
     * Hashcode method for Point.
     * 
     * @return The hashcode of the point.
     */
    @Override
    public int hashCode(){
        return (int) (this.latitude + this.longitude);
    }

}
