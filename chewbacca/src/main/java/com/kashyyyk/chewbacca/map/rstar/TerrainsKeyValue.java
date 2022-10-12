package com.kashyyyk.chewbacca.map.rstar;

public class TerrainsKeyValue {

    /*Setup of key-value pairs that are suitable for terrain urban*/
    private static final String[][] urb = new String[][]{
            {"landuse","residental"},
            {"landuse","retail"},
            {"landuse","institutional"}
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

    public static KeyValue getTerrainKV(String terrain){
        KeyValue result = switch (terrain) {
            case "water" -> water;
            case "nature" -> nature;
            default -> urban;
        };

        return result;
    }



}
