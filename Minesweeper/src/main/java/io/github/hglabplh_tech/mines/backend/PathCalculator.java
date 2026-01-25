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

import io.github.hglabplh_tech.games.backend.logexcp.GameLogger;
import io.github.hglabplh_tech.games.backend.logexcp.LoggingID;
import io.github.hglabplh_tech.games.backend.util.Point;

import java.util.*;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;

import static io.github.hglabplh_tech.games.backend.logexcp.LoggingID.MINELOG_TRC_ID_00501;
import static io.github.hglabplh_tech.games.backend.logexcp.LoggingID.MINELOG_TRC_ID_00503;

public class PathCalculator {

    private GameLogger logger = GameLogger.logInstance();

    public static final String plusName = "plus";
    public static final IntBinaryOperator plus = Integer::sum;

    public static final String minusName = "minus";
    public static final IntBinaryOperator minus = (a, b) -> a - b;

    public static final String mulName = "mul";
    public static final IntBinaryOperator mul = (a, b) -> a * b;

    public static final String divName = "div";
    public static final IntBinaryOperator div = (a, b) -> a / b;

    public static final List<BinaryFunDef> combinators = new ArrayList<>();


    private final SweeperLogic util;
    private final Labyrinth labyrinth;

    private final List<ButtonPoint> pathTrials = new ArrayList<>();

    private Integer mineAndTryCount;


    static {
        combinators.add(new BinaryFunDef(plusName, minusName, new FunTuple(plus, minus)));
        combinators.add(new BinaryFunDef(minusName, plusName, new FunTuple(minus, plus)));
        combinators.add(new BinaryFunDef(plusName, plusName, new FunTuple(plus, plus)));
        combinators.add(new BinaryFunDef(minusName, minusName, new FunTuple(minus, minus)));
        combinators.add(new BinaryFunDef(minusName, mulName, new FunTuple(minus, mul)));
        combinators.add(new BinaryFunDef(mulName, minusName, new FunTuple(mul, minus)));
        combinators.add(new BinaryFunDef(plusName, mulName, new FunTuple(plus, mul)));
        combinators.add(new BinaryFunDef(mulName, plusName, new FunTuple(mul, plus)));
    }


    public PathCalculator(SweeperLogic util, Labyrinth labyrinth) {
        this.util = util;
        this.labyrinth = labyrinth;
    }

