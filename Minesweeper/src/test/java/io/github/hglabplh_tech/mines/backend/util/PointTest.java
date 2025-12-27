package io.github.hglabplh_tech.mines.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    public void testCheckPointIsNeighborOK() {
        Point middle = new Point(10,10);
        Point down = new Point(10,11);
        Point up = new Point(10,9);
        Point left = new Point(9,10);
        Point right = new Point(11,10);
        Point upLeft = new Point(9,9);
        Point upRight = new Point(11,9);
        Point downLeft = new Point(9, 11);
        Point downRight = new Point(11,11);
        assertTrue(middle.checkPointIsNeighbor(middle), "same-point not ok");

        assertTrue(middle.checkPointIsNeighbor(down), "down not ok");
        assertTrue(down.checkPointIsNeighbor(middle), "down not ok");

        assertTrue(middle.checkPointIsNeighbor(up), "up not ok");
        assertTrue(up.checkPointIsNeighbor(middle), "up not ok");

        assertTrue(middle.checkPointIsNeighbor(left), "left not ok");
        assertTrue(middle.checkPointIsNeighbor(right), "right not ok");
        assertTrue(middle.checkPointIsNeighbor(upLeft), "upLeft not ok");
        assertTrue(middle.checkPointIsNeighbor(upRight), "upRight not ok");
        assertTrue(middle.checkPointIsNeighbor(downLeft), "downLeft not ok");
        assertTrue(middle.checkPointIsNeighbor(downRight), "downRight not ok");
    }

    @Test
    public void testCheckPointIsNeighborNOK() {
        Point middle = new Point(10,10);
        Point down = new Point(8,13);
        Point up = new Point(5,9);
        Point left = new Point(22,10);
        Point right = new Point(11,7);
        Point upLeft = new Point(9,50);
        Point upRight = new Point(11,13);
        Point downLeft = new Point(8, 11);
        Point downRight = new Point(12,11);
        assertFalse(middle.checkPointIsNeighbor(down), "down not ok");
        assertFalse(down.checkPointIsNeighbor(middle), "down not ok");
        assertFalse(middle.checkPointIsNeighbor(up), "up not ok");
        assertFalse(middle.checkPointIsNeighbor(left), "left not ok");
        assertFalse(middle.checkPointIsNeighbor(right), "right not ok");
        assertFalse(middle.checkPointIsNeighbor(upLeft), "upLeft not ok");
        assertFalse(middle.checkPointIsNeighbor(upRight), "upRight not ok");
        assertFalse(middle.checkPointIsNeighbor(downLeft), "downLeft not ok");
        assertFalse(middle.checkPointIsNeighbor(downRight), "downRight not ok");
    }

    @Test
    public void testCheckXCoord() {
        int x1 = 9;
        int x2 = 9;
        int x3 = 10;
        int x4 = 8;
        int x5 = 12;
        Point p = new Point(9, 8);
        assertTrue(p.checkXCoord(x1, x2));
        assertTrue(p.checkXCoord(x1, x3));
        assertTrue(p.checkXCoord(x1, x4));
        assertTrue(p.checkXCoord(x2, x4));

        assertTrue(p.checkXCoord(x2, x1));
        assertTrue(p.checkXCoord(x3, x1));
        assertTrue(p.checkXCoord(x4, x1));
        assertTrue(p.checkXCoord(x4, x1));

        assertFalse(p.checkXCoord(x4, x3));
        assertFalse(p.checkXCoord(x3, x5));

        assertFalse(p.checkXCoord(x3, x4));
        assertFalse(p.checkXCoord(x5, x3));
    }

    @Test
    public void testEquals() {
        Point first = new Point(10,10);
        Point second = new Point(13,13);
        Point third = new Point(13,13);
        Point fourth = new Point(22,8);

        assertNotEquals(first, fourth, "has to be different");
        assertEquals(second, third, "has to be equal");
        assertNotEquals(third,fourth, "has to be different");
    }

    @Test
    void comparePoint() {
    }

    @Test
    void compareNearerToEnd() {
    }
}