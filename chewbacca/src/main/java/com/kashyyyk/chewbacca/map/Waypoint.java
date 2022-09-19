package com.kashyyyk.chewbacca.map;

/**
 * A waypoint on the map.
 * 
 * It can be a POI, a road, a trail, a lake, a river, etc.
 */
public class Waypoint {
    /**
     * The coordinate of the waypoint.
     */
    private Coordinate coordinate;
    
    /**
     * The elevation of the waypoint.
     */
    private double elevation; 

    /**
     * The type of the waypoint.
     */
    private WaypointType type;

    /**
     * The size of the waypoint.
     */
    private double size;

    /**
     * Creates a new waypoint.
     * 
     * @param coordinate    The coordinate of the waypoint.
     * @param elevation     The elevation of the waypoint.
     * @param type          The type of the waypoint.
     * @param size          The size of the waypoint.
     */
    public Waypoint(Coordinate coordinate, double elevation, WaypointType type, double size) {
        this.coordinate = coordinate;
        this.elevation = elevation;
        this.type = type;
        this.size = size;
    }

    /**
     * Gets the coordinate of the waypoint.
     * 
     * @return The coordinate of the waypoint.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Sets the coordinate of the waypoint.
     * 
     * @param coordinate The coordinate of the waypoint.
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Gets the elevation of the waypoint.
     * 
     * @return The elevation of the waypoint.
     */
    public double getElevation() {
        return elevation;
    }

    /**
     * Sets the elevation of the waypoint.
     * 
     * @param elevation The elevation of the waypoint.
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    /**
     * Gets the type of the waypoint.
     * 
     * @return The type of the waypoint.
     */
    public WaypointType getType() {
        return type;
    }

    /**
     * Sets the type of the waypoint.
     * 
     * @param type The type of the waypoint.
     */
    public void setType(WaypointType type) {
        this.type = type;
    }

    /**
     * Gets the size of the waypoint.
     * 
     * @return The size of the waypoint.
     */
    public double getSize() {
        return size;
    }

    /**
     * Sets the size of the waypoint.
     * 
     * @param size The size of the waypoint.
     */
    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "coordinate=" + coordinate +
                ", elevation=" + elevation +
                ", type=" + type +
                '}';
    }
}
