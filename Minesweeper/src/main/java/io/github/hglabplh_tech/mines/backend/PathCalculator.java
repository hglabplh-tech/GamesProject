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

import io.github.hglabplh_tech.mines.backend.util.Point;

import java.util.*;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;

public class PathCalculator {

    private static final IntBinaryOperator plus = Integer::sum;

    private static final IntBinaryOperator minus = (a, b) -> a - b;

    private static final IntBinaryOperator mul = (a, b) -> a * b;

    private static final IntBinaryOperator div = (a, b) -> a / b;

    private static final List<FunTuple> combinators = new ArrayList<>();


    private final SweeperLogic util;
    private final Labyrinth labyrinth;

    private final List<ButtonPoint> pathTrials = new ArrayList<>();


    static {
        combinators.add(new FunTuple(plus, minus));
        combinators.add(new FunTuple(minus, plus));
        combinators.add(new FunTuple(plus, plus));
        combinators.add(new FunTuple(minus, minus));
        combinators.add(new FunTuple(minus, mul));
        combinators.add(new FunTuple(mul, minus));
        combinators.add(new FunTuple(plus, mul));
        combinators.add(new FunTuple(mul, plus));
    }


    public PathCalculator(SweeperLogic util, Labyrinth labyrinth) {
        this.util = util;
        this.labyrinth = labyrinth;
    }

    public List<List<ButtonPoint>> calculateAllPaths() {
        List<List<ButtonPoint>> allPaths = new ArrayList<>();
        List<ButtonPoint> pointsOrder = this.getLabyrinth().getPointsOrder();
        ButtonPoint first = pointsOrder.get(0);
        int index = 1;
        ButtonPoint next;
        while (index < pointsOrder.size()) {
            next = pointsOrder.get(index);
            allPaths.add(calculatePath(first, next));
            index++;
            first = next;
        }
        return allPaths;
    }

    public List<ButtonPoint> calculatePath(ButtonPoint startPoint, ButtonPoint endPoint) {
        this.pathTrials.clear();
        List<ButtonPoint> path = new ArrayList<>();
        ButtonPoint nextPoint = startPoint;
        BPointPlusIndicator result = new BPointPlusIndicator(startPoint, Indicator.FOUND_NEXT,
               new SuccessIndicator(true, false, SuccessIndicator.SUCCESSFUL));
        boolean finish = false;
        boolean success = true;
        while (!finish) {
            if (success) {
                addToSet(path, result.buttonPoint(), true);

            }
            result = this.calculateAndSetNextPoint(nextPoint, endPoint);
            nextPoint = result.buttonPoint();
            finish = result.successIndicator().finished();
            success = result.successIndicator().success() &&
                    !this.pathTrials.contains(result.buttonPoint());
        }

        System.out.println("==== Print out path trials ====");
        this.pathTrials.forEach(e -> System.out.println("entry: ->" + e));
        List<ButtonPoint> resultPath = new ArrayList<>(path);
        resultPath.add(endPoint);
        return resultPath;
    }


    public SweeperLogic getUtil() {
        return this.util;
    }

    public Labyrinth getLabyrinth() {
        return this.labyrinth;
    }

