package com.kashyyyk.chewbacca.map;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "osm")
public class Osm {
    @JsonProperty("bounds")
    public Bounds bounds;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "node")    
    @JsonProperty("node")
    public Node[] node;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "way")
    @JsonProperty("way")
    public Way[] way;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bounds {
        @JsonProperty("minlat")
        public double minlat;

        @JsonProperty("minlon")
        public double minlon;

        @JsonProperty("maxlat")
        public double maxlat;

        @JsonProperty("maxlon")
        public double maxlon;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Node {
        @JsonProperty("id")
        public long id;

        @JsonProperty("lat")
        public double lat;

        @JsonProperty("lon")
        public double lon;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "tag")
        @JsonProperty("tag")
        public Tag[] tag;  //Object (park, bench, trash bin)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Way {
        @JsonProperty("id")
        public long id;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "nd")
        @JsonProperty("nd")
        public Nd[] nd; //List of IDS

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "tag")
        @JsonProperty("tag")
        public Tag[] tag; //Meta data for
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Nd {
        @JsonProperty("ref")
        public long ref;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tag {
        @JsonProperty("k")
        public String k;

        @JsonProperty("v")
        public String v;
    }
    
}
