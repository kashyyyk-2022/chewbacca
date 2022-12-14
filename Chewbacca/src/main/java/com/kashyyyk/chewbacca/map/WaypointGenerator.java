package com.kashyyyk.chewbacca.map;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

// Will create a list of locations for the route. The list will be read by CurrentRoute.html to draw the route on the map
public class WaypointGenerator {
    private double totalDistance;
    private double halfDistance;
    private double totalElevation;
    //Create a preference type variable (ENUM)?????.

    private static String[] Terrains_Urban = {"city","residential","urban"};
    private static String[] Terrains_Nature = {"wood","forest"};
    private static String[] Terrains_Water = {"water","coastline","ocean"};

    private static String[][] Terrains = {
        Terrains_Urban, Terrains_Nature, Terrains_Water
    };


    private double startLat;
    private double startLon;

    private boolean routeDone = false;

    private LinkedList<Waypoint> points = new LinkedList<>();

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


    public final boolean testing = false;

    /**
     * This constructor call should generate a waypoint that fulfills all our preferences and criterias.
     * @param length -should be 0 if length is not specified by user. In this case time will be used instead
     * Needs to be done:
     *  - checkDistance
     *  - checkElevation
     *  - checkTerrain
     *
     * @throws IOException
     */

    public WaypointGenerator(double startLat, double startLon, double length, double timeHours, double timeMinutes, double elevation, String terrain) throws Exception {
        if(testing){
            this.totalDistance = 10000; //Dummy totalDistance. Use totalDistance;
            this.halfDistance = totalDistance/2;
            this.totalElevation = 100; //Dummy totalElevation. Use totalElevation;
            this.startLat = 57.68939495267853;
            this.startLon = 11.974072522154591;
        }
        else{
            this.startLat = startLat;
            this.startLon = startLon;
            this.totalDistance = length!=0 ? length * 1000 : timeToDistance(timeHours,timeMinutes);
            this.totalElevation = elevation;
            this.halfDistance = totalDistance/2;
        }

        double tempLat = this.startLat;
        double tempLon = this.startLon;
        Osm osm = OpenStreetMap.downloadData(57.692622286683225,11.966922283172607, 57.69652702997704,11.972200870513916);

        int currentNode = 0;
        double endLat = osm.node[currentNode].lat;
        double endLon = osm.node[currentNode].lon;

        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("TotalDistance: " + totalDistance);
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
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
        while(!routeDone){
            OsrmResult osrm = OpenSourceRoutingMachine.getRoute(tempLat, tempLon, endLat, endLon);
            System.out.println(osrm.routes[0].distance);
            if(checkDistance(tempLat, tempLon, endLat, endLon) && checkElevation() && checkTerrain(osm.node[currentNode], terrain)){
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
            currentNode += 3;
            endLat = osm.node[currentNode].lat;
            endLon = osm.node[currentNode].lon;

            if(routeDone){
                osrm = OpenSourceRoutingMachine.getRoute(tempLat, tempLon, startLat, startLon);
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
    }

    public void testElevation() throws IOException{
        checkElevation();
    }

    public LinkedList<Waypoint> getRoute(){
        return points;
    }

    //TODO: Create a system that checks if the waypoint is out of our distance requirement.
    private boolean checkDistance(double startLat, double startLon, double endLat, double endLon) throws Exception {
        OsrmResult osrm = OpenSourceRoutingMachine.getRoute(startLat, startLon, endLat, endLon);
        if(totalDistance - osrm.routes[0].distance < halfDistance){
            routeDone = true;
            return false;
        }
        totalDistance = totalDistance - osrm.routes[0].distance;
        System.out.println("TotalDistanceLeft: " + totalDistance);
        return true;
    }

    //TODO: Create a system that checks if the waypoint is out of our preferred elevation.
    private boolean checkElevation(){
        return true;
    }

    //TODO: Create a system that checks if the waypoint is considered a preferred terrain.
    private boolean checkTerrain(Osm.Node n, String terrain){
        int a = 0;
        switch (terrain) {
            case "Urban" -> a = 0;
            case "Nature" -> a = 1;
            case "Water" -> a = 2;
        }
        int i = 0;
        while(n.tag[i] != null) {
            if(Terrains[a][0].contains(n.tag[i].v)) {
                return true;
            }
            i++;
        }

        return false;
    }

    //method to get distance in meters by using time and average walking speed 5 km/h
    private double timeToDistance(double hour,double minutes){
        return (hour+minutes/60)*5*1000;
    }

    public Iterator<Waypoint> getIterator() {
        return points.iterator();
    }

}
