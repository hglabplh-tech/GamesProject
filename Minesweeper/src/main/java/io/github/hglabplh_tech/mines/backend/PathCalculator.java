/*
Copyright (c) 2025/2026 Harald Glab-Plhak

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

    private final Set<ButtonPoint> path = new HashSet<>();

    private final Set<ButtonPoint> pathTrials = new HashSet<>();



    public PathCalculator(SweeperLogic util, Labyrinth labyrinth) {
        this.theTree = new DecisionTree();
        this.util = util;
        this.labyrinth = labyrinth;
    }

    public List<Set<ButtonPoint>> calculateAllPaths() {
        List<Set<ButtonPoint>> allPaths = new ArrayList<>();
        Iterator<ButtonPoint> iter = this.getLabyrinth().getPointsOrder().iterator();
        ButtonPoint last = iter.next();
        while (iter.hasNext()) {
            ButtonPoint next = iter.next();
            allPaths.add(calculatePath(last, next));
            last = next;
        }
        return allPaths;
    }

    public Set<ButtonPoint> calculatePath(ButtonPoint startPoint, ButtonPoint endPoint) {
        List<ButtonPoint> lastPoints = new ArrayList<>();
        this.path.clear();
        this.pathTrials.clear();

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
                    addToSet(this.path, newElement.getThisPoint(), true);
                }
                lastPoints.add(result.buttonPoint());
                result = this.calculateNextPoint(newElement, endPoint, lastPoints, result.indicator());
                lastPoint = nextPoint;
                nextPoint = result.buttonPoint();
                newElement = this.getTheTree().newTreeElement(this.getTheTree().getRoot(), DecisionTree.TreeElementType.SIBLING, nextPoint);

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
            addToSet(this.path, endPoint, true);
        }

        return this.path;
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
                                                  List<ButtonPoint> lastPoints,
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
        ButtonPoint resultButton = new ButtonPoint(new Point(nextX, nextY), description);
        boolean successAddSet = addToSet(this.pathTrials, resultButton,  false);

        BPointPlusIndicator indResult;
        if (description.isMine() || !successAddSet) {
            indResult = new BPointPlusIndicator(resultButton, Indicator.FOUND_AFTER_ERROR);
        } else {
            indResult = new BPointPlusIndicator(resultButton, Indicator.FOUND_NEXT);
        }
        lastPoints.add(resultButton);
        BPointPlusInternal internResult = new BPointPlusInternal(indResult, lastPoints);

        while(resultButton.buttonDescr().isMine() || !successAddSet) {
            internResult = calculateNextPointIntern(node,
                    internResult,
                    false
                    );
            resultButton = internResult.buttonPlusInd().buttonPoint();
            indResult = new BPointPlusIndicator(resultButton, internResult.buttonPlusInd().indicator());


        }
        return indResult;
    }

    private BPointPlusInternal calculateNextPointIntern(DecisionTree.TreeElement node,
                                                         BPointPlusInternal internalHolder,
                                                         boolean  recur) {

        ButtonPoint startPoint = node.getThisPoint();
        ButtonPoint prevResultButt = internalHolder.buttonPlusInd().buttonPoint();
        Indicator prevResultInd = internalHolder.buttonPlusInd().indicator();
        List<ButtonPoint> lastPoints = internalHolder.lastPoints();
        ButtonPoint lastPoint = lastPoints.get(lastPoints.size() - 1);
        List<List<ButtonDescription>> fieldsList = this.getUtil().getFieldsList();

        System.out.println("There was a mine point \n"+ prevResultButt +"\n");
        int nextX;
        if (recur) {
            nextX = ((startPoint.myPoint().x() + 1) < this.getUtil().getCx()) ? startPoint.myPoint().x() + 1 : startPoint.myPoint().x();
        } else {
            nextX =((startPoint.myPoint().x() - 1) >= 0) ? startPoint.myPoint().x() - 1 : startPoint.myPoint().x();
        }

        int nextY;
        if (recur) {
            nextY =((startPoint.myPoint().y() + 1) < this.getUtil().getCy()) ? startPoint.myPoint().y() + 1 : startPoint.myPoint().y();
        } else {
            nextY =((startPoint.myPoint().y() - 1) >= 0) ? startPoint.myPoint().y() - 1 : startPoint.myPoint().y();
        }
        ButtonDescription description = fieldsList.get(nextY).get(nextX);
        ButtonPoint resultButton = new ButtonPoint(new Point(nextX, nextY), description);
        Indicator ind;
        lastPoints.add(resultButton);
        boolean successAddToSet = addToSet(this.pathTrials, resultButton,  false);
        if (resultButton.buttonDescr().isMine()) {
            ind = Indicator.FOUND_AFTER_ERROR;
        } else {
            ind = Indicator.FOUND_NEXT;
        }
        BPointPlusInternal nextResult = new BPointPlusInternal(new BPointPlusIndicator(resultButton, ind), lastPoints);
        if (!successAddToSet || resultButton.buttonDescr().isMine()) {
            nextResult = calculateNextPointIntern(node, nextResult, true);
        }

        return nextResult;
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

    private record BPointPlusInternal(BPointPlusIndicator buttonPlusInd, List<ButtonPoint> lastPoints) {

    }

    private static boolean addToSet(Set<ButtonPoint> theSet, ButtonPoint element, boolean throwException) {

        if (throwException) {
            theSet.add(element);
            return true;
        } else {
            if (theSet.contains(element)) {
                return false;
            } else {
                theSet.add(element);
                return true;
            }
        }
    }

}
