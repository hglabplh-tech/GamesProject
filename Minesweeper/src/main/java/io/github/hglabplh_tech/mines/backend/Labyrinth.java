package io.github.hglabplh_tech.mines.backend;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Labyrinth {
    private final Point startPoint;
    private final Point firstBase;
    private final Point secondBase;
    private final Point endPoint;
    private final java.util.List<Point> path = new ArrayList<>();
    private final List<Point> pointsOrder;


    private Point nextPoint;

    public Labyrinth(Point startPoint, Point firstBase, Point secondBase, Point endPoint) {
        this.startPoint = startPoint;
        this.firstBase = firstBase;
        this.secondBase = secondBase;
        this.endPoint = endPoint;
        this.pointsOrder = Arrays.asList(startPoint, firstBase, secondBase, endPoint);
        this.nextPoint = firstBase;
    }


    public Point getStart() {
        return startPoint;
    }

    public Point getFirstBase() {
        return firstBase;
    }

    public Point getSecondBase() {
        return secondBase;
    }

    public Point getEnd() {
        return endPoint;
    }
}
