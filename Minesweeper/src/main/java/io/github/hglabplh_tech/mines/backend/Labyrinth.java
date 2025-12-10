package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.util.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Labyrinth {
    private final Point startPoint;
    private final Point firstBase;
    private final Point secondBase;
    private final Point endPoint;
    private final java.util.List<Point> path = new ArrayList<>();
    private final List<Point> pointsOrder;
    private List<Point> pathToNext = new ArrayList<>();
    private int nextIndex = 1;
    private Point nextPoint;

    public Labyrinth(Point startPoint, Point firstBase, Point secondBase, Point endPoint) {
        this.startPoint = startPoint;
        this.firstBase = firstBase;
        this.secondBase = secondBase;
        this.endPoint = endPoint;
        this.pointsOrder = Arrays.asList(startPoint, firstBase, secondBase, endPoint);
        this.nextPoint = pointsOrder.get(this.nextIndex);
    }

    public void addXYToPath(Integer x, Integer y) {
        this.addToPath(new Point(x, y));
    }

    public void addToPath(Point point) {
        this.pathToNext.add(point);
    }

    public Point calculateNextPoint() {
        this.nextPoint = this.pointsOrder.get(++this.nextIndex);
        return this.getNextPoint();
    }

    public boolean checkCorrectPath() {
        boolean success = true;
        Iterator<Point> iterator = this.pathToNext.iterator();
        if (iterator.hasNext()) {
            iterator.next();
        }
        int index = 0;
        while (iterator.hasNext() && success) {
            Point actualPoint = this.pathToNext.get(index);
            Point nextPoint = iterator.next();
            index++;
            success = actualPoint.checkPointIsNeighbor(nextPoint);
        }
        return success;
    }


    public Point getNextPoint() {
        return this.nextPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public List<Point> getPath() {
        return path;
    }

    public List<Point> getPointsOrder() {
        return pointsOrder;
    }

    public List<Point> getPathToNext() {
        return pathToNext;
    }

    public int getNextIndex() {
        return nextIndex;
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
