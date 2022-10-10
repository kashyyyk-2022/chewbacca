package com.kashyyyk.chewbacca.services;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kashyyyk.chewbacca.map.Point;
import com.kashyyyk.chewbacca.map.WaypointGenerator;
import com.kashyyyk.chewbacca.map.rstar.RoutingStar;

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
     * @param timeHours         the time of the route in hours
     * @param timeMinutes       the time of the route in minutes
     * @param elevation         the elevation of the route
     * @param terrain           the terrain type of the route
     * @return                  the id of the new route
     */
    public String generateRoute(double startLat, double startLon, double length, double timeHours, double timeMinutes, double elevation, String terrain) {
        var id = storage.newRoute();

        executorService.submit(() -> {
            try {
                //final WaypointGenerator generator = new WaypointGenerator(startLat, startLon, length, timeHours, timeMinutes, elevation, terrain);
                
                var rstar = new RoutingStar();
                rstar.start = new Point(startLat, startLon);
                rstar.idealDistance = length;
                //rstar.idealTerrain = new String[] { terrain };
                rstar.distanceBias = 1f;
                rstar.distanceToStartBias = 0.01f;
                rstar.elevationBias = 1;
                rstar.terrainBias = 2f;
                rstar.surfaceBias = 1;
                rstar.seed = 0;

                if (timeMinutes == 5) {
                    rstar.start = new Point(57.67437, 11.95678);
                }

                rstar.Initialize();

                var points = rstar.getRoute();

                // Set the route to done
                storage.setRouteDone(id, true);

                //LinkedList<WaypointGenerator.Waypoint> points = generator.getRoute();

                // Extract the route from the waypoints into a 2D array
                double[][] route = new double[points.size()][2];
                int i = 0;
                for (Point point : points) {
                    route[i][0] = point.getLatitude();
                    route[i][1] = point.getLongitude();
                    i++;
                }

                storage.setRoute(id, route);

            } catch (Exception e) {
                // TODO: Set error
                e.printStackTrace();
            }
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
