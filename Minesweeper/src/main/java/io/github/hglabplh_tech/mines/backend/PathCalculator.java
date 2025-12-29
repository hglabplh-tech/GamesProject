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

import io.github.hglabplh_tech.mines.backend.util.DecisionTreeUtils;
import io.github.hglabplh_tech.mines.backend.util.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class PathCalculator {

    private final SweeperLogic util;
    private final Labyrinth labyrinth;

    private final DecisionTree theTree;

    public PathCalculator(SweeperLogic util, Labyrinth labyrinth) {
        this.theTree = new DecisionTree();
        this.util = util;
        this.labyrinth = labyrinth;
    }

    public List<List<ButtonPoint>> calculateAllPaths() {
        List<List<ButtonPoint>> allPaths = new ArrayList<>();
        Iterator<ButtonPoint> iter = this.getLabyrinth().getPointsOrder().iterator();
        ButtonPoint last = iter.next();
        while (iter.hasNext()) {
            ButtonPoint next = iter.next();
            allPaths.add(calculatePath(last, next));
            last = next;
        }
        return allPaths;
    }

    public List<ButtonPoint> calculatePath(ButtonPoint startPoint, ButtonPoint endPoint) {
        List<ButtonPoint> path = new ArrayList<>();
        this.getTheTree().initRoot(startPoint);
        DecisionTree.TreeElement newElement =
                DecisionTreeUtils.insertElementLeftRight(this.getTheTree(),
                        this.theTree.getRoot(),
                        DecisionTree.TreeElementType.LEFT,
                        startPoint);


        if (newElement != null) {
            ButtonPoint nextPoint = startPoint;
            ButtonPoint lastPoint;
            Conditions conditions = makeConditions(endPoint, startPoint);
            newElement.addSuccessor(conditions.nextCond(), conditions.endCond(), startPoint);
            BPointPlusIndicator result = new BPointPlusIndicator(startPoint, Indicator.FOUND_NEXT);
            DecisionTree.SuccessIndicator indicator = newElement.successIndicator();
            while (!indicator.finished()) {
                if (indicator.success()) {
                    path.add(newElement.getThisPoint());
                }
                newElement = this.getTheTree().newTreeElement(this.getTheTree().getRoot(), DecisionTree.TreeElementType.SIBLING, nextPoint);
                result = this.calculateNextPoint(newElement, endPoint, result.buttonPoint() ,result.indicator());
                lastPoint = nextPoint;
                nextPoint = result.buttonPoint();
                conditions = this.makeConditions(endPoint, nextPoint);
                if (result.indicator().equals(Indicator.FOUND_NEXT)) {
                    newElement = newElement.addSuccessor(conditions.nextCond(), conditions.endCond(), newElement.getThisPoint());
                } else {
                    ButtonPoint finalLastPoint = lastPoint;
                    newElement = newElement.addSuccessor((e -> !e.buttonDescr().isMine()
                            && e.myPoint().checkPointIsNeighbor(finalLastPoint.myPoint())), conditions.endCond(), newElement.getThisPoint());
                }
                indicator = newElement.successIndicator();
            }
            path.add(endPoint);


        }

        return path;
    }


    public DecisionTree getTheTree() {
        return this.theTree;
    }

    public SweeperLogic getUtil() {
        return this.util;
    }

    public Labyrinth getLabyrinth() {
        return this.labyrinth;
    }

    public Conditions makeConditions(ButtonPoint endPoint,
                                     ButtonPoint pointNext) {
        return new Conditions((e ->
                (!e.buttonDescr().isMine()
                        && (e.myPoint()
                        .compareNearerToEnd(pointNext.myPoint(),
                                endPoint.myPoint())
                        .xOtherNearer())
                        || (e.myPoint()
                        .compareNearerToEnd(pointNext.myPoint(),
                                endPoint.myPoint())
                        .yOtherNearer())

                )
        ),
                (e -> e.buttonDescr().pointType().equals(endPoint.buttonDescr().pointType())
                        && e.equals(endPoint)));
    }

    // TODO: if there was a mine-point the last point must be excluded in the next run change switch to if with swapping - / + for the point calculated
    public BPointPlusIndicator calculateNextPoint(DecisionTree.TreeElement node,
                                                  ButtonPoint endPoint,
                                                  ButtonPoint lastPoint,
                                                  Indicator lastIndicator
                                                  ) {
        int nextX = 0;
        int nextY = 0;
        ButtonPoint startPoint = node.getThisPoint();

        int stepsX = endPoint.myPoint().x() - startPoint.myPoint().x();
        int stepsY = endPoint.myPoint().y() - startPoint.myPoint().y();
        if (stepsX <= 0) {
            nextX = (stepsX < 0) ? startPoint.myPoint().x() - 1 : startPoint.myPoint().x();
        }
        if (stepsX > 0) {
            nextX = startPoint.myPoint().x() + 1;
        }
        if (stepsY <= 0) {
            nextY = (stepsY < 0) ? startPoint.myPoint().y() - 1 : startPoint.myPoint().y();
        }
        if (stepsY > 0) {
            nextY = startPoint.myPoint().y() + 1;
        }
        ButtonDescription description = this.getUtil().getFieldsList().get(nextY).get(nextX);
        if (description.isMine() || lastIndicator.equals(Indicator.FOUND_AFTER_ERROR)) {
            for (int index = 0; index <= 7; index++) {
                Point point = switch (index) {
                    case 0 -> new Point(startPoint.myPoint().x() + 1, startPoint.myPoint().y());
                    case 1 -> new Point(startPoint.myPoint().x(), startPoint.myPoint().y() + 1);
                    case 2 -> new Point(startPoint.myPoint().x() - 1, startPoint.myPoint().y());
                    case 3 -> new Point(startPoint.myPoint().x(), startPoint.myPoint().y() - 1);
                    case 4 -> new Point(startPoint.myPoint().x() + 1, startPoint.myPoint().y() + 1);
                    case 5 -> new Point(startPoint.myPoint().x() - 1, startPoint.myPoint().y() + 1);
                    case 6 -> new Point(startPoint.myPoint().x() + 1, startPoint.myPoint().y() - 1);
                    default -> new Point(startPoint.myPoint().x() - 1, startPoint.myPoint().y() - 1);
                };
                if (point.x().equals(this.util.getCx())) {
                    point =  new Point(point.x() - 1, point.y());
                }
                if (point.y().equals(this.getUtil().getCy())) {
                    point = new Point(point.x(), point.y() -1);
                }
                description = this.getUtil().getFieldsList().get(point.y()).get(point.x());
                if (!description.isMine()) {
                    return new BPointPlusIndicator(new ButtonPoint(new Point(point.x(), point.y()), description), Indicator.FOUND_AFTER_ERROR);
                }
            }
        }
        return new BPointPlusIndicator(new ButtonPoint(new Point(nextX, nextY), description), Indicator.FOUND_NEXT);
    }

    public static class Conditions {
        private final Predicate<ButtonPoint> nextCond;

        private final Predicate<ButtonPoint> endCond;

        public Conditions(Predicate<ButtonPoint> nextCond, Predicate<ButtonPoint> endCond) {
            this.nextCond = nextCond;
            this.endCond = endCond;
        }

        public Predicate<ButtonPoint> nextCond() {
            return nextCond;
        }

        public Predicate<ButtonPoint> endCond() {
            return endCond;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Conditions that = (Conditions) o;
            return Objects.equals(nextCond, that.nextCond) && Objects.equals(endCond, that.endCond);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nextCond, endCond);
        }
    }

    public enum Indicator {
        FOUND_NEXT,
        FOUND_AFTER_ERROR;
    }

    public record BPointPlusIndicator(ButtonPoint buttonPoint, Indicator indicator) {

    }

}