    // TODO: if there was a mine-point the last point must be excluded in the next run change switch to if with swapping - / + for the point calculated
    public BPointPlusIndicator calculateAndSetNextPoint(ButtonPoint startPoint,
                                                        ButtonPoint endPoint) {
        int nextX = 0;
        int nextY = 0;
        System.out.println("The next point \n" + startPoint + "\n");
        addToSet(this.pathTrials, startPoint, false);
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
        ButtonPoint lastButtonPoint = startPoint;
        ButtonPoint resultButton = new ButtonPoint(new Point(nextX, nextY), description);

        Conditions conditions = Conditions.instance(endPoint, startPoint, Indicator.FOUND_NEXT);
        SuccessIndicator indicator = conditions.testConditions(resultButton);
        BPointPlusIndicator result = new BPointPlusIndicator(resultButton, Indicator.FOUND_NEXT, indicator);

        if (!indicator.success() && !this.pathTrials.contains(resultButton)) {
            for (FunTuple tuple : combinators) {
                addToSet(this.pathTrials, resultButton, false);
                result = new BPointPlusIndicator(calculateNextPointIntern(lastButtonPoint, tuple.first(), tuple.second()),
                        Indicator.FOUND_AFTER_ERROR, indicator);
                resultButton = result.buttonPoint();
                conditions = Conditions.instance(endPoint, lastButtonPoint, Indicator.FOUND_AFTER_ERROR);
                indicator = conditions.testConditions(resultButton);
                if (indicator.success() && !this.pathTrials.contains(resultButton)) {
                    result = new BPointPlusIndicator(resultButton,
                            Indicator.FOUND_AFTER_ERROR, indicator);
                    addToSet(this.pathTrials, resultButton, false);
                    break;
                } else {
                    lastButtonPoint = resultButton;
                }
            }
        }
        return result;
    }

    private ButtonPoint calculateNextPointIntern(ButtonPoint startPoint,
                                                 IntBinaryOperator operatorX,
                                                 IntBinaryOperator operatorY) {

        List<List<ButtonDescription>> fieldsList = this.getUtil().getFieldsList();
        System.out.println("The next point \n" + startPoint + "\n");
        Point nextPoint = calcFun(startPoint.myPoint().x(),
                startPoint.myPoint().y(),
                this.util.getCx(), this.util.getCy(),
                operatorX, operatorY);
        ButtonDescription description = fieldsList.get(nextPoint.y()).get(nextPoint.x());
        ButtonPoint resultButton = new ButtonPoint(nextPoint, description);
        return resultButton;
    }

    private static boolean addToSet(List<ButtonPoint> theSet, ButtonPoint element, boolean throwException) {
        if (theSet.contains(element)) {
            if (throwException) {
                throw new IllegalStateException("element: " + element + " is already in list");
            }
            return false;
        } else {
            theSet.add(element);
            return true;
        }

    }

    private Point calcFun(int x, int y, int upperX, int upperY, IntBinaryOperator opX, IntBinaryOperator opY) {
        int xResult;
        int yResult;
        if (opX.applyAsInt(x, 1) >= 0 && opX.applyAsInt(x, 1) < upperX) {
            xResult = opX.applyAsInt(x, 1);
        } else if (opX.applyAsInt(x, 1) >= 0) {
            xResult = x;
        } else {
            xResult = 0;
        }
        if (opY.applyAsInt(y, 1) >= 0 && opY.applyAsInt(y, 1) < upperY) {
            yResult = opY.applyAsInt(y, 1);
        } else if (opY.applyAsInt(y, 1) >= 0) {
            yResult = y;
        } else {
            yResult = 0;
        }
        return new Point(xResult, yResult);
    }

    public static class Conditions {
        private final Predicate<ButtonPoint> nextOKCond;

        private final Predicate<ButtonPoint> nextNotOKCond;

        private final Predicate<ButtonPoint> endCond;

        private Conditions(Predicate<ButtonPoint> nextOKCond, Predicate<ButtonPoint> nextNotOKCond, Predicate<ButtonPoint> endCond) {
            this.nextOKCond = nextOKCond;
            this.nextNotOKCond = nextNotOKCond;
            this.endCond = endCond;
        }

        public Predicate<ButtonPoint> nextOKCond() {
            return this.nextOKCond;
        }

        public Predicate<ButtonPoint> nextNotOKCond() {
            return this.nextNotOKCond;
        }


        public Predicate<ButtonPoint> endCond() {
            return this.endCond;
        }

