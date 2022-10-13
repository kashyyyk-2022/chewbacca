package com.kashyyyk.chewbacca.map.rstar;

/**
 * An entry in the priority queue for the R* algorithm.
 */
public class REntry {
    
    /**
     * The node that this entry represents.
     */
    public RNode node;

    /**
     * Cost to get to this node.
     */
    public double cost;

    /**
     * Last edge used to get to this node.
     */
    public RNode lastEdge;

    /**
     * Cost to get to this node from the last edge.
     */
    public double lastEdgeCost;

    /**
     * Back pointer to the previous entry.
     */
    public REntry previous;

    /**
     * Distance from the start to this node.
     */
    public double distance;

    /**
     * Priority of this entry.
     */
    public double priority;

    /**
     * Create a new entry.
     * 
     * @param node                      The node
     * @param cost                      The cost
     * @param lastEdge                  The last edge
     * @param lastEdgeCost              The cost to get to this node from the last edge
     * @param previous                  The previous entry
     */
    public REntry(RNode node, double cost, RNode lastEdge, double lastEdgeCost, REntry previous) {
        this.node = node;
        this.cost = cost;
        this.lastEdge = lastEdge;
        this.lastEdgeCost = lastEdgeCost;
        this.previous = previous;
    }
}
