package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import io.github.hglabplh_tech.mines.backend.util.DecisionTreeUtils;
import io.github.hglabplh_tech.mines.backend.util.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
        Set<ButtonPoint> result = this.pathCalculator.calculatePath(start, next);
        result.forEach( e -> System.out.println(e));
        System.out.println("Size of path: " + result.size());
    }

    @Test
    void calculateAllPaths() {
        List<Set<ButtonPoint>> result = this.pathCalculator.calculateAllPaths();
        System.out.println("===== Print out path trial points ====");
        for (Set<ButtonPoint> buttonPoints : this.pathCalculator.getAllPathTrials()) {
            System.out.println("Next list \n");
            for (ButtonPoint t : buttonPoints) {
                System.out.println(t);
            }
        }
        System.out.println("======== Print out path points =======");
        for (Set<ButtonPoint> e : result) {
            System.out.println("Next list \n");
            for (ButtonPoint t : e) {
                System.out.println(t);
            }
        }
    }
}