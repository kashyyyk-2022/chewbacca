package com.kashyyyk.chewbacca.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kashyyyk.chewbacca.map.Point;
import com.kashyyyk.chewbacca.map.rstar.KeyValue;
import com.kashyyyk.chewbacca.map.rstar.RoutingStar;
import com.kashyyyk.chewbacca.map.rstar.TerrainsKeyValue;

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

    public String generateRoute(double startLat, double startLon, double length, double timeHours, double timeMinutes, double elevation, String terrain, boolean accessible) {
        var id = storage.newRoute();

        executorService.submit(() -> {
            try {
                //final WaypointGenerator generator = new WaypointGenerator(startLat, startLon, length, timeHours, timeMinutes, elevation, terrain);

                var rstar = new RoutingStar();
                rstar.start = new Point(startLat, startLon);
                rstar.idealDistance = length!=0 ? length : timeToDistance(timeHours,timeMinutes,true); //If we want to generate a route for a runner boolean should be false
                rstar.idealTerrain = TerrainsKeyValue.getTerrainKV(terrain);
                rstar.distanceBias = 1f;
                rstar.distanceToStartBias = 0.01f;
                
                double elev;
                switch(elevation){
                    case "low": elev=500.0; break;
                    case "medium": elev=0.0; break;
                    default: elev=-50.0; break;
                };
                if(accessible){
                    rstar.elevationBias = (elev / 500.0) * 0.1f * 100;
                }
                else{
                    rstar.elevationBias = (elev / 500.0) * 0.1f;
                }
                
                rstar.terrainBias = 0.1f;
                rstar.surfaceBias = 1;
                rstar.seed = System.currentTimeMillis();
                rstar.randomBias = 0.0000001f;
                rstar.accessibility = accessible; //Kan jag inte göra detta? Test - Marcus

                rstar.Initialize();

                if (timeMinutes == 5) {
                    rstar.start = new Point(57.67437, 11.95678);
                }


                var points = rstar.getRoute();

                // Set the route to done
                storage.setRouteDone(id, true);

                var start = rstar.getStartPoint();
                var end = rstar.getEndPoint();

                storage.setRouteStart(id, new double[] { start.getLatitude(), start.getLongitude() });
                storage.setRouteEnd(id, new double[] { end.getLatitude(), end.getLongitude() });
                storage.setRouteLabels(id, rstar.getLabels());

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

    /**
     * Get the start of the route with the given id
     * 
     * @param id                the id of the route
     * @return                  the start of the route
     */
    public double[] getRouteStart(String id) {
        return storage.getRouteStart(id);
    }

    /**
     * Get the end of the route with the given id
     * 
     * @param id                the id of the route
     * @return                  the end of the route
     */
    public double[] getRouteEnd(String id) {
        return storage.getRouteEnd(id);
    }

    /**
     * Get the route labels with the given id
     * 
     * @param id                the id of the route
     * @return                  the route labels
     */
    public RouteLabel[] getRouteLabels(String id) {
        return storage.getRouteLabels(id);
    }

    /** Method to get ideal distance of a route based on the time the user want to walk/run
     * @param hour - Hours the walk/run should be
     * @param minutes - Minutes the walk/run should be
     * @param walk - A boolean that should be set to true if user wants to walk, or false if user wants to run
     * @return The optimal distance (in km) for a route that should last for a given time as a double
     */
    private double timeToDistance(double hour,double minutes, boolean walk){ return walk ? (hour+minutes/60)*5 : (hour+minutes/60)*8.4;}

}
