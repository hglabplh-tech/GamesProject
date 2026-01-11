package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import io.github.hglabplh_tech.mines.backend.util.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.hglabplh_tech.mines.backend.PathCalculator.*;
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
        PathResult resultRec = this.pathCalculator.calculatePath(start, next);
        List<ButtonPoint> result = resultRec.path();
        System.out.println("======== Print out path points =======");
        result.forEach( e -> System.out.println(e));
        System.out.println("Size of path: " + result.size());
    }

    @Test
    void calculateAllPaths() {
        List<PathResult> result = this.pathCalculator.calculateAllPaths();
        System.out.println("======== Print out path points =======");
        for (PathResult e : result) {
            List<ButtonPoint> path = e.path();
            System.out.println("Next list \n");
            for (ButtonPoint t : path) {
                System.out.println(t);
            }
            System.out.println("Size of path: " + path.size());
        }
    }

    @Test
    void calculateAndSetNextPoint() {
    }

    @Test
    void calculateNextPointIntern() {
        ButtonPoint buttonPoint = new ButtonPoint(new Point(3,3), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint firstRes = new ButtonPoint(new Point(4,4), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint secondRes = new ButtonPoint(new Point(4,3), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint thirdRes = new ButtonPoint(new Point(2,3), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint fourthRes = new ButtonPoint(new Point(3,4), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint fivRes = new ButtonPoint(new Point(3,2), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint sixthRes = new ButtonPoint(new Point(2,2), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint seventhRes = new ButtonPoint(new Point(2,4), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint eighthRes = new ButtonPoint(new Point(4,2), new ButtonStatus(false, SweepPointType.NORMALPOINT));
        ButtonPoint result = this.pathCalculator.calculateNextPointIntern(buttonPoint, plus, plus);
        assertTrue(firstRes.equalsInPoint(result.myPoint()), "Points must fit");
        result = this.pathCalculator.calculateNextPointIntern(buttonPoint, plus, mul);
        assertTrue(secondRes.equalsInPoint(result.myPoint()), "Points must fit");
        result = this.pathCalculator.calculateNextPointIntern(buttonPoint, minus, mul);
        assertTrue(thirdRes.equalsInPoint(result.myPoint()), "Points must fit");
        result = this.pathCalculator.calculateNextPointIntern(buttonPoint, mul, plus);
        assertTrue(fourthRes.equalsInPoint(result.myPoint()), "Points must fit");
        result = this.pathCalculator.calculateNextPointIntern(buttonPoint, mul, minus);
        assertTrue(fivRes.equalsInPoint(result.myPoint()), "Points must fit");
        result = this.pathCalculator.calculateNextPointIntern(buttonPoint, minus, minus);
        assertTrue(sixthRes.equalsInPoint(result.myPoint()), "Points must fit");
        result = this.pathCalculator.calculateNextPointIntern(buttonPoint, minus, plus);
        assertTrue(seventhRes.equalsInPoint(result.myPoint()), "Points must fit");
        result = this.pathCalculator.calculateNextPointIntern(buttonPoint, plus, minus);
        assertTrue(eighthRes.equalsInPoint(result.myPoint()), "Points must fit");
    }
}