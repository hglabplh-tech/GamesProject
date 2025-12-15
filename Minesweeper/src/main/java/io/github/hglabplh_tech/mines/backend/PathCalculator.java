package io.github.hglabplh_tech.mines.backend;

public class PathCalculator {

    private final SweeperLogic util;
    private final Labyrinth labyrinth;

    public PathCalculator(SweeperLogic util, Labyrinth labyrinth) {
        this.util = util;
        this.labyrinth = labyrinth;
    }

    public SweeperLogic getUtil() {
        return util;
    }

    public Labyrinth getLabyrinth() {
        return labyrinth;
    }
}
