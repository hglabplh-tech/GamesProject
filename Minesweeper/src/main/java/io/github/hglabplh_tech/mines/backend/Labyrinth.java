/*
Copyright (c) 2025 Harald Glab-Plhak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import io.github.hglabplh_tech.mines.backend.util.Point;
import io.github.hglabplh_tech.mines.gui.GUILogics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Labyrinth  {
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

    public boolean isPositiveEnd() {
        boolean ok = false;
        if (checkCorrectPath()) {
            return this.pathToNext.contains(this.endPoint)
                    && this.pathToNext.contains(this.firstBase)
                    && this.pathToNext.contains(this.secondBase);
        } else {
            return false;
        }
    }

    public boolean checkStepOrder() {
        if (checkCorrectPath()) {
            // the order of the if statements here is essential do not change order
            if (this.pathToNext.contains(this.endPoint)) {
                return this.pathToNext.contains(this.startPoint)
                        && this.pathToNext.contains(this.firstBase)
                        && this.pathToNext.contains(this.secondBase);
            }
            if (this.pathToNext.contains(this.secondBase)) {
                return this.pathToNext.contains(this.startPoint)
                        && this.pathToNext.contains(this.firstBase);
            }
            if (this.pathToNext.contains(this.firstBase)) {
                return this.pathToNext.contains(this.startPoint);
            }
            return true;
        } else {
            return false;
        }
    }

    public Integer countBasesReached() {
        Integer result = 0;
        if (checkCorrectPath()) {

            if (this.pathToNext.contains(this.firstBase)) {
                result++;
            }
            if (this.pathToNext.contains(this.secondBase)) {
                result++;
            }
            if (this.pathToNext.contains(this.endPoint)) {
                result++;
            }
        }
        return result;
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
