package com.kashyyyk.chewbacca.map.rstar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import com.kashyyyk.chewbacca.map.OpenSourceRoutingMachine;
import com.kashyyyk.chewbacca.map.Osm;
import com.kashyyyk.chewbacca.map.OsmDatabase;
import com.kashyyyk.chewbacca.map.Point;
import com.kashyyyk.chewbacca.map.Osm.Node;
import com.kashyyyk.chewbacca.map.Osm.Way;

/**
 * <h4>R* — Routing Star</h4>
 * 
 * <p>Routing Star is an algorithm for generating a route on a graph following a
 * set of constraints and biases. It is a variant of the A* algorithm.</p>
 * 
 * <p>In constrast to A*, R* does not seek an <b>end</b> node, but rather seeks to
 * find a path around a graph that satisfies a set of constraints and biases.</p>
 * 
 * <p>Prototype by Wincent.</p>
 */
public class RoutingStar {
    /**
     * The start point of the route.
     */
    public Point start;

    /**
     * The ideal distance of the route
     */
    public double idealDistance;

    /**
     * The ideal terrain of the route
     */
    public String[] idealTerrain;

    /**
     * The ideal surface of the route
     */
    public String[] idealSurface;

    /**
     * The bias for the distance
     */
    public float distanceBias;

    /**
     * The bias for the distance to the start
     */
    public float distanceToStartBias;

    /**
     * The bias for the elevation
     */
    public float elevationBias;

    /**
     * The bias for the terrain
     */
    public float terrainBias;

    /**
     * The bias for the surface
     */
    public float surfaceBias;

    /**
     * RNG seed
     */
    public long seed;

    /**
     * RNG
     */
    private Random random;

    /**
     * OsmDatabase
     */
    private OsmDatabase database;

    /**
     * The current route
     */
    private ArrayList<Point> route;

    /**
     * The current distance
     */
    private float distance;

    /**
     * The linked routing star graph
     */
    private RoutingStarGraph graph;

    /**
     * The priority queue
     */
    private PriorityQueue<REntry> priorityQueue;

    /**
     * Visited nodes
     */
    private HashSet<Long> visited;

    /**
     * Get the route
     * 
     * @return the route
     */
    public List<Point> getRoute() {
        return route;
        /*var result = new ArrayList<Point>();

        var previous = route.get(0);
        // Use OSRM to get the route between the points
        for (var i = 1; i < route.size(); i++) {
            var current = route.get(i);

            result.addAll(runAStar(previous, current));

            previous = current;
        }

        return result;*/
    }

    /**
     * Get the database
     * 
     * @return the database
     */
    public OsmDatabase getDatabase() {
        return database;
    }

    public void Initialize() throws IOException
    {
        random = new Random(seed);

        graph = new RoutingStarGraph();

        database = new OsmDatabase(graph);

        route = new ArrayList<Point>();

        visited = new HashSet<Long>();

        distance = 0;


        database.downloadData(start);

        var origin = graph.findNearestNode(start);


        start = runRStar(null).node.point;

        distanceToStartBias = 1;
        terrainBias = 1;

        /*runRStar(origin);

        route.remove(route.size() - 1);
        route.remove(route.size() - 1);*/

        //runAStar(start, origin.point);

        /*
        var paths = database.queryWays("highway", "track", "path");

        for (var path : paths) {
            var nodes = database.getNodes(path);

            for (var node : nodes) {
                route.add(new Point(node.lat, node.lon));
            }
        } */
    }

    private REntry runRStar(RNode destination) throws IOException {
        var features = graph.getFeatures("water");  //todo Ensure that features is collected from graph
        var nearest = graph.findNearestNode(start);

        visited.clear();

        priorityQueue = new PriorityQueue<REntry>(Comparator.comparingDouble((REntry entry) -> {
            if (destination != null) {
                return Point.comparableDistance(entry.node.point, destination.point);
            }

            double p = entry.cost;
            p += distanceToStartBias * Point.comparableDistance(entry.node.point, start);
            p += terrainBias * getClosestFeaturePoint(entry.node.point, features);
            return p;
        }));

        priorityQueue.add(new REntry(
            nearest,
            0,
            null,
            0,
            null
        ));

        int iterations = 0;

        REntry end = null;

        int maxDownloads = 10;

        while (!priorityQueue.isEmpty())
        {
            iterations++;

            final var currentEntry = priorityQueue.poll();
            final var current = currentEntry.node;

            if (visited.contains(current.id)) {
                continue;
            }

            visited.add(current.id);

            end = currentEntry;

            if (destination != null && current == destination) {
                break;
            }

            if (currentEntry.distance > idealDistance || Point.distance(start, current.point) > idealDistance / 2) {
                break;
            }

            double p = currentEntry.cost;
            p += distanceToStartBias * Point.comparableDistance(current.point, start);
            p += terrainBias * getClosestFeaturePoint(current.point, features);

            var connected = graph.getConnected(current);

            if (connected.size() == 1 && maxDownloads > 0) {
                database.downloadData(current.point);

                connected = graph.getConnected(current);

                maxDownloads--;

                System.out.println("Downloaded data");
            }

            for (RNode edgeNode : connected) {
                final var cost = p;

                final var edgeEntry = new REntry(
                    edgeNode,
                    cost,
                    current,
                    currentEntry.cost,
                    currentEntry
                );

                edgeEntry.distance = currentEntry.distance + Point.distance(current.point, edgeNode.point);

                priorityQueue.add(edgeEntry);
            }
        }

        System.out.println("Iterations: " + iterations);

        if (end != null) {
            var current = end;

            while (current != null) {
                route.add(current.node.point);
                current = current.previous;
            }

            Collections.reverse(route);
        }

        return end;
    }

    /**
     * @param point - point to check distance to feature
     * @param features - set of all features that fulfill user demands
     * @return distance to feature if the point is outside the feature area. 0 if point is within feature area.
     */
    private static double getClosestFeaturePoint(Point point, Set<RFeature> features) {
        var closest = Double.MAX_VALUE;
            for (var feature : features) {
            var closestPoint = feature.closestPoint(point);

            if (closestPoint == null) continue;
            var distance = !feature.contains(point) ? Point.comparableDistance(point, closestPoint) : 0;
            if (distance < closest) {
                closest = distance;
            }
        }

        return closest;
    }

    /**
     * Normal A* search
     * 
     * @param start the start node
     * @param end   the end node
     * @return the path
     */
    private List<Point> runAStar(Point start, Point end)
    {
        var points = new ArrayList<Point>();

        try {
            var osrm = OpenSourceRoutingMachine.getRoute(
                start.getLatitude(), start.getLongitude(),
                end.getLatitude(), end.getLongitude()
            );

            Point previous = null;

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

                            if (previous == null) {
                                previous = new Point(intersection.location[1], intersection.location[0]);
                                continue;
                            }

                            var closestA = graph.findNearestNode(new Point(intersection.location[1], intersection.location[0]));
                            var closestB = graph.findNearestNode(previous);
                            
                            var path = graph.getWay(closestA, closestB);

                            points.addAll(path);

                            previous = new Point(intersection.location[1], intersection.location[0]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            points.add(start);
            points.add(end);
            e.printStackTrace();
        }

        return points;
    }

}
