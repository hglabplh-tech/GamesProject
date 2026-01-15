package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.games.backend.config.PlayModes;
import io.github.hglabplh_tech.games.backend.util.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

        List<ButtonPoint> pointsList = new ArrayList<>();
        for (BinaryFunDef binaryFunDef: combinators) {
            FunTuple tuple = binaryFunDef.tuple();
            int x = buttonPoint.myPoint().x();
            int y = buttonPoint.myPoint().y();
            ButtonPoint actPoint = new ButtonPoint(new Point(tuple.first().applyAsInt(x, 1),
                    tuple.second().applyAsInt(y, 1)),
                    new ButtonStatus(false, SweepPointType.NORMALPOINT));
            pointsList.add(actPoint);
        }
        int index = 0;
        for (ButtonPoint actPoint : pointsList) {
            FunTuple calcTuple = combinators.get(index).tuple();
            ButtonPoint result = this.pathCalculator.calculateNextPointIntern(buttonPoint, combinators.get(index));
            assertTrue(actPoint.equalsInPoint(result.myPoint()), "Points must fit");
            index++;
        }
    }
}