    public List<PathResult> calculateAllPaths() {
        List<PathResult> allPaths = new ArrayList<>();
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

    public PathResult calculatePath(ButtonPoint startPoint, ButtonPoint endPoint) {
        this.mineAndTryCount = 0;
        this.pathTrials.clear();
        List<ButtonPoint> path = new ArrayList<>();
        ButtonPoint nextPoint = startPoint;
        BPointPlusIndicator result = new BPointPlusIndicator(startPoint, Indicator.FOUND_NEXT,
                new SuccessIndicator(true, false, SuccessIndicator.SUCCESSFUL, 1));
        boolean finish = false;
        boolean success = true;
        while (!finish) {
            if (success) {
                addToSet(path, result.buttonPoint(), true);
                logger.logTrace(LoggingID.MINELOG_TRC_ID_00502, result.buttonPoint(), result.successIndicator().getSuccessCounter());

            }
            result = this.calculateAndSetNextPoint(nextPoint, endPoint);
            nextPoint = result.buttonPoint();
            finish = result.successIndicator().finished();
            success = result.successIndicator().success() &&
                    !this.pathTrials.contains(result.buttonPoint());
        }


        logger.logDebug(LoggingID.MINELOG_DEB_ID_00005);
        this.pathTrials.forEach(e -> logger.logDebug(LoggingID.MINELOG_DEB_ID_00007, e));
        List<ButtonPoint> resultPath = new ArrayList<>(path);

        resultPath.add(endPoint);
        logger.logDebug(LoggingID.MINELOG_DEB_ID_00006);
        resultPath.forEach(e -> logger.logDebug(LoggingID.MINELOG_DEB_ID_00007, e));
        // TODO : change all this to LOGGING and throw Exceptions look for all occurrences of System/ println
        if (Labyrinth.reallyCheckCorrectPath(resultPath)) {
            logger.logDebug(LoggingID.MINELOG_DEB_ID_00008);
        } else {
            logger.logDebug(LoggingID.MINELOG_DEB_ID_00009);

        }

        return new PathResult(resultPath, this.mineAndTryCount, this.pathTrials);
    }

    public boolean isPathPoint(List<ButtonPoint> pointList, ButtonPoint compare) {
        long count = pointList.stream().filter(e -> e.equalsInPoint(compare.myPoint())).count();
        return (count > 0);
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
        addToSet(this.pathTrials, startPoint, false);
        this.mineAndTryCount++;
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
        ButtonStatus description = this.getUtil().getFieldsList().get(nextY).get(nextX);
        ButtonPoint lastButtonPoint = startPoint;
        ButtonPoint resultButton = new ButtonPoint(new Point(nextX, nextY), description);
        logger.logTrace(MINELOG_TRC_ID_00503, resultButton, startPoint);

        Conditions conditions = Conditions.instance(startPoint, endPoint);
        SuccessIndicator indicator = conditions.testConditions(resultButton);
        BPointPlusIndicator result = new BPointPlusIndicator(resultButton, Indicator.FOUND_NEXT, indicator);

        if (!indicator.success() && !this.pathTrials.contains(resultButton)) {
            result = new BPointPlusIndicator(resultButton, Indicator.FOUND_AFTER_ERROR, indicator);
            for (BinaryFunDef binaryFunDef : combinators) {
                FunTuple tuple = binaryFunDef.tuple();
                addToSet(this.pathTrials, resultButton, false);
                this.mineAndTryCount++;
                resultButton = result.buttonPoint();
                conditions = Conditions.instance(lastButtonPoint, endPoint);
                if (correctPointCondition(startPoint, resultButton, conditions)) {
                    result = new BPointPlusIndicator(resultButton,
                            Indicator.FOUND_AFTER_ERROR, indicator);
                    addToSet(this.pathTrials, resultButton, false);
                    break;
                } else {
                    result = new BPointPlusIndicator(calculateNextPointIntern(lastButtonPoint, binaryFunDef),
                            Indicator.FOUND_AFTER_ERROR, indicator);
                    //lastButtonPoint = resultButton;
                }
            }
        }
        return result;
    }

    public ButtonPoint calculateNextPointIntern(ButtonPoint startPoint,
                                                BinaryFunDef operatorDef) {

        // FunTuple operatorTuple = operatorDef.tuple();
        List<List<ButtonStatus>> fieldsList = this.getUtil().getFieldsList();
        Point nextPoint = calcFun(startPoint.myPoint().x(),
                startPoint.myPoint().y(),
                this.util.getCx(), this.util.getCy(),
                operatorDef);

        ButtonStatus description = fieldsList.get(nextPoint.y()).get(nextPoint.x());
        ButtonPoint resultButton = new ButtonPoint(nextPoint, description);
        logger.logTrace(MINELOG_TRC_ID_00501, resultButton, startPoint, operatorDef);
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

    public Point calcFun(int x, int y, int upperX, int upperY, BinaryFunDef funTupleDef) {
        FunTuple funTuple = funTupleDef.tuple();
        IntBinaryOperator opX = funTuple.first();
        IntBinaryOperator opY = funTuple.second();
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


    // TODO: think about correctness
    public boolean correctPointCondition(ButtonPoint startButton,
                                         ButtonPoint resultButton,
                                         Conditions conditions) {
        SuccessIndicator indicator = conditions.testConditions(resultButton);
        return (indicator.success()
                && ((!this.labyrinth.getPointsOrder().contains(resultButton))
                && (!this.pathTrials.contains(resultButton)))
                || ((this.pathTrials.contains(resultButton)
                && (resultButton.equalsInPoint(startButton.myPoint())))));
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
            int stepCount = 0;
            if (value != null) {
                if (this.endCond() != null && this.nextOKCond() != null) {
                    success = this.nextOKCond().test(value);
                    stepCount++;
                }
                if (!success && this.endCond() != null && this.nextNotOKCond() != null) {
                    success = this.nextNotOKCond().test(value);
                    stepCount++;
                }
                Boolean finished = this.endCond().test(value);
                Integer indicator = findIndicator(success, finished, stepCount);
                return new SuccessIndicator(success,
                        finished, indicator, stepCount);
            } else {
                return new SuccessIndicator(Boolean.FALSE,
                        Boolean.FALSE, SuccessIndicator.NOK, stepCount);
            }
        }

        public static Conditions instance(ButtonPoint pointNext, ButtonPoint endPoint) {

            return new Conditions((e ->
                    (!e.buttonStatus().isMine()
                            && (e.myPoint()
                            .checkPointIsNeighbor(pointNext.myPoint())
                            && (e.myPoint()
                            .compareNearerToEnd(pointNext.myPoint(),
                                    endPoint.myPoint())
                            .bothNearer())))),

                    (e -> !e.buttonStatus().isMine()
                            && e.myPoint().checkPointIsNeighbor(pointNext.myPoint())),

                    (e -> !e.buttonStatus().isMine() && e.buttonStatus().pointType()
                            .equals(endPoint.buttonStatus().pointType())
                            && e.equals(endPoint)));
        }


        private Integer findIndicator(Boolean success, Boolean finished, int stepCount) {
            Integer indicator = 0;
            if (!success && finished) {
                indicator = DecisionTree.SuccessIndicator.FINISHED;
            } else {
                if (success) {
                    if (finished) {
                        indicator = DecisionTree.SuccessIndicator.FIN_SUCCESS;
                    } else {
                        if (stepCount == 1) {
                            indicator = DecisionTree.SuccessIndicator.SUCCESSFUL;
                        } else if (stepCount == 2) {
                            indicator = DecisionTree.SuccessIndicator.SUCCESSFUL_SECOND;
                        } else {
                            indicator = DecisionTree.SuccessIndicator.NOK;
                        }
                    }
                } else {
                    indicator = DecisionTree.SuccessIndicator.NOK;
                }
            }
            return indicator;
        }


    }


    public static class SuccessIndicator {
        private final Boolean success;

        private final Boolean finished;

        private final Integer indicator;

        private final Integer successCounter;

        public static final Integer SUCCESSFUL = 0x01;
        public static final Integer FINISHED = 0x02;
        public static final Integer FIN_SUCCESS = SUCCESSFUL + FINISHED;
        public static final Integer NOK = 0x10;


        public SuccessIndicator(Boolean success, Boolean finished, Integer indicator, Integer successCounter) {
            this.success = success;
            this.finished = finished;
            this.indicator = indicator;
            this.successCounter = successCounter;
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

        public Integer getSuccessCounter() {
            return this.successCounter;
        }

        public Boolean firstClassSuccess() {
            return (success && (successCounter.equals(1)));
        }

        public Boolean secondClassSuccess() {
            return (success && (successCounter.equals(2)));
        }

        public Boolean isFinishItem() {
            return success() && finished();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            SuccessIndicator that = (SuccessIndicator) o;
            return Objects.equals(this.success(), that.success())
                    && Objects.equals(this.finished(), that.finished())
                    && Objects.equals(this.indicator(), that.indicator());
        }

        @Override
        public int hashCode() {
            return Objects.hash(success, finished, indicator);
        }

        @Override
        public String toString() {
            return "SuccessIndicator{" +
                    "success=" + this.success() +
                    ", finished=" + this.finished() +
                    ", indicator=" + this.indicator() +
                    '}';
        }
    }

    public enum Indicator {
        FOUND_NEXT,
        FOUND_AFTER_ERROR;
    }

    public record FunTuple(IntBinaryOperator first, IntBinaryOperator second) {

    }

    public record BinaryFunDef(String firstOpName, String secondOpName, FunTuple tuple) {

    }

    public record BPointPlusIndicator(ButtonPoint buttonPoint, Indicator indicator, SuccessIndicator successIndicator) {

    }

    public record PathResult(List<ButtonPoint> path, Integer mineAndTryCount, List<ButtonPoint> pathTrials) {


    }


}
