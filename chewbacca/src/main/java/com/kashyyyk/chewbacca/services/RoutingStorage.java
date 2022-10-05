package com.kashyyyk.chewbacca.services;

/**
 * An interface for storing and retrieving routes.
 */
public interface RoutingStorage {

    /**
     * Create a new route entry
     * 
     * @return the id of the new route entry
     */
    public String newRoute();

    /**
     * Check if a route is done
     * 
     * @return true if the route is done, false otherwise
     */
    public boolean isRouteDone(String id);

    /**
     * Get the route with the given id
     * 
     * @param id the id of the route
     * @return the route
     */
    public double[][] getRoute(String id);

    /**
     * Set if the route is done
     * 
     * @param id the id of the route
     * @param done true if the route is done, false otherwise
     */
    public void setRouteDone(String id, boolean done);

    /**
     * Set the route with the given id
     * 
     * @param id    the id of the route
     * @param route the route
     */
    public void setRoute(String id, double[][] route);

    /**
     * Delete the route with the given id
     * 
     * @param id the id of the route
     */
    public void deleteRoute(String id);
}
