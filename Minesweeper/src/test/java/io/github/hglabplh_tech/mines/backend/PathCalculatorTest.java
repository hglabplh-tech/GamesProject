package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import io.github.hglabplh_tech.mines.backend.util.DecisionTreeUtils;
import io.github.hglabplh_tech.mines.backend.util.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathCalculatorTest {

    private PathCalculator pathCalculator;

    private SweeperLogic logic;

    private Labyrinth labyrinth;

    @BeforeEach
    void before() {
        this.logic = new SweeperLogic(PlayModes.LABYRINTH, 20, 20, 35);
        this.logic.calculateMines();
        this.labyrinth = this.logic.getLabyrinth();
        this.pathCalculator= new PathCalculator(this.logic, this.labyrinth);
    }
    @Test
    void calculatePath() {
        ButtonPoint start = labyrinth.getStart();
        ButtonPoint next = labyrinth.getFirstBase();
        List<ButtonPoint> result = this.pathCalculator.calculatePath(start, next);
        result.forEach( e -> System.out.println(e));
        System.out.println("Size of path: " + result.size());
    }

    @Test
    void makeConditions() {
        DecisionTree tree = new DecisionTree();
        ButtonPoint rootPoint = new ButtonPoint(new Point(3,3), new ButtonDescription(false, SweepPointType.NORMALPOINT));
        ButtonPoint endPoint = new ButtonPoint(new Point(0,7), new ButtonDescription(false, SweepPointType.NORMALPOINT));
        DecisionTree.TreeElement root = tree.initRoot(rootPoint);
        PathCalculator.BPointPlusIndicator result = this.pathCalculator.calculateNextPoint(root, endPoint, rootPoint, PathCalculator.Indicator.FOUND_NEXT);
        ButtonPoint nextPoint = result.buttonPoint();
        DecisionTree.TreeElement goOn = tree.newTreeElement(root, DecisionTree.TreeElementType.SIBLING, nextPoint);
        PathCalculator.Conditions conditions = this.pathCalculator.makeConditions(endPoint, nextPoint);
        goOn.addSuccessor(conditions.nextCond(), conditions.endCond(), nextPoint);
        DecisionTree.SuccessIndicator indicator = goOn.successIndicator();
        while (!indicator.finished()) {
            conditions = this.pathCalculator.makeConditions(endPoint, nextPoint);
            goOn.addSuccessor(conditions.nextCond(), conditions.endCond(), nextPoint);
            indicator = goOn.successIndicator();
            System.out.println(indicator);
            System.out.println(nextPoint);
            result = this.pathCalculator.calculateNextPoint(goOn, endPoint, result.buttonPoint() ,result.indicator());
            nextPoint = result.buttonPoint();
            goOn = tree.newTreeElement(root, DecisionTree.TreeElementType.SIBLING, nextPoint);
        }
    }

    @Test
    void calculateNextPoint() {
        DecisionTree tree = new DecisionTree();
        ButtonPoint rootPoint = new ButtonPoint(new Point(3,3), new ButtonDescription(false, SweepPointType.NORMALPOINT));
        ButtonPoint endPoint = new ButtonPoint(new Point(0,7), new ButtonDescription(false, SweepPointType.NORMALPOINT));
        DecisionTree.TreeElement root = tree.initRoot(rootPoint);
        PathCalculator.BPointPlusIndicator result = this.pathCalculator.calculateNextPoint(root, endPoint, rootPoint, PathCalculator.Indicator.FOUND_NEXT);
        ButtonPoint nextPoint = result.buttonPoint();
        DecisionTree.TreeElement goOn = tree.newTreeElement(root, DecisionTree.TreeElementType.SIBLING, nextPoint);
        result = this.pathCalculator.calculateNextPoint(goOn, endPoint, result.buttonPoint(),result.indicator());
        ButtonPoint nearerPoint = result.buttonPoint();
        System.out.println(nearerPoint);
        goOn = tree.newTreeElement(root, DecisionTree.TreeElementType.SIBLING, nearerPoint);
        result = this.pathCalculator.calculateNextPoint(goOn, endPoint, result.buttonPoint(), result.indicator());
        nearerPoint = result.buttonPoint();
        System.out.println(nearerPoint);
        goOn = tree.newTreeElement(root, DecisionTree.TreeElementType.SIBLING, nearerPoint);
        result = this.pathCalculator.calculateNextPoint(goOn, endPoint, result.buttonPoint(), result.indicator());
        nearerPoint = result.buttonPoint();
        System.out.println(nearerPoint);
        goOn = tree.newTreeElement(root, DecisionTree.TreeElementType.SIBLING, nearerPoint);
        result = this.pathCalculator.calculateNextPoint(goOn, endPoint, result.buttonPoint(), result.indicator());
        nearerPoint = result.buttonPoint();
        System.out.println(nearerPoint);
    }

    @Test
    void calculateNextPossiblePoint() {
    }

    @Test
    void calculateAllPaths() {
    }
}