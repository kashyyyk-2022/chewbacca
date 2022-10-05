package com.kashyyyk.chewbacca.map;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

// Will create a list of locations for the route. The list will be read by CurrentRoute.html to draw the route on the map
public class WaypointGenerator {
    private double totalDistance;
    private double totalElevation;
    //Create a preference type variable (ENUM)????? .

    double startLat;
    double startLon;

    public class Waypoint {
        private double lat;
        private double lon;

        public Waypoint(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() { return lat;}
        public double getlon() {return lon;}

        public String toString(){
            return "["+ lat + ", "+lon +"]";
        }
    }

    private LinkedList<Waypoint> points = new LinkedList<>();

    /**
     * This constructor call should generate a waypoint that fulfills all our preferences and criterias.
     *
     * Needs to be done:
     *  - checkDistance
     *  - checkElevation
     *  - checkTerrain
     *
     * @throws IOException
     */

    public final boolean testing = true;

    public WaypointGenerator(double startLat, double startLon, double length, double timeHours, double timeMinutes, double elevation, String terrain) throws Exception {
        if(testing){
            this.totalDistance = 5000; //Dummy totalDistance. Use totalDistance;
            this.totalElevation = 100; //Dummy totalElevation. Use totalElevation;
            this.startLat = 57.68939495267853;
            this.startLon = 11.974072522154591;
        }
        else{
            this.startLat = startLat;
            this.startLon = startLon;
            this.totalDistance = length * 1000;
            this.totalElevation = elevation;
        }

        double tempLat = this.startLat;
        double tempLon = this.startLon;
        double halfDistance = this.totalDistance * 0.5;
        Osm osm = OpenStreetMap.downloadData(57.692622286683225,11.966922283172607, 57.69652702997704,11.972200870513916);

        int currentNode = 0;
        double endLat = osm.node[currentNode].lat;
        double endLon = osm.node[currentNode].lon;
        int routeDone = 0;
        //TODO: Make the downloadData "box" adjust to the distance we want the route to be(?)
        /*for (int i = 0; i < osm.node.length; i++) {
            System.out.println("Lat: " + osm.node[i].lat +" lon: " + osm.node[i].lon);
            if(osm.node[i].tag != null) {
                for(int j = 0; j < osm.node[i].tag.length; j++) {
            //        System.out.println(osm.node[i].tag[j].k + " = " + osm.node[i].tag[j].v);
                }

            }
            if(osm.way[i].tag != null) {
                for(int j = 0; j < osm.way[i].tag.length; j++) {
                    System.out.println(osm.way[i].tag[j].k + " = " + osm.way[i].tag[j].v);
                }
            }
        }*/

        /*//How to get location Lat and Lon
        try {
            OsrmResult osrm = OpenSourceRoutingMachine.getRoute(startLat, startLon, endLat, endLon);
            System.out.println(Arrays.toString(osrm.routes[0].legs[0].steps[0].intersections[0].location));
            double locLon = osrm.routes[0].legs[0].steps[0].intersections[0].location[0];
            double locLat = osrm.routes[0].legs[0].steps[0].intersections[0].location[1];
            System.out.println("Lat: " + osm.node[0].lat +" lon: " + osm.node[0].lon);
            System.out.println(locLat + ", " + locLon);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        while(routeDone < 5){
            OsrmResult osrm = OpenSourceRoutingMachine.getRoute(tempLat, tempLon, endLat, endLon);
            if(checkElevation() && checkTerrain()){
                for(int i = 0; i < osrm.routes.length; i++){
                    var route = osrm.routes[i];
                    // For each leg in the route
                    for (int j = 0; j < route.legs.length; j++) {
                        var leg = route.legs[j];
                        // For each step in the leg
                        for (int k = 0; k < leg.steps.length; k++) {
                            var step = leg.steps[k];
                            // For each intersection in the step
                            for (int l = 0; l < step.intersections.length; l++) {
                                var intersection = step.intersections[l];
                                points.add(new Waypoint(intersection.location[1], intersection.location[0]));
                            }
                        }
                    }
                }
            }
            tempLat = endLat;
            tempLon = endLon;
            System.out.println("tmpLat: " + tempLat + " tempLon:" + tempLon);
            currentNode++;
            endLat = osm.node[currentNode].lat;
            endLon = osm.node[currentNode].lon;
            routeDone++;
        }
        OsrmResult osrm = OpenSourceRoutingMachine.getRoute(endLat, endLon, startLat, startLon);
        if(checkElevation() && checkTerrain()){
            for(int i = 0; i < osrm.routes.length; i++){
                var route = osrm.routes[i];
                // For each leg in the route
                for (int j = 0; j < route.legs.length; j++) {
                    var leg = route.legs[j];
                    // For each step in the leg
                    for (int k = 0; k < leg.steps.length; k++) {
                        var step = leg.steps[k];
                        // For each intersection in the step
                        for (int l = 0; l < step.intersections.length; l++) {
                            var intersection = step.intersections[l];
                            points.add(new Waypoint(intersection.location[1], intersection.location[0]));
                        }
                    }
                }
            }
        }
    }

    public void testElevation() throws IOException{
        checkElevation();
    }

    public LinkedList<Waypoint> getRoute(){
        return points;
    }

    private void addWaypoint(double lat, double lon){

    }

    //TODO: Create a system that checks if the waypoint is out of our distance requirement.
    private boolean checkDistance(double endLat, double endLon) throws Exception {
        OsrmResult osrm = OpenSourceRoutingMachine.getRoute(startLat, startLon, endLat, endLon);
        if(totalDistance - osrm.routes[0].distance < 0){
            return false;
        }
        totalDistance = totalDistance - osrm.routes[0].distance;

        return true;
    }

    //TODO: Create a system that checks if the waypoint is out of our preferred elevation.
    private boolean checkElevation(){
        return true;
    }

    //TODO: Create a system that checks if the waypoint is considered a preferred terrain.
    private boolean checkTerrain(){
        return true;
    }

    public Iterator<Waypoint> getIterator() {
        return points.iterator();
    }

}
