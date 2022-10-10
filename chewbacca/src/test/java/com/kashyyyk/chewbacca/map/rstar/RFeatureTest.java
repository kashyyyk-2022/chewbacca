package com.kashyyyk.chewbacca.map.rstar;

import com.kashyyyk.chewbacca.map.Point;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class RFeatureTest {

    @Test
    void contains() {
        Point[] points={new Point(0,0),new Point(5,0),new Point(5,5), new Point(0,5)};
        String[] tags = {"water","nature"};
        RFeature rf = new RFeature(1,points,tags);

        Point p1=new Point(2,2);
        Point p2=new Point(1,3);
        Point p3=new Point(1,6);

        assertEquals(true,rf.contains(p1));
        assertEquals(true,rf.contains(p2));
        assertEquals(false,rf.contains(p3));
        System.out.println("Simple rectangle passed");

        Point[] points2={new Point(1,1),new Point(0,2),new Point(2,3), new Point(2,4),
                new Point(3,5),new Point(5,4),new Point(4,3),new Point(4,2)};
        String[] tags2 = {"water","nature"};
        RFeature rf2 = new RFeature(2,points2,tags2);

        Point p4 = new Point(0,0);
        Point p5 = new Point(1,2);
        Point p6 = new Point(3,3);
        Point p7 = new Point(1,3);

        assertEquals(false,rf2.contains(p4));
        assertEquals(true,rf2.contains(p5));
        assertEquals(true,rf2.contains(p6));
        assertEquals(false,rf2.contains(p7));

        System.out.println("Advanced polygon passed");
    }
}