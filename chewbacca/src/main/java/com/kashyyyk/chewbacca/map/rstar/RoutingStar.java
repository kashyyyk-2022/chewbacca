package com.kashyyyk.chewbacca.map.rstar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    public float idealDistance;

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
    private PriorityQueue<RNode> priorityQueue;

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
        var result = new ArrayList<Point>();

        var previous = route.get(0);
        // Use OSRM to get the route between the points
        for (var i = 1; i < route.size(); i++) {
            var current = route.get(i);

            result.addAll(runAStar(previous, current));

            previous = current;
        }

        return result;
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

        // Testing

        database.downloadData(start);

        var features = graph.getFeatures("water");

        var nearest = graph.findNearestNode(start);

        priorityQueue = new PriorityQueue<RNode>((a, b) -> {
            var pa = Point.comparableDistance(a.point, start) * distanceToStartBias;
            pa += getClosestFeaturePoint(a.point, features) * terrainBias;

            var pb = Point.comparableDistance(b.point, start) * distanceToStartBias;
            pb += getClosestFeaturePoint(b.point, features) * terrainBias;

            return (int) (pa * 1000 - pb * 1000);
        });

        priorityQueue.add(nearest);

        var maxNodes = 3;

        while (!priorityQueue.isEmpty())
        {
            var current = priorityQueue.poll();

            if (current == null)
            {
                break;
            }

            if (visited.contains(current.id))
            {
                continue;
            }

            visited.add(current.id);

            var connected = graph.getConnected(current);

            if (connected.size() >= 3)
            {
                route.add(current.point);

                start = current.point;
            }

            for (var node : connected)
            {
                priorityQueue.add(node);
            }

            if (route.size() >= maxNodes)
            {
                break;
            }
        }
        
        /*
        var paths = database.queryWays("highway", "track", "path");

        for (var path : paths) {
            var nodes = database.getNodes(path);

            for (var node : nodes) {
                route.add(new Point(node.lat, node.lon));
            }
        } */
    }

    private static double getClosestFeaturePoint(Point point, Set<RFeature> features) {
        var closest = Double.MAX_VALUE;

        for (var feature : features) {
            var closestPoint = feature.closestPoint(point);
            if (closestPoint == null) continue;
            var distance = Point.comparableDistance(point, closestPoint);

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
