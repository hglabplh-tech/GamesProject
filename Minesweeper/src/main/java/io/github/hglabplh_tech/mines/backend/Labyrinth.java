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

import io.github.hglabplh_tech.games.backend.logexcp.GameLogger;
import io.github.hglabplh_tech.games.backend.logexcp.LoggingID;
import io.github.hglabplh_tech.games.backend.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Labyrinth  {
    private static GameLogger logger = GameLogger.logInstance();

    private final ButtonPoint startPoint;
    private final ButtonPoint firstBase;
    private final ButtonPoint secondBase;
    private final ButtonPoint endPoint;
    private final List<ButtonPoint> path = new ArrayList<>();
    private final List<ButtonPoint> pointsOrder;
    private List<ButtonPoint> pathToNext = new ArrayList<>();
    private int nextIndex = 1;
    private ButtonPoint nextPoint;
    private ButtonPoint actPoint;

    public Labyrinth(ButtonPoint startPoint, ButtonPoint firstBase, ButtonPoint secondBase, ButtonPoint endPoint) {
        this.startPoint = startPoint;
        this.firstBase = firstBase;
        this.secondBase = secondBase;
        this.endPoint = endPoint;
        this.pointsOrder = Arrays.asList(startPoint, firstBase, secondBase, endPoint);
        this.nextPoint = pointsOrder.get(this.nextIndex);
        this.actPoint = pointsOrder.get(0);
        logger.logDebug(LoggingID.MINELOG_ID_00004, startPoint, firstBase, secondBase, endPoint);
    }

    public void addXYToPath(Integer x, Integer y, ButtonStatus description) {
        this.addToPath(new ButtonPoint(new Point(x, y), description));
    }

    public void addToPath(ButtonPoint point) {
        this.pathToNext.add(point);
    }

    public void calculateNextPoint() {
        this.nextPoint = this.pointsOrder.get(++this.nextIndex);
    }

    public ButtonPoint switchToNextBase(Integer x, Integer y) {
        Point actPoint= new Point(x, y);
        if (this.nextPoint.equalsInPoint(actPoint)) {
            this.actPoint = this.nextPoint;
            calculateNextPoint();
        }
        return this.nextPoint;
    }

    public boolean checkCorrectPath() {
        return reallyCheckCorrectPath(this.pathToNext);
    }

    public static boolean reallyCheckCorrectPath(List<ButtonPoint> pathToCheck) {
        boolean success = true;
        Iterator<ButtonPoint> iterator = pathToCheck.iterator();
        if (iterator.hasNext()) {
            iterator.next();
        }
        int index = 0;
        while (iterator.hasNext() && success) {
            ButtonPoint actualPoint = pathToCheck.get(index);
            ButtonPoint nextPoint = iterator.next();
            index++;
            if (actualPoint.myPoint().checkPointIsNeighbor(nextPoint.myPoint())) {

                success = true;
            } else {
                logger.logDebug(LoggingID.MINELOG_ID_00010,
                        actualPoint.myPoint(), nextPoint.myPoint());
                success = false;
            }
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

    public ButtonPoint getNextPoint() {
        return this.nextPoint;
    }

    public ButtonPoint getActPoint () {
        return this.actPoint;
    }

    public List<ButtonPoint> getPath() {
        return path;
    }

    public List<ButtonPoint> getPointsOrder() {
        return pointsOrder;
    }

    public List<ButtonPoint> getPathToNext() {
        return pathToNext;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public ButtonPoint getStart() {
        return startPoint;
    }

    public ButtonPoint getFirstBase() {
        return firstBase;
    }

    public ButtonPoint getSecondBase() {
        return secondBase;
    }

    public ButtonPoint getEnd() {
        return endPoint;
    }


}
