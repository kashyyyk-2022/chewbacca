package com.kashyyyk.chewbacca.map.rstar;

/** Class used to store settings for different terrains, by associating the terrains with a set of key-value pairs */
public class TerrainsKeyValue {

    /*Setup of key-value pairs that are suitable for terrain urban*/
    private static final String[][] urb = new String[][]{
            {"landuse","residential"},
            {"landuse","retail"},
            {"landuse","institutional"},
            {"landuse","commercial"}
    };
    private static final KeyValue urban = new KeyValue(urb);

    /*Setup of key-value pairs that are suitable for terrain nature*/
    private static final String[][] nat ={
            {"landuse","forest"},
            {"leisure","park"},
            {"natural","wetland"},
            {"natural","scrub"},
            {"natural","wood"}
    };
    private static final KeyValue nature = new KeyValue(nat);

    /*Setup of key-value pairs that are suitable for terrain nature*/
    private static final String[][] wat ={
            {"natural","water"},
            {"natural","shoreline"}
    };
    private static final KeyValue water = new KeyValue(wat);

    /** Method to get a KeyVal structure containing keys and values that is worth checking inside osm-tags for a specific terrain
     * @param terrain - string with one of the preferences: "water","nature" or "urban".
     * @return - a KeyValue object, with the keys and values that are relevant for the terrain preferred by the user.
     */
    public static KeyValue getTerrainKV(String terrain){
        KeyValue result = switch (terrain) {
            case "water" -> water;
            case "nature" -> nature;
            default -> urban;
        };

        return result;
    }



}
