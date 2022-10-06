package com.kashyyyk.chewbacca.map.rstar;

import com.kashyyyk.chewbacca.map.Point;

/**
 * A feature in an R* graph.
 */
public class RFeature {
    
    /**
     * Id of the feature
     */
    public final long id;
    
    /**
     * The points of the polygon
     */
    public final Point[] points;

    /**
     * The tags of the feature
     */
    public String[] tags;

    /**
     * Constructor for RFeature
     * 
     * @param id                        Id of the feature
     * @param points                    The points of the polygon
     * @param tags                      The tags of the feature
     */
    public RFeature(long id, Point[] points, String[] tags) {
        this.id = id;
        this.points = points;
        this.tags = tags;
    }

    /**
     * Check if a point is inside the polygon
     * 
     * @param point                     The point to check
     */
    public boolean contains(Point point) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].getLatitude() > point.getLatitude()) != (points[j].getLatitude() > point.getLatitude()) &&
                (point.getLongitude() < (points[j].getLongitude() - points[i].getLongitude()) * (point.getLatitude() - 
                 points[i].getLatitude()) / (points[j].getLatitude() - points[i].getLatitude()) + points[i].getLongitude())) {
                result = !result;
            }
        }
        return result;
    }

    /**
     * Get the closest point on the polygon to a point
     * 
     * @param point                     The point to check
     */
    public Point closestPoint(Point point) {
        Point closestPoint = null;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            double distance = Point.comparableDistance(point, points[i]);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPoint = points[i];
            }
        }
        return closestPoint;
    }
}
