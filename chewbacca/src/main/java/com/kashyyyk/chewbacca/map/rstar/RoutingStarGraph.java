package com.kashyyyk.chewbacca.map.rstar;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.kashyyyk.chewbacca.map.OsmDatabase;
import com.kashyyyk.chewbacca.map.OsmGraph;
import com.kashyyyk.chewbacca.map.Point;
import com.kashyyyk.chewbacca.map.Osm.*;

public class RoutingStarGraph implements OsmGraph {
    
    private OsmDatabase database;

    private final HashMap<Long, RNode> nodes = new HashMap<Long, RNode>();

    private final HashMap<Long, RFeature> features = new HashMap<Long, RFeature>();

    private final HashMap<Long, Set<RNode>> connections = new HashMap<Long, Set<RNode>>();

    private final HashMap<Long, Set<Long>> ways = new HashMap<Long, Set<Long>>();

    private final HashMap<Long, List<RNode>> wayNodes = new HashMap<Long, List<RNode>>();

    private void connect(RNode a, RNode b) {
        if (!connections.containsKey(a.id)) {
            connections.put(a.id, new HashSet<RNode>());
        }
        connections.get(a.id).add(b);

        if (!connections.containsKey(b.id)) {
            connections.put(b.id, new HashSet<RNode>());
        }
        connections.get(b.id).add(a);
    }

    /**
     * Get connected nodes
     * 
     * @param node                      The node
     */
    public Set<RNode> getConnected(RNode node) {
        return connections.get(node.id);
    }

    /**
     * Get all features that have one of the tags associated with desired terrain
     * @param terrains- A map containing all relevant key-values, where the values are used to determine if features match preference
     * @return a set of all the features that match desired terrain
     */
    public Set<RFeature> getFeatures(KeyValue terrains) {
        HashSet<RFeature> result = new HashSet<RFeature>();

        for (RFeature feature : features.values()) {
            for (int i = 0; i < feature.tags.length; i++) {
                if(terrains.containsValue(feature.tags[i])) {
                    result.add(feature);
                }
            }
        }

        return result;
    }

    @Override
    public void processNode(Node node) {
    }

    @Override
    public void processWay(Way way, boolean accesibility) {
        var nodes = database.getNodes(way);

        var highway = database.getTagValue(way.tag, "highway");

        if (highway == null || highway.equals("motorway") || highway.equals("primary") || highway.equals("trunk") || (accesibility && highway.equals("steps"))) return;

        var access = database.getTagValue(way.tag, "access");

        if (access != null && access.equals("private")) return;

        RNode previous = null;
        for (Node node : nodes) {
            RNode rnode;

            if (!this.nodes.containsKey(node.id)) {
                rnode = new RNode(node.id, new Point(node.lat, node.lon));
                this.nodes.put(node.id, rnode);
            } else {
                rnode = this.nodes.get(node.id);
            }

            rnode.surface = highway;

            if (!ways.containsKey(node.id)) {
                ways.put(node.id, new HashSet<Long>());
            }

            ways.get(node.id).add(way.id);

            if (!wayNodes.containsKey(way.id)) {
                wayNodes.put(way.id, new ArrayList<RNode>(nodes.length));
            }

            wayNodes.get(way.id).add(rnode);

            if (previous != null) {
                connect(previous, rnode);
            }

            previous = rnode;
        }
    }


    @Override
    public void processRelation(Relation relation) {
        if (features.containsKey(relation.id)) return;

        var nodes = database.getNodes(relation);

        var type = database.getTagValues(relation.tag);

        if (type == null) return;

        var points = new Point[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            points[i] = new Point(nodes[i].lat, nodes[i].lon);
        }

        RFeature feature = new RFeature(relation.id, points, type.values().toArray(new String[0]));

        features.put(relation.id, feature);
    }

    @Override
    public void setDatabase(OsmDatabase database) {
        this.database = database;
    }

    /**
     * Get the way which connects two nodes
     * 
     * @param a                         The first node
     * @param b                         The second node
     */
    public List<Point> getWay(RNode a, RNode b) {
        var result = new ArrayList<Point>();
        
        for (var way : ways.get(a.id)) {
            if (ways.get(b.id).contains(way)) {
                for (var node : wayNodes.get(way)) {
                    result.add(node.point);
                }

                break;
            }
        }

        return result;
    }

    /**
     * Find the nearest node to a point
     * 
     * @param point                     The point to find the nearest node to
     */
    public RNode findNearestNode(Point point) {
        RNode nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (RNode node : nodes.values()) {
            double distance = Point.comparableDistance(node.point, point);

            if (distance < nearestDistance) {
                nearest = node;
                nearestDistance = distance;
            }
        }

        return nearest;
    }

    @Override
    public void processElevations(Map<Long, Double> elevations) {
        for (var node : nodes.values()) {
            if (elevations.containsKey(node.id)) {
                node.elevation = elevations.get(node.id);
            }
        }
    }
    
}
