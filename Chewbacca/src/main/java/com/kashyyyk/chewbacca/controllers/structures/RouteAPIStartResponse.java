package com.kashyyyk.chewbacca.controllers.structures;

/**
 * A response for the route api start
 */
public class RouteAPIStartResponse {

    /**
     * The id of the route
     */
    private String id;

    /**
     * Create a new route start response
     * 
     * @param id the id of the route
     */
    public RouteAPIStartResponse(String id) {
        this.id = id;
    }

    /**
     * Get the id of the route
     * 
     * @return the id of the route
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id of the route
     * 
     * @param id the new id of the route
     */
    public void setId(String id) {
        this.id = id;
    }
}
