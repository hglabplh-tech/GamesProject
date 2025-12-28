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

    public List<ButtonPoint> calculatePath(ButtonPoint start, ButtonPoint end) {
        List<ButtonPoint> path = new ArrayList<>();
        List<List<ButtonDescription>> field = this.getUtil().getFieldsList();
        ButtonDescription descriptionStart = field.get(start.myPoint().y()).get(start.myPoint().x());
        ButtonDescription descriptionEnd = field.get(end.myPoint().y()).get(end.myPoint().x());
        ButtonPoint startPoint = new ButtonPoint(start.myPoint(), descriptionStart);
        ButtonPoint endPoint = new ButtonPoint(end.myPoint(), descriptionEnd);
        if (startPoint.equals(start) && endPoint.equals(end)) {
            path.add(startPoint);
            this.getTheTree().initRoot(startPoint);
            DecisionTree.TreeElement leftElement =
                    DecisionTreeUtils.insertElementLeftRight(this.getTheTree(),
                            this.theTree.getRoot(),
                            DecisionTree.TreeElementType.LEFT,
                            startPoint);
            Conditions conditions = makeConditions(startPoint, endPoint, leftElement);
            if (leftElement != null) {
                leftElement = leftElement.addSuccessor(conditions.nextCond(), conditions.endCond(), leftElement.getThisPoint());
                Conditions nextCond;
                ButtonPoint nextPoint = calculateNextPoint(leftElement, endPoint);
                DecisionTree.TreeElement newElement = DecisionTreeUtils
                        .insertElementLeftRight(this.getTheTree(), leftElement,
                                DecisionTree.TreeElementType.RIGHT, nextPoint);
                if (newElement != null) {
                    boolean toggler = true;
                    while (!Objects.requireNonNull(newElement).successIndicator().finished()) {
                        nextCond = makeConditions(startPoint, endPoint, newElement);
                        if (toggler) {
                            newElement = newElement.addSuccessor(nextCond.nextCond(), nextCond.endCond(), nextPoint);
                        } else {
                            toggler = true;
                            newElement = newElement.addSuccessor(
                                    (e -> !e.buttonDescr().pointType().equals(SweepPointType.MINEPOINT)),
                                    nextCond.endCond(), nextPoint);
                        }
                        if (newElement.successIndicator().success()) {
                            path.add(newElement.getThisPoint());
                            DecisionTree.TreeElement parent = newElement.getParent();
                            nextPoint = calculateNextPoint(newElement, endPoint);
                            AtomicReference<DecisionTree.TreeElement> nextElement = new AtomicReference<>(DecisionTreeUtils
                                    .insertSibling(this.getTheTree(), parent, newElement, nextPoint));
                            newElement = nextElement.get();
                        } else {
                            toggler = false;
                            nextPoint = calculateNextPossiblePoint(newElement, nextPoint);
                        }
                    }
                }
            }
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

    public Conditions makeConditions(ButtonPoint startPoint, ButtonPoint endPoint,
                                     DecisionTree.TreeElement actualNode) {
        ButtonPoint pointNext = calculateNextPoint(actualNode, endPoint);
        return new Conditions((e ->
                (!e.buttonDescr().isMine()
                        && (!(e.myPoint()
                        .compareNearerToEnd(pointNext.myPoint(),
                                startPoint.myPoint())
                        .bothNearer())
                        && (e.myPoint()
                        .compareNearerToEnd(pointNext.myPoint(),
                                endPoint.myPoint())
                        .bothNearer())))
        ),
                (e -> e.equals(endPoint)));
    }

    public ButtonPoint calculateNextPoint(DecisionTree.TreeElement node, ButtonPoint endPoint) {
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
        return new ButtonPoint(new Point(nextX, nextY), description);
    }

    public ButtonPoint calculateNextPossiblePoint(DecisionTree.TreeElement node,
                                                   ButtonPoint wrongPoint) {
        ButtonPoint startPoint = node.getThisPoint();
        int nextX = startPoint.myPoint().x() + 1;
        int nextY = startPoint.myPoint().y() + 1;


        int index = 0;

        if (Objects.equals(wrongPoint, new Point(nextX, nextY))) {
            if (index == 0) {
                nextX = startPoint.myPoint().x() - 1;
                nextY = wrongPoint.myPoint().y() - 1;
            } else if (index == 1) {
                nextX = startPoint.myPoint().x() + 1;
                nextY = wrongPoint.myPoint().y() - 1;
            } else if (index == 2) {
                nextX = startPoint.myPoint().x() - 1;
                nextY = wrongPoint.myPoint().y() + 1;
            }
        }
        ButtonDescription description = this.getUtil().getFieldsList().get(nextY).get(nextX);
        return new ButtonPoint(new Point(nextX, nextY), description);
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
}
