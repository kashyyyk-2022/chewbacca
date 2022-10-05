package com.kashyyyk.chewbacca.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoutingService {
    private RoutingStorage storage;

    private ExecutorService executorService;

    public RoutingService(RoutingStorage storage, int threads) {
        this.storage = storage;
        this.executorService = Executors.newFixedThreadPool(threads);
    }
    
    /**
     * Generate a new route
     * 
     * @param startLat          the latitude of the start point
     * @param startLon          the longitude of the start point
     * @param length            the length of the route
     * @param time              the time of the route
     * @param elevation         the elevation of the route
     * @param terrain           the terrain type of the route
     * @return                  the id of the new route
     */
    public String generateRoute(double startLat, double startLon, double length, double timeHours, double timeMinutes, double elevation, int terrain) {
        var id = storage.newRoute();

        executorService.submit(() -> {
            // TODO: generate route
            // Wait for 10 seconds for testing
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Set the route to done
            storage.setRouteDone(id, true);
            
            storage.setRoute(id, new double[][] { 
                { 57.68939495267853, 11.974072522154591 }, 
                { 57.690375578417566, 11.973305940628054 }, 
                { 57.69218190152542, 11.973670721054079 }, 
                { 57.69351221524594, 11.972994804382326 }, 
                { 57.69710593033657, 11.970380880735673 }, 
                { 57.69657814788056, 11.966474609001716 }, 
                { 57.695820881360234, 11.966646313253772 }, 
                { 57.69370143693639, 11.966772079467775 }, 
                { 57.69370963050795, 11.968148725459137 }, 
                { 57.69329654529788, 11.96849213396321 }, 
                { 57.69359822522775, 11.969239711761476 }, 
                { 57.692608059481266, 11.970380880735673 }, 
                { 57.69309000092662, 11.971582810499989 }, 
                { 57.69178185923145, 11.972913518453298 }
            });
        });

        return id;
    }

    /**
     * Check if a route is done
     * 
     * @param id                the id of the route
     * @return                  true if the route is done, false otherwise
     */
    public boolean isRouteDone(String id) {
        return storage.isRouteDone(id);
    }

    /**
     * Get the route with the given id
     * 
     * @param id                the id of the route
     * @return                  the route
     */
    public double[][] getRoute(String id) {
        return storage.getRoute(id);
    }
}