        public SuccessIndicator testConditions(ButtonPoint value) {
            Boolean success = false;
            if (value != null) {
                if (this.endCond() != null && this.nextOKCond() != null) {
                    success = this.nextOKCond().test(value);
                } else if (this.endCond() != null && this.nextNotOKCond() != null) {
                    success = this.nextNotOKCond().test(value);
                }
                Boolean finished = this.endCond().test(value);
                Integer indicator = findIndicator(success, finished);
                return new SuccessIndicator(success,
                        finished, indicator);
            } else {
                return new SuccessIndicator(Boolean.FALSE,
                        Boolean.FALSE, SuccessIndicator.NOK);
            }
        }

        public static Conditions instance(ButtonPoint endPoint,
                                          ButtonPoint pointNext, Indicator indicator) {
            Conditions inst;
            if (indicator == Indicator.FOUND_NEXT) {
                inst = new Conditions((e ->
                        (!e.buttonDescr().isMine()
                                && (e.myPoint()
                                .checkPointIsNeighbor(pointNext.myPoint())
                                && (e.myPoint()
                                .compareNearerToEnd(pointNext.myPoint(),
                                        endPoint.myPoint())
                                .xOtherNearer())
                                || (e.myPoint()
                                .compareNearerToEnd(pointNext.myPoint(),
                                        endPoint.myPoint())
                                .yOtherNearer())))),
                        null,
                        (e -> !e.buttonDescr().isMine() && e.buttonDescr().pointType()
                                .equals(endPoint.buttonDescr().pointType())
                                && e.equals(endPoint)));
            } else {
                inst = new Conditions(null,
                        (e -> !e.buttonDescr().isMine()
                                && e.myPoint().checkPointIsNeighbor(pointNext.myPoint())),

                        (e -> !e.buttonDescr().isMine() && e.buttonDescr().pointType()
                                .equals(endPoint.buttonDescr().pointType())
                                && e.equals(endPoint)));
            }
            return inst;
        }


        private Integer findIndicator(Boolean success, Boolean finished) {
            Integer indicator = DecisionTree.SuccessIndicator.NOK;
            if (!success && finished) {
                indicator = DecisionTree.SuccessIndicator.FINISHED;
            } else {
                if (success) {
                    if (finished) {
                        indicator = DecisionTree.SuccessIndicator.FIN_SUCCESS;
                    } else {
                        indicator = DecisionTree.SuccessIndicator.SUCCESSFUL;
                    }
                }
            }
            return indicator;
        }


    }

    public static class SuccessIndicator {
        private final Boolean success;

        private final Boolean finished;

        private final Integer indicator;

        public static final Integer SUCCESSFUL = 0x01;
        public static final Integer FINISHED = 0x02;
        public static final Integer FIN_SUCCESS = SUCCESSFUL + FINISHED;
        public static final Integer NOK = 0x10;


        public SuccessIndicator(Boolean success, Boolean finished, Integer indicator) {
            this.success = success;
            this.finished = finished;
            this.indicator = indicator;
        }

        public Boolean success() {
            return this.success;
        }

        public Integer indicator() {
            return this.indicator;
        }

        public Boolean finished() {
            return this.finished;
        }

        public Boolean isFinishItem() {
            return success() && finished();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            DecisionTree.SuccessIndicator that = (DecisionTree.SuccessIndicator) o;
            return Objects.equals(success, that.success())
                    && Objects.equals(finished, that.finished())
                    && Objects.equals(indicator, that.indicator());
        }

        @Override
        public int hashCode() {
            return Objects.hash(success, finished, indicator);
        }

        @Override
        public String toString() {
            return "SuccessIndicator{" +
                    "success=" + success +
                    ", finished=" + finished +
                    ", indicator=" + indicator +
                    '}';
        }
    }

    public enum Indicator {
        FOUND_NEXT,
        FOUND_AFTER_ERROR;
    }

    public record FunTuple(IntBinaryOperator first, IntBinaryOperator second) {

    }

    public record BPointPlusIndicator(ButtonPoint buttonPoint, Indicator indicator, SuccessIndicator successIndicator) {

    }

    private record BPointPlusInternal(BPointPlusIndicator buttonPlusInd, List<ButtonPoint> lastPoints) {

    }


}
