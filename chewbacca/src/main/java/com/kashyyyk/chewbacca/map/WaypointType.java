package com.kashyyyk.chewbacca.map;

import java.awt.Color;
import java.io.IOException;

/**
 * The type of a waypoint.
 * 
 * TODO: Fill this out
 */
public enum WaypointType {
    Path(0b00, new Color(127, 127, 127)),
    Water(0b01, new Color(0, 166, 255), new Color(158, 219, 239)),
    Road(0b10),
    Building(0b11);

    private int value;
    private Color[] color;

    private WaypointType(int value, Color... color) {
        this.value = value;
        this.color = color;
    }

    public boolean ContainsColor(Color color) {
        for (Color c : this.color) {
            if (c.equals(color)) {
                return true;
            }
        }
        return false;
    }

    public void serialize(java.io.DataOutputStream stream) throws IOException {
        stream.writeByte(value);
    }

    public static WaypointType deserialize(java.io.DataInputStream stream) throws IOException {
        var value = stream.readByte();
        for (var type : WaypointType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IOException("Invalid waypoint type");
    }
}
