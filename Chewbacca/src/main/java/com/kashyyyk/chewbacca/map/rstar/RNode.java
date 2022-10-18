package com.kashyyyk.chewbacca.map.rstar;

import com.kashyyyk.chewbacca.map.Point;

/**
 * A node in an R* graph.
 */
public class RNode {
    
    /**
     * Id of the node
     */
    public final long id;

    /**
     * Point of the node
     */
    public final Point point;

    /**
     * Terrain of the node
     */
    public String terrain;

    /**
     * Surface of the node
     */
    public String surface;

    /**
     * Elevation of the node
     */
    public double elevation;

    /**
     * Constructor for RNode
     * 
     * @param id                        Id of the node
     * @param point                     The point of the node
     * @param terrain                   The terrain of the node
     * @param surface                   The surface of the node
     * @param elevation                 The elevation of the node
     */
    public RNode(long id, Point point, String terrain, String surface, float elevation) {
        this.id = id;
        this.point = point;
        this.terrain = terrain;
        this.surface = surface;
        this.elevation = elevation;
    }

    /**
     * Constructor for RNode
     * 
     * @param id                        Id of the node
     * @param point                     The point of the node
     */
    public RNode(long id, Point point) {
        this(id, point, null, null, 0);
    }
}
